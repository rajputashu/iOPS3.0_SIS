package com.sisindia.ai.android.features.units.details.posts

import android.app.Activity
import android.content.Intent
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.commons.YesNoDialogFragment
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentUnitPostsBinding
import com.sisindia.ai.android.features.units.addedit.AddEditPostActivity

/**
 * Created by Ashu Rajput on 3/24/2020.
 */
class UnitPostsFragment : IopsBaseFragment() {

    private lateinit var viewModel: UnitPostsViewModel

    companion object {
        fun newInstance() = UnitPostsFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_unit_posts
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(UnitPostsViewModel::class.java) as UnitPostsViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this, { message: Message ->
            when (message.what) {
                NavigationConstants.OPEN_ADD_EDIT_POSTS -> {
                    startActivityForResult(AddEditPostActivity.newIntent(requireActivity(),
                        message.arg1), IntentRequestCodes.REQUEST_ADD_EDIT_POST)
                }
                NavigationConstants.ON_DELETING_POST -> {
                    openConfirmationDialog()
                }
            }
        })
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        val binding: FragmentUnitPostsBinding = DataBindingUtil.inflate(inflater!!,
            layoutResource, container, false)
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onCreated() {
        viewModel.getUnitPostsDetails()
    }

    private fun openConfirmationDialog() {
        val dialog =
            YesNoDialogFragment.newInstance(resources.getString(R.string.string_delete_post_msg))
        dialog.show(requireActivity().supportFragmentManager,
            YesNoDialogFragment::class.java.simpleName)
        dialog.initDialogListener(viewModel.dialogListener)
        dialog.isCancelable = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IntentRequestCodes.REQUEST_ADD_EDIT_POST && resultCode == Activity.RESULT_OK) {
            Toast.makeText(activity, "Post Added/Updated Successfully", Toast.LENGTH_LONG).show()
            viewModel.getUnitPostsDetails()
        }
    }
}