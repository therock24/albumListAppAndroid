package work.therock24.albumapp.albums.domain.use_case

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import work.therock24.albumapp.albums.domain.model.Album
import work.therock24.albumapp.albums.domain.repository.AlbumRepository
import work.therock24.albumapp.rules.MainDispatcherRule

@OptIn(ExperimentalCoroutinesApi::class)
class GetLocalAlbumsUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val albumRepository: AlbumRepository = mockk(relaxed = true)
    private val useCase = GetLocalAlbumsUseCase(albumRepository)

    @Test
    fun `invoke should return expected paged albums`() = runTest {
        // Given
        val expectedAlbums = listOf(
            Album(id = 1, albumId = 1, title = "Title 1", url = "url1", thumbnailUrl = "thumb1"),
            Album(id = 2, albumId = 1, title = "Title 2", url = "url2", thumbnailUrl = "thumb2")
        )
        val pagingData = PagingData.from(expectedAlbums)

        coEvery { albumRepository.getAllAlbums() } returns flowOf(pagingData)

        // When
        val result = useCase()
        val actualAlbums = result.asSnapshot()

        // Then
        assertThat(actualAlbums).isEqualTo(expectedAlbums)
        coVerify(exactly = 1) { albumRepository.getAllAlbums() }
    }
}
