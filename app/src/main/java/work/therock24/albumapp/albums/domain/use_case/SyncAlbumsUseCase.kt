package work.therock24.albumapp.albums.domain.use_case

import work.therock24.albumapp.core.domain.Error
import work.therock24.albumapp.core.domain.Result
import work.therock24.albumapp.albums.domain.model.Album
import work.therock24.albumapp.albums.domain.repository.AlbumRepository
import work.therock24.albumapp.core.domain.ErrorConverter
import javax.inject.Inject

/**
 * Use case for synchronizing albums from a remote API.
 * This class delegates the synchronization logic to [AlbumRepository].
 *
 * @param repository Repository for accessing album data.
 */
class SyncAlbumsUseCase @Inject constructor(
    private val repository: AlbumRepository
) {

    suspend operator fun invoke(): Result<List<Album>, Error> {
        return try {
            val albums = repository.getAlbumsFromApi()
            Result.Success(albums)
        } catch (e: Exception) {
            Result.Error(ErrorConverter.fromThrowable(e))
        }
    }
}
