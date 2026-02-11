package co.za.pokie

import co.za.pokie.data.network.PokieApiService
import co.za.pokie.data.repository.PokieRepositoryImpl
import co.za.pokie.domain.repository.PokieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class PokieAppModule {
    @Provides
    fun pokieRepository(pokieService: PokieApiService): PokieRepository = PokieRepositoryImpl(pokieService)
}
