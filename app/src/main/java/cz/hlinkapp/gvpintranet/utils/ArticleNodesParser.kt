package cz.hlinkapp.gvpintranet.utils

import cz.hlinkapp.gvpintranet.contracts.ServerContract
import cz.hlinkapp.gvpintranet.model.article_nodes.ArticleNode
import cz.hlinkapp.gvpintranet.model.article_nodes.ImageNode
import cz.hlinkapp.gvpintranet.model.article_nodes.TextNode
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node

class ArticleNodesParser {

    private val resultList = ArrayList<ArticleNode>()
    private val textNodesToAddList = ArrayList<Node>()

    /**
     * Parses the given html text to a list of [TextNode]s and [ImageNode]s to be displayed in [cz.hlinkapp.gvpintranet.adapters.ArticleNodesRecyclerAdapter] .
     */
    fun parseText (text: String?) : ArrayList<ArticleNode> {
        resultList.clear()
        textNodesToAddList.clear()
        val doc = Jsoup.parseBodyFragment(text)
        doc.setBaseUri(ServerContract.BASE_URL)
        val nodes = doc.body().childNodesCopy()
        parse(nodes)
        writeTextPartsToResult()
        return resultList
    }

    //TODO: fix bugs and add docs

    private fun parse(nodes : List<Node>) {
        for (node in nodes) if (node.childNodesCopy().isEmpty()) {
            if (node is Element && node.`is`("img")) {
                writeTextPartsToResult()
                resultList.add(ImageNode(node.absUrl("src")))
            } else textNodesToAddList.add(node)
        } else parse(node.childNodesCopy())
    }

    private fun writeTextPartsToResult() {
        val textDoc = Document(ServerContract.BASE_URL)
        textDoc.insertChildren(0,textNodesToAddList)
        resultList.add(TextNode(textDoc.toString()))
        textNodesToAddList.clear()
    }
}