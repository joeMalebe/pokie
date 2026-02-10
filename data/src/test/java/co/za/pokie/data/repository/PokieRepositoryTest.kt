package co.za.pokie.data.repository

import co.za.pokie.data.network.PokieApiService
import co.za.pokie.domain.model.Pokemon
import co.za.pokie.domain.viewmodel.HomeViewModel
import junit.framework.TestCase.assertEquals
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

    lateinit var viewModel: HomeViewModel
    val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setup() {
        val dispatcher =
            object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse {
                    return when (request.requestUrl.toString()) {
                        "http://localhost:${mockWebServer.port}/api/v2/pokemon?limit=100" ->
                            MockResponse().setResponseCode(
                                200,
                            )
                                .setBody(loadJson("pokemonList.json"))

                        else ->
                            MockResponse().setResponseCode(200)
                                .setBody(loadJson("pokemonDetail.json"))
                    }
                }
            }
        mockWebServer.dispatcher = dispatcher
        mockWebServer.start()

        client =
            Retrofit.Builder().client(
                OkHttpClient().newBuilder().addInterceptor(
                    MockInterceptor(
                        mockWebServer.url("/"),
                    ),
                ).build(),
            )
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(PokieApiService::class.java)
        pokieRepository = PokieRepositoryImpl(client, Dispatchers.Unconfined)
        viewModel = HomeViewModel(pokieRepository)
    }

    @Test
    fun `getPokemons should return pokemon data when successful`() =
        runTest {
            val results = mutableListOf<Result<List<Pokemon>>>()

            pokieRepository.getPokemons().toList(results)

            val data = results.first().getOrNull()
            assertEquals(4, results.size)
            assertNotNull(results)
            data?.let { assertEquals(20, it.size) } ?: fail("No results data")
        }

    @Test
    fun `getPokemons should return error data when successful`() =
        runTest {
            val server = MockWebServer()
            val testClient =
                Retrofit.Builder()
                    .baseUrl(server.url("/"))
                    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                    .build()
                    .create(PokieApiService::class.java)
            val results = mutableListOf<Result<List<Pokemon>>>()
            val repository = PokieRepositoryImpl(testClient, Dispatchers.Unconfined)
            val response = MockResponse().setResponseCode(404)
            server.enqueue(response)

            repository.getPokemons().toList(results)
            server.takeRequest()

            val data = results.first().exceptionOrNull()
            data?.let { assertEquals("Client Error", it.message) } ?: fail("No exception from results")
        }

    fun loadJson(name: String): String {
        return this::class.java.classLoader!!
            .getResource(name)!!
            .readText()
    }
}

class MockInterceptor(private val mockServerUrl: HttpUrl) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url

        if (url.host == "pokeapi.co") {
            val newUrl =
                url.newBuilder()
                    .scheme(mockServerUrl.scheme)
                    .host(mockServerUrl.host)
                    .port(mockServerUrl.port)
                    .build()

            request = request.newBuilder().url(newUrl).build()
        }
        return chain.proceed(request)
    }
}
