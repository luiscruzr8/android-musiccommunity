package com.tfm.musiccommunityapp.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.ui.extensions.toPx

class HorizontalTagsLayout : FlexboxLayout {

    private val flexboxLayout: FlexboxLayout = FlexboxLayout(context).apply {
        val flexboxLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        flexWrap = FlexWrap.WRAP
        layoutParams = flexboxLayoutParams
    }

    private var tagList = mutableListOf<String>()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizontalTagsLayout)
        typedArray.recycle()
        notifyDataChange()
    }


    init {
        addView(flexboxLayout)
        isHorizontalScrollBarEnabled = false
        notifyDataChange()
    }


    fun setTagList(values: List<String>) {
        tagList.apply {
            clear()
            addAll(values)
        }
        notifyDataChange()
    }


    private fun notifyDataChange() {
        flexboxLayout.removeAllViews()
        val textLayoutParams = LayoutParams(
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        ).apply {
            setMargins(0, 8.toPx, 18.toPx, 0)
        }

        tagList.forEach { value ->
            flexboxLayout.addView(
                AppCompatTextView(context).apply {
                    background = ContextCompat.getDrawable(context, R.drawable.tag_chip)
                    gravity = Gravity.CENTER
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                    setTextColor(ContextCompat.getColor(context, R.color.primaryColor))
                    setPadding(6.toPx, 0, 6.toPx, 0)
                    layoutParams = textLayoutParams
                    text = value
                })
        }
    }
}