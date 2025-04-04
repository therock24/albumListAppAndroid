package work.therock24.albumapp.albums.domain.model

/**
 * Represents an album in the application.
 * This class is used to define the properties of an album.
 */
data class Album(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
)