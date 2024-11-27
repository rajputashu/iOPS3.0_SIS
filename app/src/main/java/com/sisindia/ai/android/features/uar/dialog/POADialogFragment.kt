package com.sisindia.ai.android.features.uar.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.sisindia.ai.android.R
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.databinding.FragmentDialogPoaBinding
import com.sisindia.ai.android.uimodels.uarpoa.IPPoaPendingCompletedMO
import com.sisindia.ai.android.uimodels.uarpoa.POADetailsMO

/**
 * Created by Ashu Rajput on 4/4/2020.
 */
class POADialogFragment : DialogFragment() {

    companion object {
        fun newInstance(poaMO: POADetailsMO): POADialogFragment {
            val bundle = Bundle()
            bundle.putParcelable(IntentConstants.CLOSED_POA_DATA, poaMO)
            val fragment = POADialogFragment()
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(poaMO: IPPoaPendingCompletedMO): POADialogFragment {
            val bundle = Bundle()
            bundle.putParcelable(IntentConstants.CLOSED_IP_POA, poaMO)
            val fragment = POADialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var listener: DialogListener

    fun initDialogListener(listener: DialogListener) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, vg: ViewGroup?, b: Bundle?): View? {
        val binding: FragmentDialogPoaBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_dialog_poa, vg, false)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        arguments?.apply {
            if (this.containsKey(IntentConstants.CLOSED_POA_DATA)) {
                binding.poaMO = requireArguments().getParcelable(IntentConstants.CLOSED_POA_DATA)
                binding.ipPoaMO = null
            } else if (this.containsKey(IntentConstants.CLOSED_IP_POA)) {
                binding.poaMO = null
                binding.ipPoaMO = requireArguments().getParcelable(IntentConstants.CLOSED_IP_POA)
            }
        }
        binding.dialogCloseButton.setOnClickListener {
            listener.onCrossButtonClick()
            dismiss()
        }
        binding.dialogViewAllButton.setOnClickListener {
            listener.onViewAllPOAClick()
            dismiss()
        }
        return binding.root
    }
}