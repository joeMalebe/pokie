package co.za.pokie.data.repository

import co.za.pokie.data.network.PokieApiService
import co.za.pokie.domain.model.PageData
import co.za.pokie.domain.repository.PokieRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class PokieRepositoryTest {
    private lateinit var client: PokieApiService
    val mockWebServer = MockWebServer()
    lateinit var pokieRepository: PokieRepository

    val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setup() {
        val dispatcher =
            object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse = when (request.requestUrl.toString()) {
                    "http://localhost:${mockWebServer.port}/api/v2/pokemon?limit=100" ->
                        MockResponse()
                            .setResponseCode(
                                200,
                            ).setBody(loadJson("pokemonList.json"))

                    else ->
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(loadJson("pokemonDetail.json"))
                }
            }
        mockWebServer.dispatcher = dispatcher
        mockWebServer.start()

        client =
            Retrofit
                .Builder()
                .client(
                    OkHttpClient()
                        .newBuilder()
                        .addInterceptor(
                            MockInterceptor(
                                mockWebServer.url("/"),
                            ),
                        ).build(),
                ).baseUrl(mockWebServer.url("/"))
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(PokieApiService::class.java)
        pokieRepository = PokieRepositoryImpl(client, Dispatchers.Unconfined)
    }

    @Test
    fun `given page 1 getPokemons should return pokemon data for 10 pokemons when successful`() = runTest {
        val results = mutableListOf<Result<PageData>>()

        val pageNumber = 1
        pokieRepository.getPokemons(pageNumber).toList(results)

        val data = results.first().getOrNull()
        assertEquals(1, results.size)
        assertNotNull(results)
        data?.let {
            assertEquals(10, it.pokemons.size)
            assertEquals(pageNumber + 1, it.currentPage)
            assertEquals(2, it.currentPage)
        } ?: fail("No results data")
    }

    @Test
    fun `given existing pokemons when getPokemons then load new page of pokemons`() = runTest {
        val results = mutableListOf<Result<PageData>>()
        val results2 = mutableListOf<Result<PageData>>()

        val pageNumber = 1
        pokieRepository.getPokemons(pageNumber).toList(results)
        pokieRepository.getPokemons(2).toList(results2)

        val data = results.first().getOrNull()
        assertEquals(1, results.size)
        assertEquals(1, results2.size)
        assertNotNull(results)
        data?.let {
            assertEquals(10, it.pokemons.size)
            assertEquals(pageNumber + 1, it.currentPage)
            assertEquals(2, it.currentPage)
            assertEquals(3, results2.first().getOrNull()!!.currentPage)
            assertTrue(results2.first().getOrNull()!!.isLastPage)
            assertFalse(it.isLastPage)
        } ?: fail("No results data")
    }

    @Test
    fun `when page number is more than maximum then getPokemons should emit empty list`() = runTest {
        val results = mutableListOf<Result<PageData>>()

        pokieRepository.getPokemons(500).toList(results)

        val data = results.first().getOrNull()
        assertEquals(1, results.size)
        assertNotNull(results)
        data?.let { assertTrue(it.pokemons.isEmpty()) } ?: fail("Data should not be null ")
    }

    @Test
    fun `getPokemons should return error data when successful`() = runTest {
        val server = MockWebServer()
        val testClient =
            Retrofit
                .Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(PokieApiService::class.java)
        val results = mutableListOf<Result<PageData>>()
        val repository = PokieRepositoryImpl(testClient, Dispatchers.Unconfined)
        val response = MockResponse().setResponseCode(404)
        server.enqueue(response)

        repository.getPokemons(1).toList(results)
        server.takeRequest()

        val data = results.first().exceptionOrNull()
        data?.let { assertEquals("Client Error", it.message) } ?: fail("No exception from results")
    }

    @Test
    fun `given getPokemons is successful when getPokemonDetail fails then return error`() = runTest {
        val server = MockWebServer()
        val results = mutableListOf<Result<PageData>>()
        val repository = PokieRepositoryImpl(getTestClient(server), Dispatchers.Unconfined)
        val pokemonList = MockResponse().setResponseCode(200).setBody(
            """
           {
               "count":1350,
               "next":"https://pokeapi.co/api/v2/pokemon/?offset=20&limit=20",
               "previous":null,
               "results":[
                  {
                     "name":"bulbasaur",
                     "url":"http://${server.hostName}:${server.port}/api/"
                  },
                  {
                     "name":"ivysaur",
                     "url":"http://${server.hostName}:${server.port}/api/"
                  },
                  {
                     "name":"venusaur",
                     "url":"http://${server.hostName}:${server.port}/api/"
                  }
               ]
           }
            """.trimIndent(),
        )
        val pokemonDetailResponse = MockResponse().setResponseCode(500)
        server.enqueue(pokemonList)
        server.enqueue(pokemonDetailResponse)
        repository.getPokemons(1).toList(results)
        val data = results.first().exceptionOrNull()
        data?.let { assertEquals("Server Error", it.message) } ?: fail("No exception from results")
    }

    private fun getTestClient(server: MockWebServer): PokieApiService = Retrofit
        .Builder()
        .baseUrl(server.url("/"))
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(PokieApiService::class.java)

    fun loadJson(name: String): String = this::class.java.classLoader!!
        .getResource(name)!!
        .readText()
}

class MockInterceptor(
    private val mockServerUrl: HttpUrl,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url

        if (url.host == "pokeapi.co") {
            val newUrl =
                url
                    .newBuilder()
                    .scheme(mockServerUrl.scheme)
                    .host(mockServerUrl.host)
                    .port(mockServerUrl.port)
                    .build()

            request = request.newBuilder().url(newUrl).build()
        }
        return chain.proceed(request)
    }
}
