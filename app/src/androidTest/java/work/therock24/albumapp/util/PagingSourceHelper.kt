package work.therock24.albumapp.util

import androidx.paging.PagingSource
import kotlinx.coroutines.runBlocking

/**
 * Utility class for testing [PagingSource] implementations.
 *
 * This helper allows synchronous execution of [PagingSource.load] operations
 * within test code, making it easier to assert paging results.
 *
 * @param T The type of data being paged.
 * @property pagingSource The [PagingSource] under test.
 */
class PagingSourceHelper<T : Any>(
    private val pagingSource: PagingSource<Int, T>
) {

    /**
     * Loads a page from the [PagingSource] synchronously using the provided [params].
     *
     * @param params The [PagingSource.LoadParams] to pass to the paging source.
     * @return The result of the load operation as [PagingSource.LoadResult].
     */
    fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, T> = runBlocking {
        pagingSource.load(params)
    }

    /**
     * Loads a default initial page with the given [loadSize].
     *
     * @param loadSize The number of items to load.
     * @return A list of loaded items, or an empty list if the load fails or returns no data.
     */
    fun loadInitial(loadSize: Int): List<T> = runBlocking {
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = loadSize,
                placeholdersEnabled = false
            )
        )

        (result as? PagingSource.LoadResult.Page)?.data.orEmpty()
    }
}
