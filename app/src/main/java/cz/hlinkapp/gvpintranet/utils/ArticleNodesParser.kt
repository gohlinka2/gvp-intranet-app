package cz.hlinkapp.gvpintranet.utils

import cz.hlinkapp.gvpintranet.contracts.ServerContract
import cz.hlinkapp.gvpintranet.model.article_nodes.ArticleNode
import cz.hlinkapp.gvpintranet.model.article_nodes.ImageNode
import cz.hlinkapp.gvpintranet.model.article_nodes.TextNode
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node

/**
 * A utility class for parsing html text into an array of [ArticleNode]s.
 * Explanation: Because embedded images are loaded into a different view than the text, the html document needs to be split into
 * a list of pieces of text and image source URIs. This list then gets loaded and displayed in a RecyclerView, and the adapter will choose the correct view for each [ArticleNode].
 * This class handles the creation of the list.
 */
class ArticleNodesParser {

    private val resultList = ArrayList<ArticleNode>()
    private val document = Document(ServerContract.BASE_URL)

    /**
     * Parses the given html text to a list of [TextNode]s and [ImageNode]s to be displayed in [cz.hlinkapp.gvpintranet.adapters.ArticleNodesRecyclerAdapter] .
     */
    fun parseText (text: String?) : ArrayList<ArticleNode> {
        resultList.clear()
        document.empty()
        val doc = Jsoup.parseBodyFragment(text)
        doc.setBaseUri(ServerContract.BASE_URL)
        parse(doc.body(),document) //parse the document
        writeTextPartsToResult() //write remaining text parts from working document to result
        return resultList
    }

    //TODO: DOESN'T FUCKING WORK, FIX IT
    //FUCK RECURSION I HATE IT SO MUCH

    /**
     * Parses the given Node and all of its children recursively into a list of [ArticleNode]s.
     */
    private fun parse(nodeToParse : Node, parent: Element) {
        val shallowNodeClone = nodeToParse.shallowClone()
        parent.appendChild(shallowNodeClone) //appends this node with no children to the parent
        if (shallowNodeClone is Element) for (childNode in nodeToParse.childNodesCopy()) { //for every child, decide what to do:
            if (childNode.childNodeSize() == 0) { //if the child does not have children:
                if (childNode is Element && childNode.`is`("img")) { //if the child is an image, add text from working document to the result and add the image
                    writeTextPartsToResult()
                    resultList.add(ImageNode(childNode.absUrl("src"))) //get the image's absolute url and add it to the result list
                } else shallowNodeClone.appendChild(childNode) //if the child is not an image, append it to the parent shallow copy
            } else parse(childNode,shallowNodeClone) //the node has children, parse every child recursively
        }
    }

    /**
     * Writes text parts from the working document to the result list. Call before an image is added and after parsing.
     */
    private fun writeTextPartsToResult() {
        resultList.add(TextNode(document.toString()))
        document.empty() //clear the working document
    }
}