package com.example.flavorfinder.view.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.flavorfinder.R

class CommentEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): AppCompatEditText(context, attrs), View.OnTouchListener {

    private var postButton: Drawable
    private var postButtonClickListener: (() -> Unit)? = null

    init {
        postButton = ContextCompat.getDrawable(context, R.drawable.baseline_check_24) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) showPostButton() else hidePostButton()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun hidePostButton() {
        setButtonDrawables()
    }

    private fun showPostButton() {
        setButtonDrawables(endOfTheText = postButton)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val postButtonStart: Float
            val postButtonEnd: Float
            var isPostButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                postButtonEnd = (postButton.intrinsicWidth + paddingStart).toFloat()
                if (event.x < postButtonEnd) isPostButtonClicked = true
            } else {
                postButtonStart = (width - paddingEnd - postButton.intrinsicWidth).toFloat()
                if (event.x > postButtonStart) isPostButtonClicked = true
            }

            if (isPostButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.d("CommentEditText", "Post button pressed")
                        postButton = ContextCompat.getDrawable(context, R.drawable.baseline_check_24) as Drawable
                        showPostButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.d("CommentEditText", "Post button released")
                        postButton = ContextCompat.getDrawable(context, R.drawable.baseline_check_24) as Drawable
                        if (text != null) {
                            postButtonClickListener?.invoke()
                            text?.clear()
                        }
                        hidePostButton()
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }

    fun setPostButtonClickListener(listener: () -> Unit) {
        postButtonClickListener = listener
    }

}