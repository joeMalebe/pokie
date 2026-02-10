package co.za.pokie.data

import co.za.pokie.data.network.PokieApiService
import co.za.pokie.data.network.PokieClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

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
