package work.therock24.albumapp.albums.data.remote.api

import retrofit2.http.GET
import work.therock24.albumapp.albums.data.remote.model.AlbumDto

/**
 * Represents the API service for fetching album data.
 * This interface defines the endpoints for the API.
 */
interface AlbumService {
    @GET("img/shared/technical-test.json")
    suspend fun getAlbums(): List<AlbumDto>
}