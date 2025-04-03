package work.therock24.albumapp.albums.data

import work.therock24.albumapp.albums.domain.model.Album
import work.therock24.albumapp.albums.presentation.models.AlbumUiModel
import work.therock24.albumapp.albums.presentation.models.toAlbumUi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.nio.charset.Charset

/**
 * Utility object for loading static test data from JSON resources.
 */
object TestDataProvider {

    private const val ALBUMS_SUCCESS_JSON = "get_albums_success_response.json"
    private const val ALBUMS_ERROR_JSON = "get_albums_error_response.json"

    val albums: List<AlbumUiModel> by lazy {
        loadJson<List<Album>>(ALBUMS_SUCCESS_JSON).map { it.toAlbumUi() }
    }

    val firstAlbum: AlbumUiModel get() = albums.first()

    /**
     * Generic JSON deserializer for reading JSON files into model objects.
     */
    inline fun <reified T> loadJson(
        fileName: String,
        gson: Gson = Gson(),
        charset: Charset = Charsets.UTF_8
    ): T {
        val inputStream = Thread.currentThread().contextClassLoader
            ?.getResourceAsStream(fileName)
            ?: throw FileNotFoundException("File $fileName not found in resources!")

        return inputStream.bufferedReader(charset).use {
            gson.fromJson(it, object : TypeToken<T>() {}.type)
        }
    }
}
