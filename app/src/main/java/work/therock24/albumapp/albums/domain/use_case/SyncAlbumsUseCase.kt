package work.therock24.albumapp.albums.domain.use_case

import work.therock24.albumapp.core.domain.Error
import work.therock24.albumapp.core.domain.Result
import work.therock24.albumapp.albums.domain.model.Album
import work.therock24.albumapp.albums.domain.repository.AlbumRepository

class SyncAlbumsUseCase(private val repository: AlbumRepository) {

    operator fun invoke(): Result<List<Album>, Error> {
        // TODO: implement later in domain implementation
        return Result.Success(listOf())
    }
}