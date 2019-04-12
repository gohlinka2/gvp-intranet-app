package cz.hlinkapp.gvpintranet.utils

import android.content.SharedPreferences
import cz.hlinkapp.gvpintranet.model.Article
import cz.hlinkapp.gvpintranet.model.PieceOfNews
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A simple helper class for saving and reading predefined values from shared preferences.
 */
@Singleton
class SharedPrefUtil
/**
 * Default constructor for this class. It's important to pass the DEFAULT shared prefs.
 * @param sharedPreferences The default shared preferences of the app.
 */
@Inject constructor(private val sharedPreferences: SharedPreferences) {

    /**
     * Sets the timestamp of when the article data was last fetched to this moment.
     * Call when the data has just been fetched.
     */
    fun updateArticlesLastFetchedTime() {
        with(sharedPreferences.edit()) {
            putLong(KEY_ARTICLES_LAST_FETCHED_TIME,Calendar.getInstance().timeInMillis)
            apply()
        }
    }

    /**
     * Determines whether to fetch new articles from the server or use the local db.
     * @return True if the server data should be fetched.
     */
    fun isArticleDataOld() : Boolean {
        return (Calendar.getInstance().timeInMillis - getArticlesLastFetchedTime()) > DATA_EXPIRATION_TIME_MILLIS
    }

    /**
     * Reads the timestamp of when the article data was last fetched.
     * @return the timestamp
     */
    private fun getArticlesLastFetchedTime() : Long {
        return sharedPreferences.getLong(KEY_ARTICLES_LAST_FETCHED_TIME,0)
    }

    /**
     * Sets the timestamp of when the news data was last fetched to this moment.
     * Call when the data has just been fetched.
     */
    fun updateNewsLastFetchedTime() {
        with(sharedPreferences.edit()) {
            putLong(KEY_NEWS_LAST_FETCHED_TIME,Calendar.getInstance().timeInMillis)
            apply()
        }
    }

    /**
     * Determines whether to fetch new news from the server or use the local db.
     * @return True if the server data should be fetched.
     */
    fun isNewsDataOld() : Boolean {
        return (Calendar.getInstance().timeInMillis - getNewsLastFetchedTime()) > DATA_EXPIRATION_TIME_MILLIS
    }

    /**
     * Reads the timestamp of when the news data was last fetched.
     * @return the timestamp
     */
    private fun getNewsLastFetchedTime() : Long {
        return sharedPreferences.getLong(KEY_NEWS_LAST_FETCHED_TIME,0)
    }

    /**
     * Reads any string shared preference with the provided key, or null if not found.
     */
    fun getStringSharedPref(key: String) : String?{
        return sharedPreferences.getString(key,null)
    }

    /**
     * Saves a draft of the user's work on an article.
     * Only the following fields are saved: [Article.title], [Article.description], [Article.content] and [Article.note].
     */
    fun saveArticleDraft(article: Article) {
        with(sharedPreferences.edit()) {
            putString(KEY_ARTICLE_DRAFT_TITLE,article.title)
            putString(KEY_ARTICLE_DRAFT_DESCRIPTION,article.description)
            putString(KEY_ARTICLE_DRAFT_CONTENT,article.content)
            putString(KEY_ARTICLE_DRAFT_NOTE,article.note)
            apply()
        }
    }

    /**
     * Clears a previously saved article draft.
     */
    fun clearArticleDraft() {
        with(sharedPreferences.edit()) {
            putString(KEY_ARTICLE_DRAFT_TITLE,"")
            putString(KEY_ARTICLE_DRAFT_DESCRIPTION,"")
            putString(KEY_ARTICLE_DRAFT_CONTENT,"")
            putString(KEY_ARTICLE_DRAFT_NOTE,"")
            apply()
        }
    }

    /**
     * Retrieves a previously saved draft of an article.
     * Only the following fields are retrieved: [Article.title], [Article.description], [Article.content] and [Article.note].
     */
    fun retrieveArticleDraft() : Article {
        val art = Article()
        art.title = sharedPreferences.getString(KEY_ARTICLE_DRAFT_TITLE,"")
        art.description = sharedPreferences.getString(KEY_ARTICLE_DRAFT_DESCRIPTION,"")
        art.content = sharedPreferences.getString(KEY_ARTICLE_DRAFT_CONTENT,"")
        art.note = sharedPreferences.getString(KEY_ARTICLE_DRAFT_NOTE,"")
        return art
    }

    /**
     * Saves a draft of the user's work on an event.
     * Only the following fields are saved: [PieceOfNews.title] and [PieceOfNews.description].
     */
    fun saveEventDraft(event: PieceOfNews) {
        with(sharedPreferences.edit()) {
            putString(KEY_EVENT_DRAFT_TITLE,event.title)
            putString(KEY_EVENT_DRAFT_DESCRIPTION,event.description)
            apply()
        }
    }

    /**
     * Clears a previously saved event draft.
     */
    fun clearEventDraft() {
        with(sharedPreferences.edit()) {
            putString(KEY_EVENT_DRAFT_TITLE,"")
            putString(KEY_EVENT_DRAFT_DESCRIPTION,"")
            apply()
        }
    }

    /**
     * Retrieves a previously saved draft of an event.
     * Only the following fields are retrieved: [PieceOfNews.title] and [PieceOfNews.description].
     */
    fun retrieveEventDraft() : PieceOfNews {
        val even = PieceOfNews()
        even.title = sharedPreferences.getString(KEY_EVENT_DRAFT_TITLE,"")
        even.description = sharedPreferences.getString(KEY_EVENT_DRAFT_DESCRIPTION,"")
        return even
    }
    
    

    companion object {
        private const val KEY_ARTICLES_LAST_FETCHED_TIME = "articlesLastFetchedTime"
        private const val KEY_NEWS_LAST_FETCHED_TIME = "newsLastFetchedTime"
        private const val DATA_EXPIRATION_TIME_MILLIS = 1000 * 60 * 2 //two minutes

        private const val KEY_ARTICLE_DRAFT_TITLE = "draft_articleTitle"
        private const val KEY_ARTICLE_DRAFT_DESCRIPTION = "draft_articleDescription"
        private const val KEY_ARTICLE_DRAFT_CONTENT = "draft_articleContent"
        private const val KEY_ARTICLE_DRAFT_NOTE = "draft_articleNote"

        private const val KEY_EVENT_DRAFT_TITLE = "draft_eventTitle"
        private const val KEY_EVENT_DRAFT_DESCRIPTION = "draft_eventDescription"
    }
}