package work.therock24.albumapp.albums.domain.use_case

import work.therock24.albumapp.core.domain.EmptyResult
import work.therock24.albumapp.core.domain.Error
import work.therock24.albumapp.core.domain.Result
import work.therock24.albumapp.albums.domain.model.Album
import work.therock24.albumapp.albums.domain.repository.AlbumRepository
import javax.inject.Inject

/**
 * Use case for saving a list of albums to the local database.
 * This class delegates the saving logic to [AlbumRepository].
 *
 * @param repository Repository for accessing local album data.
 */
class SaveAlbumsUseCase @Inject constructor(
    private val repository: AlbumRepository
) {

    suspend operator fun invoke(albumList: List<Album>): EmptyResult<Error.DatabaseError> {
        return try {
            repository.saveAlbums(albumList)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(Error.DatabaseError.FAILED_SAVING)
        }
    }
}