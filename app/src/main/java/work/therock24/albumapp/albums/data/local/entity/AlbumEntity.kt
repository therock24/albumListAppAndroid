package work.therock24.albumapp.albums.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database entity representing an album record in the "albums" table.
 * Defines the schema used by Room for local persistence.
 */
@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey val id: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)
