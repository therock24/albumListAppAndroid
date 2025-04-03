package work.therock24.albumapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import work.therock24.albumapp.albums.domain.repository.AlbumRepository
import work.therock24.albumapp.albums.domain.use_case.GetLocalAlbumsUseCase
import work.therock24.albumapp.albums.domain.use_case.SaveAlbumsUseCase
import work.therock24.albumapp.albums.domain.use_case.SyncAlbumsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providesGetLocalAlbumUseCase(
        albumRepository: AlbumRepository,
    ): GetLocalAlbumsUseCase {
        return GetLocalAlbumsUseCase(albumRepository)
    }

    @Provides
    @Singleton
    fun providesSyncAlbumUseCase(
        albumRepository: AlbumRepository,
    ): SyncAlbumsUseCase {
        return SyncAlbumsUseCase(albumRepository)
    }

    @Provides
    @Singleton
    fun providesSaveAlbumsUseCase(
        albumRepository: AlbumRepository,
    ): SaveAlbumsUseCase {
        return SaveAlbumsUseCase(albumRepository)
    }
}