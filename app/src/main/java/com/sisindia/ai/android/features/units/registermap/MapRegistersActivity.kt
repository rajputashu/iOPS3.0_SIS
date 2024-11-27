package com.sisindia.ai.android.features.units.registermap

import android.app.Activity
import android.os.Message
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.NavigationConstants.ON_MAPPING_REGISTERS_SUCCESSFULLY
import com.sisindia.ai.android.databinding.ActivityMapRegistersBinding

/**
 * Created by Ashu Rajput on 10/21/2020.
 */
class MapRegistersActivity : IopsBaseActivity() {

    private lateinit var viewModel: MapRegistersViewModel
    private lateinit var binding: ActivityMapRegistersBinding

    override fun getLayoutResource(): Int {
        return R.layout.activity_map_registers
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(MapRegistersViewModel::class.java) as MapRegistersViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun extractBundle() {
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbAddPost)
        viewModel.getRegistersToMapFromServer()
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    ON_MAPPING_REGISTERS_SUCCESSFULLY -> {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            })
    }
}