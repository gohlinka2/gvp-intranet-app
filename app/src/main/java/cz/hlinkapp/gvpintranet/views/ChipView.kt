package cz.hlinkapp.gvpintranet.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import cz.hlinkapp.gvpintranet.R
import kotlinx.android.synthetic.main.compound_view_chip_view.view.*

/**
 * A View representing a chip for displaying an attribute or other small piece of data. The chip can be cancelable, which
 * will allow the user to cancel and, for example, the chip can be removed from a list afterwards.
 * ChipView also has two color modes, check the [setColorMode] documentation for more.
 */
//TODO: remove class and all dependencies
@Deprecated("Use material components implementation instead.")
class ChipView(context: Context?, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {


    private val mView = View.inflate(context, R.layout.compound_view_chip_view,this)
    private val mTextView = mView.textView
    private val mCancelImageView = mView.cancel
    private var mOnCancelClickListener : (() -> Unit)? = null
    private var mCancelable : Boolean
    private var mText : String?
    private var mColorMode : Int
    private var mIconDrawable : Drawable?

    init {
        val typedArray = context!!.obtainStyledAttributes(attrs,R.styleable.ChipView)
        mCancelable = typedArray.getBoolean(R.styleable.ChipView_cancelable,false)
        mText = typedArray.getString(R.styleable.ChipView_chipText)
        mColorMode = typedArray.getInt(R.styleable.ChipView_colorMode, COLOR_MODE_WHITE_BACKGROUND)
        mIconDrawable = typedArray.getDrawable(R.styleable.ChipView_chipIcon)
        typedArray.recycle()
        setPadding(resources!!.getDimensionPixelSize(R.dimen.ChipView_padding_left_right),
                resources!!.getDimensionPixelSize(R.dimen.ChipView_padding_top_bottom),
                resources!!.getDimensionPixelSize(R.dimen.ChipView_padding_left_right),
                resources!!.getDimensionPixelSize(R.dimen.ChipView_padding_top_bottom))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setCancelable(mCancelable)
        setText(mText)
        setColorMode(mColorMode)
        setIconDrawable(mIconDrawable)
        mCancelImageView.setOnClickListener{mOnCancelClickListener?.invoke()}
    }

    /**
     * Sets the drawable for the left icon of this chip. Pass null to remove the icon.
     */
    fun setIconDrawable(drawable: Drawable?) {
        mIconDrawable = drawable
        mView.icon.setImageDrawable(mIconDrawable)
        mView.icon.visibility = if (drawable == null) View.GONE else View.VISIBLE
    }

    /**
     * Sets whether to display the cancel/close button. If true, however, you still need to handle the close behaviour yourself using a listener set with #setOnCancelClickListener.
     */
    fun setCancelable(cancelable : Boolean) {
        mCancelable = cancelable
        if (mCancelable) mCancelImageView.visibility = View.VISIBLE else mCancelImageView.visibility = View.GONE
    }

    /**
     * Sets the color mode of this chip. Can be either #COLOR_MODE_WHITE_BACKGROUND for a chip that will be displayed on a white background or
     * #COLOR_MODE_COLOR_PRIMARY_BACKGROUND for a chip that will be displayed on a primary color background.
     * @param colorMode The requested color mode, use the constants.
     * @throws IllegalArgumentException if something else than one of the constants is passed as the argument.
     */
    fun setColorMode(colorMode: Int) {
        mColorMode = colorMode
        when (mColorMode) {
            COLOR_MODE_WHITE_BACKGROUND -> {
                mCancelImageView.setImageResource(R.drawable.ic_cancel_bg_white)
                mView.icon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.colorTextSecondaryOnWhite))
                mTextView.setTextColor(ContextCompat.getColor(context,R.color.colorTextSecondaryOnWhite))
                mView.setBackgroundResource(R.drawable.bg_attribute_chip_bg_white)
            }
            COLOR_MODE_COLOR_PRIMARY_BACKGROUND -> {
                mCancelImageView.setImageResource(R.drawable.ic_cancel_bg_primary)
                mView.icon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.colorWhite))
                mTextView.setTextColor(ContextCompat.getColor(context,R.color.colorWhite))
                mView.setBackgroundResource(R.drawable.bg_attribute_chip_bg_primary)
            }
            else -> throw IllegalArgumentException("Use only the color mode constants!")
        }
    }

    /**
     * Returns the current color mode constant of this chip.
     */
    fun getColorMode(): Int {
        return mColorMode
    }

    /**
     * Sets the listener that will be called when the close/cancel button of this chip is pressed.
     */
    fun setOnCancelClickListener(listener: () -> Unit) {
        mOnCancelClickListener = listener
    }

    /**
     * Sets the text of this chip.
     */
    fun setText(text: String?) {
        mText = text
        mTextView.text = text
    }

    /**
     * Returns the text of this chip.
     */
    fun getText() : String = mTextView.text.toString()

    companion object {
        const val COLOR_MODE_WHITE_BACKGROUND = 0
        const val COLOR_MODE_COLOR_PRIMARY_BACKGROUND = 1
    }

}