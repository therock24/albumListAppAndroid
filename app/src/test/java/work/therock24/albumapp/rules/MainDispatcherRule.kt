package work.therock24.albumapp.rules

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A JUnit Test Rule that sets a custom [TestDispatcher] as the main dispatcher
 * for unit tests that use coroutines.
 *
 * This ensures that coroutines launched on [Dispatchers.Main] run in a controlled test environment,
 * allowing for consistent and deterministic behavior in unit tests.
 *
 * Usage:
 * ```
 * @get:Rule
 * val mainDispatcherRule = MainDispatcherRule()
 * ```
 *
 * @param testDispatcher The [TestDispatcher] to use as the main dispatcher during the test.
 *                       Defaults to [UnconfinedTestDispatcher] if not specified.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    /**
     * Called before the test starts.
     * Sets the main dispatcher to the provided [testDispatcher].
     */
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    /**
     * Called after the test finishes.
     * Resets the main dispatcher to its original state.
     */
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
