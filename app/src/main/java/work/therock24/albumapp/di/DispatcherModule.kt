package work.therock24.albumapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Qualifier for identifying the IO-specific [CoroutineDispatcher] in Hilt injection.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

/**
 * Hilt module that provides coroutine dispatchers.
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    /**
     * Provides a [CoroutineDispatcher] optimized for IO-bound work.
     * Marked with [@IoDispatcher] to distinguish it during injection.
     */
    @Provides
    @Singleton
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
