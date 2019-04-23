package cz.hlinkapp.gvpintranet.utils

import cz.hlinkapp.gvpintranet.contracts.ServerContract
import cz.hlinkapp.gvpintranet.model.article_nodes.ArticleNode
import cz.hlinkapp.gvpintranet.model.article_nodes.ImageNode
import cz.hlinkapp.gvpintranet.model.article_nodes.TextNode
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.select.NodeFilter
import org.jsoup.select.NodeVisitor
import java.util.*
import kotlin.collections.ArrayList

/**
 * A utility class for parsing html text into an array of [ArticleNode]s.
 * Explanation: Because embedded images are loaded into a different view than the text, the html document needs to be split into
 * a list of pieces of text and image source URIs. This list then gets loaded and displayed in a RecyclerView, and the adapter will choose the correct view for each [ArticleNode].
 * This class handles the creation of the list.
 */
class ArticleNodesParser {

    /**
     * The document with the input html. During parsing, elements already parsed will get removed from the document.
     */
    private lateinit var inputDocument : Document
    /**
     * The working document. Parsed elements from the [inputDocument] will get copied to this document.
     * When an image is discovered or the end of [inputDocument] has been reached, this document will get emptied and a [TextNode] with its contents will be added to [resultList].
     */
    private lateinit var workingDocument : Document
    /**
     * The result ArrayList with the parsed text and image nodes.
     */
    private lateinit var resultList : ArrayList<ArticleNode>

    /**
     * Parses the given html text to a list of [TextNode]s and [ImageNode]s to be displayed in [cz.hlinkapp.gvpintranet.adapters.ArticleNodesRecyclerAdapter] .
     */
    fun parseText (text: String?) : ArrayList<ArticleNode> {
        resultList = ArrayList()
        inputDocument = Jsoup.parseBodyFragment(text,ServerContract.BASE_URL)
        workingDocument = Document(ServerContract.BASE_URL)
        generateIDs()
        parse()
        return resultList
    }

    /**
     * Generates a unique ID for each element in the [inputDocument].
     * This method MUST be called before calling [parse].
     */
    private fun generateIDs() {
        inputDocument.traverse(object : NodeVisitor {
            override fun tail(node: Node, depth: Int) {}
            override fun head(node: Node, depth: Int) {
                node.attr(ATTR_PARSE_ID,UUID.randomUUID().toString())
            }
        })
    }

    /**
     * Parses the [inputDocument] into an ArrayList of [ArticleNode]s, written to [resultList].
     * The method [generateIDs] MUST be called after initialising the [inputDocument] and before calling this method.
     * The [workingDocument] is used as a temporary document needed for the parsing.
     */
    private fun parse() {
        //if the inputDocument is not empty, parse it, else write the parsed parsed from the workingDocument to the resultList
        if (inputDocument.childNodeSize() != 0) {
            //traverse the document tree and decide what to do for each node
            inputDocument.filter(object : NodeFilter {
                override fun tail(node: Node, depth: Int): NodeFilter.FilterResult {
                    return NodeFilter.FilterResult.REMOVE //remove the ending when it's been already parsed
                }

                override fun head(node: Node, depth: Int): NodeFilter.FilterResult {
                    return if (node is Element && node.`is`("img")) { //the element is an image
                        writeTextPartsToResult()
                        resultList.add(ImageNode(node.absUrl("src"))) //get the image's absolute url and add it to the result list
                        node.remove() //remove the image
                        NodeFilter.FilterResult.STOP //stop this traversal
                    } else { //the element is not an image
                        val parentId = node.parent()?.attr(ATTR_PARSE_ID) //the id of this node's parent
                        val workingNodeParent = //the copy of this node's parent in the workingDocument, or null if not present
                            if (parentId != null && parentId.isNotEmpty()) workingDocument.getElementsByAttributeValue(ATTR_PARSE_ID,parentId).first()
                            else null
                        //if the copy of this node's parent is present in the workingDocument, add a shallow copy of this node to it, else add it to the root of the workingDocument
                        if (workingNodeParent != null) workingNodeParent.appendChild(node.shallowClone())
                        else workingDocument.appendChild(node.shallowClone())
                        NodeFilter.FilterResult.CONTINUE //continue the traversal
                    }
                }
            })
            parse() //parse again
        } else writeTextPartsToResult()
    }

    /**
     * Writes text parts from the working document to the result list. Call before an image is added and after parsing.
     */
    private fun writeTextPartsToResult() {
        resultList.add(TextNode(workingDocument.toString()))
        workingDocument.empty() //clear the working document
    }

    companion object {
        const val ATTR_PARSE_ID = "parse-ID"
    }

}