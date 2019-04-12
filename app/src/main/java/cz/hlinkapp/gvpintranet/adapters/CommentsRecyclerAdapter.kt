package cz.hlinkapp.gvpintranet.adapters

import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.model.Comment
import kotlinx.android.synthetic.main.item_comment.view.*

/**
 * A [RecyclerView] adapter for displaying a list of comments for an article.
 */
class CommentsRecyclerAdapter : PagedListAdapter<Comment,CommentsRecyclerAdapter.ViewHolder>(COMMENT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.item_comment,parent,false)
        return ViewHolder(root,root.text,root.author,root.date)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) with(holder) {
            mText.movementMethod = LinkMovementMethod.getInstance()
            mText.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(item.text, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(item.text)
            mAuthor.text = item.author
            mDate.text = item.date
        }
    }

    inner class ViewHolder(val mRoot: View, val mText : TextView, val mAuthor: Chip, val mDate : TextView) : RecyclerView.ViewHolder(mRoot)
    
    companion object {
        private val COMMENT_COMPARATOR = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem == newItem
        }
    }

}