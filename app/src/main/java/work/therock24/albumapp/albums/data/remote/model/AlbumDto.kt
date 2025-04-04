package work.therock24.albumapp.albums.data.remote.model


/**
 * Represents an album DTO received from a remote API.
 * This class is used to deserialize JSON data into a data class.
 */
data class AlbumDto(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)