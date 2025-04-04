package work.therock24.albumapp.rules

import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import work.therock24.albumapp.util.TestDataProvider.readFromJSONToString
import java.net.HttpURLConnection
import javax.net.ssl.HttpsURLConnection

/**
 * Test rule that sets up a [MockWebServer] and provides utilities
 * to stub Retrofit services for integration-style API testing.
 */
class RemoteTestRule : TestWatcher() {

    private val mockWebServer = MockWebServer()

    val baseUrl get() = mockWebServer.url("/").toString()

    override fun starting(description: Description) {
        super.starting(description)
        mockWebServer.start()
    }

    override fun finished(description: Description) {
        super.finished(description)
        mockWebServer.shutdown()
    }

    /**
     * Creates a Retrofit test service using the mock server's URL.
     *
     * @return A Retrofit service implementation of [Service].
     */
    inline fun <reified Service> createTestService(): Service {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(Service::class.java)
    }

    /**
     * Enqueues a raw JSON response with a custom HTTP status code.
     */
    fun enqueueResponse(jsonBody: String, code: Int = HttpURLConnection.HTTP_OK) {
        val response = MockResponse()
            .setBody(jsonBody)
            .setResponseCode(code)
        mockWebServer.enqueue(response)
    }

    /**
     * Enqueues a success response using a JSON file from the resources.
     */
    fun enqueueSuccess(jsonFileName: String) {
        val json = readFromJSONToString(jsonFileName)
        enqueueResponse(json, HttpURLConnection.HTTP_OK)
    }

    /**
     * Enqueues an error response using a JSON file from the resources.
     */
    fun enqueueError(jsonFileName: String, code: Int = HttpsURLConnection.HTTP_BAD_REQUEST) {
        val json = readFromJSONToString(jsonFileName)
        enqueueResponse(json, code)
    }
}
