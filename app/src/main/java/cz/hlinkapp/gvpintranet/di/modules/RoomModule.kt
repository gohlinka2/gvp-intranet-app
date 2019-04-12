package cz.hlinkapp.gvpintranet.di.modules

import android.app.Application
import androidx.room.Room
import cz.hlinkapp.gvpintranet.data.data_sources.room.MainDao
import cz.hlinkapp.gvpintranet.data.data_sources.room.MainDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * A Dagger2 module which provides Room-related classes.
 */
@Module
class RoomModule(private val app: Application){

    @Singleton
    @Provides
    fun provideDb(): MainDatabase {
        return Room
            .databaseBuilder(app, MainDatabase::class.java, "mainDb.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMainDao(db: MainDatabase): MainDao {
        return db.mainDao()
    }
}