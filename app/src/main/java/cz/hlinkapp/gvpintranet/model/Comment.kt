package cz.hlinkapp.gvpintranet.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A model class representing a comment. Also works as a Room's [Entity].
 */
@Entity
data class Comment(
    @PrimaryKey
    var id: Int? = null,
    var articleId: Int? = null,
    var text: String? = null,
    var author: String? = null,
    var date: String? = null,
    var visible : Int? = null,
    var email : String? = null
) {
}