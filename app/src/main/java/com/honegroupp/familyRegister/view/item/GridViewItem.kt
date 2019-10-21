package com.honegroupp.familyRegister.view.item

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

//This class is modified from code on stackoverflow, convert java code to kotlin
//https://stackoverflow.com/questions/24416847/how-to-force-gridview-to-generate-square-cells-in-android
class GridViewItem : ImageView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(
        context,
        attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // This is the key that will make the height equivalent to its width
        super.onMeasure(
            widthMeasureSpec,
            widthMeasureSpec
        )
    }
}