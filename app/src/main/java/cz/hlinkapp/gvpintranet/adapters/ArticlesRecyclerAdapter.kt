package cz.hlinkapp.gvpintranet.adapters

import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.google.android.material.chip.Chip
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.config.setGone
import cz.hlinkapp.gvpintranet.config.setVisible
import cz.hlinkapp.gvpintranet.contracts.ServerContract
import cz.hlinkapp.gvpintranet.model.Article
import kotlinx.android.synthetic.main.item_article.view.*
import me.zhanghai.android.materialprogressbar.IndeterminateCircularProgressDrawable
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * A [RecyclerView] adapter for displaying a paged list of articles.
 */
class ArticlesRecyclerAdapter() : PagedListAdapter<Article,ArticlesRecyclerAdapter.ViewHolder>(ARTICLE_COMPARATOR) {

    constructor(onItemClickListener: ((article: Article) -> Unit)?) : this() {
        this.onItemClickListener = onItemClickListener
    }

    var onItemClickListener: ((article: Article) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.item_article,parent,false)
        return ViewHolder(root,root.imageView,root.title,root.description, root.author, root.date)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) with(holder) {

            //parse description text
            val descDoc = Jsoup.parseBodyFragment(item.description)
            descDoc.setBaseUri(ServerContract.BASE_URL)

            val imgToDisplay: Element? = descDoc.body().selectFirst("img")
            val src = imgToDisplay?.absUrl("src") //only the first image should be displayed, if it exists
            //remove all img elements
            var elToRemove : Element? = null
            do {
                elToRemove?.remove()
                elToRemove = descDoc.body().selectFirst("img")
            } while (elToRemove != null)

            mRoot.touchTarget.setOnClickListener { onItemClickListener?.invoke(item)  }
            mRoot.layoutParams = (mRoot.layoutParams as RecyclerView.LayoutParams).apply { bottomMargin = if (position == itemCount - 1) mRoot.resources.getDimensionPixelSize(R.dimen.dimen_8dp) else 0 }
            mTitle.movementMethod = LinkMovementMethod.getInstance()
            mDescription.movementMethod = LinkMovementMethod.getInstance()
            mTitle.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(item.title, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(item.title)
            mDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(descDoc.toString(), Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(descDoc.toString())
            mAuthor.text = item.author
            mDate.text = item.date
            if (src != null) {
                mImage.setVisible()
                Glide.with(mImage)
                    .load(src)
                    .placeholder(IndeterminateCircularProgressDrawable(mImage.context))
                    .error(R.drawable.ic_broken_image)
                    .downsample(DownsampleStrategy.FIT_CENTER)
                    .into(mImage)
            } else mImage.setGone()
        }
    }

    inner class ViewHolder(val mRoot: View, val mImage: ImageView, val mTitle : TextView, val mDescription: TextView, val mAuthor: Chip, val mDate : Chip) : RecyclerView.ViewHolder(mRoot)
    
    companion object {
        private val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem
        }
    }

}