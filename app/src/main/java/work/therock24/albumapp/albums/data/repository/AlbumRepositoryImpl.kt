package work.therock24.albumapp.albums.data.repository

import work.therock24.albumapp.di.IoDispatcher
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import work.therock24.albumapp.albums.domain.model.Album
import work.therock24.albumapp.albums.domain.repository.AlbumRepository
import work.therock24.albumapp.albums.data.local.dao.AlbumDao
import work.therock24.albumapp.albums.data.mapper.toAlbum
import work.therock24.albumapp.albums.data.mapper.toAlbumEntity
import work.therock24.albumapp.albums.data.remote.api.AlbumService



class AlbumRepositoryImpl @Inject constructor(
    private val albumDao: AlbumDao,
    private val albumService: AlbumService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AlbumRepository {

    override suspend fun saveAlbums(albumsDto: List<Album>) = withContext(ioDispatcher) {
        val albumEntities = albumsDto.map { it.toAlbumEntity() }
        albumDao.replaceAll(albumEntities)
    }

    override fun getAllAlbums(): Flow<PagingData<Album>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { albumDao.getPagedAlbums() }
        ).flow.map { pagingData ->
            pagingData.map { it.toAlbum() }
        }
    }

    override suspend fun getAlbumsFromApi(): List<Album> = withContext(ioDispatcher) {
        albumService.getAlbums()
            .map { it.toAlbum() }
    }
}
