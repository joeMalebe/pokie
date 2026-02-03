package co.za.pokie

import co.za.pokie.networking.PokieApiService
import co.za.pokie.networking.PokieClient
import co.za.pokie.networking.repository.PokieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
class PokieAppModule {

    @Provides
    fun pokieRepository(pokieService: PokieApiService): PokieRepository {
        return PokieRepository(pokieService)
    }

    @Provides
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    @Provides
    fun retrofitClient(okHttpClient: OkHttpClient): PokieClient {
        return PokieClient(okHttpClient)
    }

    @Provides
    fun service(retroFitClient: PokieClient): PokieApiService {
        return retroFitClient.client.create(PokieApiService::class.java)
    }
}
