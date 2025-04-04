package work.therock24.albumapp.albums.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import work.therock24.albumapp.albums.data.local.dao.AlbumDao
import work.therock24.albumapp.albums.data.local.entity.AlbumEntity

/**
 * The main Room database for the application.
 * Provides access to persisted data and exposes DAOs for database operations.
 *
 * Contains a single table for [AlbumEntity].
 */
@Database(entities = [AlbumEntity::class], version = 1, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}