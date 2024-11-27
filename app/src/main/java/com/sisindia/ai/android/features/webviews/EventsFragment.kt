package com.sisindia.ai.android.features.webviews

import android.content.Intent
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_WEB_VIEW
import com.sisindia.ai.android.databinding.FragmentEventsBinding
import com.sisindia.ai.android.features.notification.CustomWebPage
import com.sisindia.ai.android.models.EventsDataMO

/**
 * Created by Ashu Rajput on 12-10-2022
 */
class EventsFragment : IopsBaseFragment() {

    private lateinit var viewModel: EventsViewModel
    private lateinit var binding: FragmentEventsBinding

    companion object {
        fun newInstance(): EventsFragment = EventsFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_events
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(EventsViewModel::class.java) as EventsViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                OPEN_WEB_VIEW -> {
                    val eventMO = message.obj as EventsDataMO
                    startActivity(Intent(activity, CustomWebPage::class.java)
                        .putExtra("NOTIFICATION_TITLE", eventMO.eventName)
                        .putExtra("NOTIFICATION_URL", eventMO.mobileUrl + eventMO.mobileToken))
                }
            }
        }
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container, false)
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onCreated() {
        viewModel.getEventsFromAPI()
    }
}