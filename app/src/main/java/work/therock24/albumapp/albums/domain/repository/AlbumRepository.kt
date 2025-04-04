package work.therock24.albumapp.albums.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import work.therock24.albumapp.albums.domain.model.Album

/**
 * Repository interface for managing album data across local and remote sources.
 * Defines methods for saving, retrieving, and syncing album information.
 */
interface AlbumRepository {

    /**
     * Persists a list of albums into the local database.
     *
     * @param albumsDto The list of [Album] objects to be saved locally.
     */
    suspend fun saveAlbums(albumsDto: List<Album>)

    /**
     * Returns all locally stored albums as a paginated stream.
     *
     * @return A [Flow] emitting [PagingData] of [Album].
     */
    fun getAllAlbums(): Flow<PagingData<Album>>

    /**
     * Retrieves albums from the remote API.
     *
     * @return A list of [Album] objects fetched from the network.
     */
    suspend fun getAlbumsFromApi(): List<Album>
}
