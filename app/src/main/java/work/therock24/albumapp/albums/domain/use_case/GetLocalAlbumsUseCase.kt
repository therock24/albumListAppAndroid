package work.therock24.albumapp.albums.domain.use_case

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

import work.therock24.albumapp.albums.domain.model.Album
import work.therock24.albumapp.albums.domain.repository.AlbumRepository

class GetLocalAlbumsUseCase(private val repository: AlbumRepository) {

    operator fun invoke(): Flow<PagingData<Album>> {
        // TODO: implement later in domain implementation
        return flowOf()
    }
}