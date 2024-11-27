package com.sisindia.ai.android.features.onboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.sisindia.ai.android.R
import com.sisindia.ai.android.uimodels.intro.ModuleInfoMO
//import kotlinx.android.synthetic.main.row_intro_pager_adapter.view.*

/**
 * Created by Ashu Rajput on 2/1/2020.
 */
class IntroPagerAdapter(val context: Context, private val list: List<ModuleInfoMO>) : PagerAdapter() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = layoutInflater.inflate(R.layout.row_intro_pager_adapter, container, false)
        try {
            context.apply {

                /*view.introModuleLabel.text = list[position].header
                view.introAboutModuleMsg.text = list[position].message
                val resId = resources.getIdentifier(list[position].imageName, "drawable", applicationContext.packageName)
                Glide.with(this).load(resId).into(view.introDisplayPic)*/

                val introModuleLabel: AppCompatTextView = view.findViewById(R.id.introModuleLabel)
                val introAboutModuleMsg: AppCompatTextView = view.findViewById(R.id.introAboutModuleMsg)
                val introDisplayPic: AppCompatImageView = view.findViewById(R.id.introDisplayPic)

                // Update the views with data from the list
                introModuleLabel.text = list[position].header
                introAboutModuleMsg.text = list[position].message

                val resId = resources.getIdentifier(list[position].imageName, "drawable", applicationContext.packageName)
                Glide.with(this).load(resId).into(introDisplayPic)

                container.addView(view)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}