package work.therock24.albumapp.albums.domain.use_case

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import work.therock24.albumapp.albums.domain.model.Album
import work.therock24.albumapp.albums.domain.repository.AlbumRepository
import work.therock24.albumapp.core.domain.Error
import work.therock24.albumapp.core.domain.Result
import work.therock24.albumapp.rules.MainDispatcherRule

class SaveAlbumsUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: AlbumRepository = mockk(relaxed = true)
    private val useCase = SaveAlbumsUseCase(repository)

    private val albums = listOf(
        Album(id = 1, albumId = 1, title = "title1", url = "url1", thumbnailUrl = "thumb1"),
        Album(id = 2, albumId = 2, title = "title2", url = "url2", thumbnailUrl = "thumb2")
    )

    @Test
    fun `invoke - repository succeeds - returns success result`() = runTest {
        // When
        val result = useCase(albums)

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        coVerify(exactly = 1) { repository.saveAlbums(albums) }
    }

    @Test
    fun `invoke - repository throws - returns error result`() = runTest {
        // Given
        coEvery { repository.saveAlbums(any()) } throws RuntimeException("DB write error")

        // When
        val result = useCase(albums)

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val error = (result as Result.Error).error
        assertThat(error).isEqualTo(Error.DatabaseError.FAILED_SAVING)
        coVerify(exactly = 1) { repository.saveAlbums(albums) }
    }
}
