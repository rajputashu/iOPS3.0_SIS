package com.sisindia.ai.android.features.onboard

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.droidcommons.preference.Prefs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.PrefConstants.MODULE_LOGIN
import com.sisindia.ai.android.databinding.ActivityIntroBinding
import com.sisindia.ai.android.uimodels.intro.BoardingIntroMO
import java.io.IOException

/**
 * Created by Ashu Rajput on 1/31/2020.
 */
class IntroActivity : IopsBaseActivity() {

    lateinit var binding: ActivityIntroBinding

    override fun getLayoutResource(): Int {
        return R.layout.activity_intro
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.introSkipButton -> finishIntroActivity()
            R.id.introDoneButton -> {
                Prefs.putBoolean(moduleName, true)
                finishIntroActivity()
            }
            R.id.introNextButton -> {
                binding.introSliderViewPager.currentItem = binding.introSliderViewPager.currentItem + 1
            }
        }
    }

    private fun finishIntroActivity() {
        finish()
    }

    lateinit var moduleName: String
    override fun onCreated() {
        val introJsonData = getJsonDataFromAsset()
        if (!introJsonData.isNullOrEmpty()) {
            moduleName = intent.extras!!.getString(IntentConstants.MODULE_NAME)!!
            val listIntroType = object : TypeToken<BoardingIntroMO>() {}.type
            val boardingIntroMO: BoardingIntroMO = Gson().fromJson(introJsonData, listIntroType)
            val moduleInfoList =
                when (moduleName) {
                    MODULE_LOGIN -> boardingIntroMO.loginList!!
                    else -> arrayListOf()
                }

            binding.introSliderViewPager.adapter = IntroPagerAdapter(this, moduleInfoList)
            binding.introDotsIndicator.setViewPager(binding.introSliderViewPager)
            binding.introSliderViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    if (position == boardingIntroMO.loginList!!.size - 1) {
                        binding.introDoneButton.visibility = View.VISIBLE
                        binding.introNextButton.visibility = View.GONE
                    } else {
                        binding.introDoneButton.visibility = View.GONE
                        binding.introNextButton.visibility = View.VISIBLE
                    }
                }
            })
        }
       binding.introSkipButton.setOnClickListener(onClickListener)
       binding.introNextButton.setOnClickListener(onClickListener)
       binding.introDoneButton.setOnClickListener(onClickListener)
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.executePendingBindings()
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
    }

    override fun initViewModel() {
    }

    private fun Context.getJsonDataFromAsset(): String? {
        val jsonString: String
        try {
            jsonString = assets.open("OnBoardingIntro.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    override fun onBackPressed() {

    }
}