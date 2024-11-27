package com.sisindia.ai.android.features.taskcheck.clienthandshake.frags

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentClientHandshakeBinding
import com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet.AddClientsBottomSheet
import com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet.NotMetReasonBottomSheet
import com.sisindia.ai.android.room.entities.CustomerContactEntity
//import kotlinx.android.synthetic.main.fragment_client_handshake.*

/**
 * Created by Ashu Rajput on 4/8/2020.
 */
class ClientHandshakeFragment : IopsBaseFragment() {

    private lateinit var viewModel: HandshakeFragViewModel
    private lateinit var binding: FragmentClientHandshakeBinding

    companion object {
        fun newInstance(): ClientHandshakeFragment = ClientHandshakeFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_client_handshake
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(HandshakeFragViewModel::class.java)
                as HandshakeFragViewModel
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container, false)
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.OPEN_BOTTOM_SHEET -> {
                    openNotMetReasonsBottomSheet()
                }
                NavigationConstants.OPEN_ADD_CLIENT_BOTTOM_SHEET -> {
                    openAddClientsBottomSheet()
                }
                NavigationConstants.ON_SELECT_NOT_MET_REASON -> {
                    viewModel.reasonNotMetClient.set(message.obj as String)
                    viewModel.selectedClient.set(CustomerContactEntity(-1))
                    viewModel.showEditButton.set(View.VISIBLE)
                }
                NavigationConstants.ON_CLIENT_ADDED_SUCCESSFULLY -> {
                    Toast.makeText(requireActivity(),
                        "Client added successfully",
                        Toast.LENGTH_LONG)
                        .show()
                    viewModel.fetchClientDetails()
                }
            }
        }
    }

    override fun onCreated() {
        viewModel.apply {
            tabLists = listOf(binding.yesButtonTV, binding.noButtonTV)
            isClientHandshakeTaskAlreadyStarted()
            fetchClientDetails()
        }
    }

    private fun openNotMetReasonsBottomSheet() {
        if (requireActivity().supportFragmentManager.findFragmentByTag(NotMetReasonBottomSheet::class.java.simpleName) == null) {
            NotMetReasonBottomSheet.newInstance().show(requireActivity().supportFragmentManager,
                NotMetReasonBottomSheet::class.java.simpleName)
        }
    }

    private fun openAddClientsBottomSheet() {
        if (requireActivity().supportFragmentManager.findFragmentByTag(AddClientsBottomSheet::class.java.simpleName) == null) {
            val addClientSheet = AddClientsBottomSheet.newInstance()
            addClientSheet.show(requireActivity().supportFragmentManager,
                AddClientsBottomSheet::class.java.simpleName)
            addClientSheet.isCancelable = false
        }
    }
}