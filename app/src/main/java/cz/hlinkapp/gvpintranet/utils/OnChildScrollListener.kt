package cz.hlinkapp.gvpintranet.utils

/***
 * An interface Activities can implement to know about child Fragments scroll events.
 * This can be useful for example for showing and hiding FloatingActionButtons on scroll events.
 * Child Fragments should cast their parent Activity to an instance of this interface and call the corresponding event functions.
 */
interface OnChildScrollListener {

    /**
     * Call when the main scrolling view of the Fragment is being scrolled up.
     */
    fun onScrollUp()

    /**
     * Call when the main scrolling view of the Fragment is being scrolled down.
     */
    fun onScrollDown()
}