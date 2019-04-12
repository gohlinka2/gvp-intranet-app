package cz.hlinkapp.gvpintranet.model.article_nodes

/**
 * A model class representing an image embedded in a text. [imageSrc] is the source of the image.
 */
data class ImageNode( var imageSrc: String) : ArticleNode()