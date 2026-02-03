package co.za.pokie.repository

import co.za.pokie.domain.model.Pokemon
import co.za.pokie.domain.viewmodel.HomeViewModel
import co.za.pokie.networking.service.PokieApiService
import co.za.pokie.networking.repository.PokieRepository
import co.za.pokie.networking.util.ApiResult
import junit.framework.TestCase.assertEquals
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
import org.mockito.Mock
import org.mockito.Mockito.mock
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class PokieRepositoryTest {
    private lateinit var client: PokieApiService
    val mockWebServer = MockWebServer()
    lateinit var pokiecRepository: PokieRepository

    @Mock
    lateinit var mockRepository: PokieRepository

    lateinit var viewModel: HomeViewModel
    val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setup() {
        mockRepository = mock<PokieRepository>()

        val dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.requestUrl.toString()) {
                    "http://localhost:${mockWebServer.port}/api/v2/pokemon?limit=100" -> MockResponse().setResponseCode(
                        200
                    )
                        .setBody(loadJson("pokemonList.json"))

                    else -> MockResponse().setResponseCode(200)
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
                        mockWebServer.url("/")
                    )
                ).build()
            )
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(PokieApiService::class.java)
        pokiecRepository = PokieRepository(client, Dispatchers.Unconfined)
        viewModel = HomeViewModel(pokiecRepository)
    }

    @Test
    fun `getPokemons should return pokemon data when successful`() = runTest {
        val results = mutableListOf<ApiResult<List<Pokemon>>>()

        pokiecRepository.getPokemons().toList(results)

        val data = (results.first() as ApiResult.Success).data
        assertEquals(4, results.size)
        assertNotNull(results)
        assertEquals(20, data.size)
    }

    @Test
    fun `getPokemons should return error data when successful`() = runTest {
        val server = MockWebServer()
        val testClient = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PokieApiService::class.java)
        val results = mutableListOf<ApiResult<List<Pokemon>>>()
        val repository = PokieRepository(testClient, Dispatchers.Unconfined)
        val response = MockResponse().setResponseCode(404)
        server.enqueue(response)

        repository.getPokemons().toList(results)
        server.takeRequest()

        val data = (results.first() as ApiResult.Error)
        assertEquals("Client Error", data.message)
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
            val newUrl = url.newBuilder()
                .scheme(mockServerUrl.scheme)
                .host(mockServerUrl.host)
                .port(mockServerUrl.port)
                .build()

            request = request.newBuilder().url(newUrl).build()
        }
        return chain.proceed(request)
    }
}