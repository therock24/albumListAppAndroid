package work.therock24.albumapp.albums.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import work.therock24.albumapp.albums.data.local.entity.AlbumEntity

@Dao
interface AlbumDao {

    /**
     * Clears all existing albums and inserts the given list in a single transaction.
     *
     * @param albums List of [AlbumEntity] to insert after clearing the table.
     */
    @Transaction
    suspend fun replaceAll(albums: List<AlbumEntity>) {
        deleteAll()
        insertAll(albums)
    }

    /**
     * Inserts a list of [AlbumEntity] into the database.
     * Existing rows with the same primary key will be replaced.
     *
     * @param albums The albums to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(albums: List<AlbumEntity>)

    /**
     * Deletes all album records from the local database.
     */
    @Query("DELETE FROM albums")
    suspend fun deleteAll()

    /**
     * Returns all albums in the database as a [PagingSource] for pagination.
     *
     * @return A [PagingSource] emitting [AlbumEntity] items by page.
     */
    @Query("SELECT * FROM albums")
    fun getPagedAlbums(): PagingSource<Int, AlbumEntity>
}
