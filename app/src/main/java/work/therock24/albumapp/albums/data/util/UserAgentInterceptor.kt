package work.therock24.albumapp.albums.data.util

import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp interceptor that appends a custom "User-Agent" header to all outgoing HTTP requests.
 *
 * @param userAgent The value to be used for the "User-Agent" header.
 */
class UserAgentInterceptor(private val userAgent: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val modifiedRequest = original.newBuilder()
            .header("User-Agent", userAgent)
            .build()

        return chain.proceed(modifiedRequest)
    }
}
