package com.sisindia.ai.android.features.sales

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentSalesReferenceBinding
import com.sisindia.ai.android.models.sales.SalesReferenceMO
import org.threeten.bp.LocalDate

/**
 * Created by Ashu Rajput on 4/30/2021.
 */
class SalesReferenceFragment : IopsBaseFragment() {

    private lateinit var viewModel: SalesReferenceViewModel
    private lateinit var binding: FragmentSalesReferenceBinding

    companion object {
        fun newInstance(): SalesReferenceFragment = SalesReferenceFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_sales_reference
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(SalesReferenceViewModel::class.java) as SalesReferenceViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this,
            { message: Message ->
                when (message.what) {
                    NavigationConstants.ON_ADD_SALES_REFERENCE -> openAddSalesReferenceBottomSheet()
                    NavigationConstants.ON_UPDATE_SALES_REFERENCE -> {
                        openUpdateSalesReferenceBottomSheet(message.obj as SalesReferenceMO)
                    }
                    NavigationConstants.ON_REFRESHING_SALES_REFERENCE -> getCurrentMonthSalesRefFromAPI()
                }
            })
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container, false)
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onCreated() {
//        getCurrentMonthSalesRefFromAPI()
    }

    private fun getCurrentMonthSalesRefFromAPI() {
        viewModel.getSalesReferenceFromAPI(LocalDate.now().monthValue)
    }

    private fun openAddSalesReferenceBottomSheet() {
        if (requireActivity().supportFragmentManager.findFragmentByTag(
                AddUpdateSalesReferenceBottomSheet::class.java.simpleName) == null) {
            AddUpdateSalesReferenceBottomSheet.newInstance(true)
                .show(requireActivity().supportFragmentManager,
                    AddUpdateSalesReferenceBottomSheet::class.java.simpleName)
        }
    }

    private fun openUpdateSalesReferenceBottomSheet(salesMO: SalesReferenceMO) {
        if (requireActivity().supportFragmentManager.findFragmentByTag(
                AddUpdateSalesReferenceBottomSheet::class.java.simpleName) == null) {
            AddUpdateSalesReferenceBottomSheet.newInstanceWithData(false, salesMO)
                .show(requireActivity().supportFragmentManager,
                    AddUpdateSalesReferenceBottomSheet::class.java.simpleName)
        }
    }
}