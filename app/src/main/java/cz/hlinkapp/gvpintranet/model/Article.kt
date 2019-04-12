package cz.hlinkapp.gvpintranet.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A model class representing an article. Also works as a Room's [Entity].
 */
@Entity
data class Article(
    @PrimaryKey
    var id: Int? = null,
    var title: String? = null,
    var description: String? = null,
    var content: String? = null,
    var author: String? = null,
    var date: String? = null,
    var email: String? = null,
    var visible : Int? = null,
    var note: String? = null
) {
}