package com.honegroupp.familyRegister.view.item

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.honegroupp.familyRegister.R

public class SliderAdapter(context: Context) : PagerAdapter() {
    var context = context
    // Arrays
    val slide_images = arrayOf(R.mipmap.eat_icon, R.mipmap.eat_icon, R.mipmap.sleep_icon)

    val slide_headings = arrayOf("EAT", "SLEEP", "CODE")

    val slide_descs = arrayOf(
        "Lkcjash, sajhashcj sajdch sakjch, askjch, aschk, ascjhkjhkjhkhcskah csajk cajksh  akjchoeufq feoif asc,c asjhkjhascjkh askjhckash" + "aliqua",
        "Lkcjash, sajhashcj sajdch sakjch, askjch, aschk, ascjhkjhkjhkhcskah csajk cajksh  akjchoeufq feoif asc,c asjhkjhascjkh askjhckash" + "aliqua",
        "Lkcjash, sajhashcj sajdch sakjch, askjch, aschk, ascjhkjhkjhkhcskah csajk cajksh  akjchoeufq feoif asc,c asjhkjhascjkh askjhckash" + "aliqua"
    )
    override fun getCount(): Int {
        return slide_headings.size
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        var layoutInflater:LayoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.slide_layout, container, false)

        var slideImageView = view.findViewById<ImageView>(R.id.slide_image)
        var slideHeaing = view.findViewById<TextView>(R.id.slide_heading)
        var slideDescription = view.findViewById<TextView>(R.id.slide_desc)

        slideImageView.setImageResource(slide_images[position])
        slideHeaing.setText(slide_headings[position])
        slideDescription.setText(slide_descs[position])

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}