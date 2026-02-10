package co.za.pokie

import co.za.pokie.data.network.PokieApiService
import co.za.pokie.data.network.PokieClient
import co.za.pokie.data.repository.PokieRepositoryImpl
import co.za.pokie.domain.model.PokieRepository
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
    fun pokieRepository(pokieService: PokieApiService): PokieRepository = PokieRepositoryImpl(pokieService)

    @Provides
    fun okHttpClient(): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            },
        ).build()

    @Provides
    fun retrofitClient(okHttpClient: OkHttpClient): PokieClient = PokieClient(okHttpClient)

    @Provides
    fun service(retroFitClient: PokieClient): PokieApiService = retroFitClient.client.create(PokieApiService::class.java)
}
