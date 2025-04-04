package work.therock24.albumapp.core.domain


import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorConverter {

    fun fromThrowable(throwable: Throwable): Error = when (throwable) {
        is HttpException -> mapHttpException(throwable)
        is SocketTimeoutException -> Error.NetworkError.REQUEST_TIMEOUT
        is UnknownHostException -> Error.NetworkError.NO_INTERNET
        is IOException -> Error.NetworkError.SERVER_ERROR
        else -> Error.Unknown
    }

    private fun mapHttpException(exception: HttpException): Error.NetworkError = when (exception.code()) {
        HttpURLConnection.HTTP_FORBIDDEN -> Error.NetworkError.ACCESS_DENIED
        HttpURLConnection.HTTP_NOT_FOUND -> Error.NetworkError.NOT_FOUND
        HttpURLConnection.HTTP_CLIENT_TIMEOUT -> Error.NetworkError.REQUEST_TIMEOUT
        HttpURLConnection.HTTP_INTERNAL_ERROR -> Error.NetworkError.INTERNAL_SERVER_ERROR
        HttpURLConnection.HTTP_UNAVAILABLE -> Error.NetworkError.SERVER_ERROR
        429 -> Error.NetworkError.TOO_MANY_REQUESTS
        else -> Error.NetworkError.UNKNOWN
    }
}

