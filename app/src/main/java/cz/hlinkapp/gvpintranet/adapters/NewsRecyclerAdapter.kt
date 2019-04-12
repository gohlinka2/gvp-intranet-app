package cz.hlinkapp.gvpintranet.adapters

import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.model.PieceOfNews
import kotlinx.android.synthetic.main.item_piece_of_news.view.*

/**
 * A [RecyclerView] adapter for displaying a list of news/events.
 */
class NewsRecyclerAdapter() : RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>() {

    private var mNews = ArrayList<PieceOfNews>()

    var news : ArrayList<PieceOfNews>
        get() {
            val list = ArrayList<PieceOfNews>()
            list.addAll(mNews)
            return list
        }
        set(value) {
            mNews.clear()
            mNews.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.item_piece_of_news,parent,false)
        return ViewHolder(root,root.title,root.description)
    }

    override fun getItemCount(): Int = mNews.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with (holder) {
            mRoot.layoutParams = (mRoot.layoutParams as RecyclerView.LayoutParams).apply { bottomMargin = if (position == itemCount - 1) mRoot.resources.getDimensionPixelSize(R.dimen.dimen_8dp) else 0 }
            mTitle.movementMethod = LinkMovementMethod.getInstance()
            mDescription.movementMethod = LinkMovementMethod.getInstance()
            mTitle.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(mNews[position].title, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(mNews[position].title)
            mDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(mNews[position].description, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(mNews[position].description)

        }
    }

    inner class ViewHolder(val mRoot: View, val mTitle : TextView, val mDescription: TextView) : RecyclerView.ViewHolder(mRoot)

}