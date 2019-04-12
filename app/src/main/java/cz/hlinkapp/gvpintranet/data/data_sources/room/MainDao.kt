package cz.hlinkapp.gvpintranet.data.data_sources.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.hlinkapp.gvpintranet.model.Article
import cz.hlinkapp.gvpintranet.model.Comment
import cz.hlinkapp.gvpintranet.model.PieceOfNews

/**
 * Main implementation of Room's [Dao] (data access object) for handling the database.
 */
@Dao
interface MainDao {

    @Query("SELECT * FROM article ORDER BY id DESC")
    fun getPagedArticles() : DataSource.Factory<Int,Article>

    @Query("SELECT * FROM article WHERE id = :id")
    fun getArticle(id : Int) : LiveData<Article>

    @Query("SELECT * FROM news ORDER BY id DESC LIMIT 15")
    fun getNews() : LiveData<List<PieceOfNews>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveArticles(articles: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNews(news: List<PieceOfNews>)

    @Query("SELECT * FROM comment WHERE articleId = :articleId ORDER BY id DESC")
    fun getPagedComments(articleId: Int) : DataSource.Factory<Int,Comment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveComments(comments: List<Comment>)
}