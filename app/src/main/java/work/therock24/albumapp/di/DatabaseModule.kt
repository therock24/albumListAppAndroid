package work.therock24.albumapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import work.therock24.albumapp.albums.data.local.dao.AlbumDao
import work.therock24.albumapp.albums.data.local.database.MainDatabase
import javax.inject.Singleton

/**
 * Hilt module that provides Room database and DAO dependencies.
 * Installed in [SingletonComponent] to ensure application-wide singleton scope.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MainDatabase =
        Room.databaseBuilder(
            context,
            MainDatabase::class.java,
            "app_database"
        ).build()

    @Provides
    fun provideAlbumDao(database: MainDatabase): AlbumDao =
        database.albumDao()
}
