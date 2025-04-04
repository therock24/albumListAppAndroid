package work.therock24.albumapp.core.domain

sealed interface Error {
    enum class NetworkError: Error {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER_ERROR,
        UNKNOWN,
        NOT_FOUND,
        ACCESS_DENIED,
        INTERNAL_SERVER_ERROR,
    }

    enum class DatabaseError: Error {
        FAILED_SAVING,
        FAILED_LOADING
    }


    data object Unknown : Error
}

