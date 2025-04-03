package work.therock24.albumapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import work.therock24.albumapp.albums.domain.repository.AlbumRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesAlbumRepository(): AlbumRepository {
        // TODO: implement later in data implementation
        return AlbumRepositoryImpl()
    }

    class AlbumRepositoryImpl():AlbumRepository {}

}