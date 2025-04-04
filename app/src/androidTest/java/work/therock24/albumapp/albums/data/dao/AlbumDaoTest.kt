package work.therock24.albumapp.albums.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import work.therock24.albumapp.albums.data.local.dao.AlbumDao
import work.therock24.albumapp.albums.data.local.database.MainDatabase
import work.therock24.albumapp.util.PagingSourceHelper
import work.therock24.albumapp.util.TestDataProvider

@RunWith(AndroidJUnit4::class)
class AlbumDaoTest {

    private lateinit var database: MainDatabase
    private lateinit var dao: AlbumDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            MainDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.albumDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAll() = runTest {
        val albums = TestDataProvider.albumEntityList
        dao.insertAll(albums)

        val result = PagingSourceHelper(dao.getPagedAlbums()).loadInitial(albums.size)
        assertThat(result).isEqualTo(albums)
    }

    @Test
    fun replaceAll() = runTest {
        val initialAlbums = TestDataProvider.albumEntityList.take(5)
        val updatedAlbums = TestDataProvider.albumEntityList.takeLast(5)

        dao.insertAll(initialAlbums)
        dao.replaceAll(updatedAlbums)

        val result = PagingSourceHelper(dao.getPagedAlbums()).loadInitial(updatedAlbums.size)
        assertThat(result).isEqualTo(updatedAlbums)
    }

    @Test
    fun getPagedAlbums() = runTest {
        val albums = TestDataProvider.albumEntityList.take(10)
        dao.insertAll(albums)

        val result = PagingSourceHelper(dao.getPagedAlbums()).loadInitial(albums.size)
        assertThat(result).isEqualTo(albums)
    }
}
