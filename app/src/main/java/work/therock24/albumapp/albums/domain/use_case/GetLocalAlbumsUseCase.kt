package work.therock24.albumapp.albums.domain.use_case

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import work.therock24.albumapp.albums.domain.model.Album

import work.therock24.albumapp.albums.domain.repository.AlbumRepository
import javax.inject.Inject

/**
 * Use case for fetching paginated albums from the local database.
 * This class delegates the retrieval logic to [AlbumRepository] and exposes the result
 * as a stream of [PagingData] suitable for UI consumption.
 *
 * @param albumRepository Repository for accessing local album data.
 */
class GetLocalAlbumsUseCase @Inject constructor(
    private val albumRepository: AlbumRepository
) {

    /**
     * Fetches albums from the local data source.
     * Declared as an operator to allow usage like a function call.
     *
     * @return A [Flow] emitting [PagingData] of [AlbumUiModel].
     */
    operator fun invoke(): Flow<PagingData<Album>> {
        return albumRepository.getAllAlbums()
    }
}