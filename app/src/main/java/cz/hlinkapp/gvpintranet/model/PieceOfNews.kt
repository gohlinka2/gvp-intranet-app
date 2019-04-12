package cz.hlinkapp.gvpintranet.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A model class representing an event/piece-of-news. Also works as a Room's [Entity].
 */
@Entity(tableName = "news")
data class PieceOfNews(
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null,
    var title : String? = null,
    var description: String? = null,
    var date: String? = null,
    var visible : Int? = null,
    var author : String? = null,
    var email : String? = null
) {
}