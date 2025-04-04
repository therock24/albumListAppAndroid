package work.therock24.albumapp.albums.data.repository

import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import work.therock24.albumapp.albums.data.local.dao.AlbumDao
import work.therock24.albumapp.albums.data.mapper.toAlbumEntity
import work.therock24.albumapp.albums.data.remote.api.AlbumService
import work.therock24.albumapp.rules.MainDispatcherRule
import work.therock24.albumapp.rules.RemoteTestRule
import work.therock24.albumapp.util.TestDataProvider

class AlbumRepositoryImplTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @get:Rule
    val remoteRule = RemoteTestRule()

    private lateinit var repository: AlbumRepositoryImpl
    private val albumDao: AlbumDao = mockk(relaxed = true) // Only verifying insert

    private val expectedAlbums = TestDataProvider.loadJson<List<work.therock24.albumapp.albums.domain.model.Album>>(
        "get_albums_success_response.json"
    )

    @Before
    fun setup() {
        repository = AlbumRepositoryImpl(
            albumDao = albumDao,
            albumService = remoteRule.createTestService<AlbumService>(),
            ioDispatcher = dispatcherRule.testDispatcher
        )
    }

    @Test
    fun `getAlbumsFromApi - success - should return domain models`() = runTest {
        // Given
        remoteRule.enqueueSuccess("get_albums_success_response.json")

        // When
        val result = repository.getAlbumsFromApi()

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result).isEqualTo(expectedAlbums)
    }

    @Test
    fun `getAlbumsFromApi - error - should throw exception`() = runTest {
        // Given
        remoteRule.enqueueError("get_albums_error_response.json")

        // When
        val result = runCatching { repository.getAlbumsFromApi() }

        // Then
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `saveAlbums - should map and insert album entities`() = runTest {
        // Given
        val albumsToSave = expectedAlbums

        // When
        repository.saveAlbums(albumsToSave)

        // Then
        val expectedEntities = albumsToSave.map { it.toAlbumEntity() }
        coVerify(exactly = 1) { albumDao.replaceAll(expectedEntities) }
    }
}
