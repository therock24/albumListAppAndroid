package work.therock24.albumapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import work.therock24.albumapp.albums.data.local.dao.AlbumDao
import work.therock24.albumapp.albums.data.remote.api.AlbumService
import work.therock24.albumapp.albums.data.repository.AlbumRepositoryImpl
import work.therock24.albumapp.albums.domain.repository.AlbumRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesAlbumRepository(
        albumService: AlbumService,
        albumDao: AlbumDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): AlbumRepository {
        return AlbumRepositoryImpl(
            albumService = albumService,
            albumDao = albumDao,
            ioDispatcher = ioDispatcher
        )
    }
}