package cz.hlinkapp.gvpintranet.data.data_sources.room

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.hlinkapp.gvpintranet.model.Article
import cz.hlinkapp.gvpintranet.model.Comment
import cz.hlinkapp.gvpintranet.model.PieceOfNews

/**
 * Main implementation of Room's [RoomDatabase].
 */
@Database(entities = [Article::class,PieceOfNews::class, Comment::class], version = 5, exportSchema = false)
abstract class MainDatabase : RoomDatabase(){
    abstract fun mainDao() : MainDao
}