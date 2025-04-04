package work.therock24.albumapp.albums.domain.use_case

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import work.therock24.albumapp.albums.domain.model.Album
import work.therock24.albumapp.albums.domain.repository.AlbumRepository
import work.therock24.albumapp.core.domain.Error
import work.therock24.albumapp.core.domain.Result
import work.therock24.albumapp.rules.MainDispatcherRule
import java.io.IOException

class SyncAlbumsUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val albumRepository: AlbumRepository = mockk()
    private val useCase = SyncAlbumsUseCase(albumRepository)

    @Test
    fun `invoke - when repository returns albums - should return success`() = runTest {
        // Given
        val albums = listOf(
            Album(id = 1, albumId = 1, title = "title1", url = "url1", thumbnailUrl = "thumb1"),
            Album(id = 2, albumId = 2, title = "title2", url = "url2", thumbnailUrl = "thumb2")
        )
        coEvery { albumRepository.getAlbumsFromApi() } returns albums

        // When
        val result = useCase()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(albums)
    }

    @Test
    fun `invoke - when repository throws exception - should return error`() = runTest {
        // Given
        coEvery { albumRepository.getAlbumsFromApi() } throws RuntimeException("Some network error")

        // When
        val result = useCase()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val error = (result as Result.Error).error
        assertThat(error).isEqualTo(Error.Unknown)
    }

    @Test
    fun `invoke - when repository throws IOException - should return Server Error`() = runTest {
        // Given
        coEvery { albumRepository.getAlbumsFromApi() } throws IOException("Some network error")

        // When
        val result = useCase()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val error = (result as Result.Error).error
        assertThat(error).isEqualTo(Error.NetworkError.SERVER_ERROR)
    }
}
