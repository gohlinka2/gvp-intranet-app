package cz.hlinkapp.gvpintranet.adapters

import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.config.setGone
import cz.hlinkapp.gvpintranet.config.setVisible
import cz.hlinkapp.gvpintranet.model.article_nodes.ArticleNode
import cz.hlinkapp.gvpintranet.model.article_nodes.ImageNode
import cz.hlinkapp.gvpintranet.model.article_nodes.TextNode
import kotlinx.android.synthetic.main.item_node.view.*
import me.zhanghai.android.materialprogressbar.IndeterminateCircularProgressDrawable


/**
 * A [RecyclerView] adapter for displaying parts of a text and images. Used to display images embedded into text.
 * The [ArticleNode] class and its descendants are used as a model.
 */
class ArticleNodesRecyclerAdapter() : RecyclerView.Adapter<ArticleNodesRecyclerAdapter.ViewHolder>() {

    private var mNodes = ArrayList<ArticleNode>()
    var textSize = 16f

    var nodes : ArrayList<ArticleNode>
        get() {
            val list = ArrayList<ArticleNode>()
            list.addAll(mNodes)
            return list
        }
        set(value) {
            mNodes.clear()
            mNodes.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.item_node,parent,false)
        return ViewHolder(root,root.textView,root.imageView)
    }

    override fun getItemCount(): Int = mNodes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with (holder) {
            val node = mNodes[position]
            if (node is ImageNode) {
                mText.setGone()
                mImage.setVisible()
                Glide.with(mImage)
                    .load(node.imageSrc)
                    .placeholder(IndeterminateCircularProgressDrawable(mImage.context))
                    .error(R.drawable.ic_broken_image)
                    .into(mImage)
            } else if (node is TextNode) {
                mText.setVisible()
                mImage.setGone()
                mText.textSize = textSize
                mText.movementMethod = LinkMovementMethod.getInstance()
                mText.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(node.text, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(node.text)
            }
            mText.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    fun refresh() {
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mRoot: View, val mText : TextView, val mImage: ImageView) : RecyclerView.ViewHolder(mRoot)

}