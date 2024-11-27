package com.sisindia.ai.android.commons

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
import com.sisindia.ai.android.databinding.FragmentDialogYesNoBinding
import com.sisindia.ai.android.features.uar.dialog.DialogListener

/**
 * Created by Ashu Rajput on 4/24/2020.
 */
class YesNoDialogFragment : DialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance(message: String): YesNoDialogFragment {
            val dialogFragment = YesNoDialogFragment()
            Bundle().apply {
                putString(IntentConstants.DIALOG_MESSAGE, message)
                dialogFragment.arguments = this
            }
            return dialogFragment
        }

        @JvmStatic
        fun newSingleButtonInstance(message: String): YesNoDialogFragment {
            val dialogFragment = YesNoDialogFragment()
            Bundle().apply {
                putString(IntentConstants.DIALOG_MESSAGE, message)
                putBoolean(IntentConstants.IS_SINGLE_BUTTON, true)
                dialogFragment.arguments = this
            }
            return dialogFragment
        }
    }

    private lateinit var listener: DialogListener

    fun initDialogListener(listener: DialogListener) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, vg: ViewGroup?, b: Bundle?): View? {
        val binding: FragmentDialogYesNoBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_dialog_yes_no, vg, false)

        binding.message = arguments?.getString(IntentConstants.DIALOG_MESSAGE, "")
        val isSingleButton = arguments?.getBoolean(IntentConstants.IS_SINGLE_BUTTON, false)

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (isSingleButton!!) {
            binding.dialogNoButton.visibility = View.GONE
            binding.dialogYesButton.text = resources.getString(R.string.string_ok)
        }

        binding.dialogYesButton.setOnClickListener {
            if (::listener.isInitialized)
                listener.onYesButtonClicked()
            dismiss()
        }
        binding.dialogNoButton.setOnClickListener {
            if (::listener.isInitialized)
                listener.onNoButtonClicked()
            dismiss()
        }
        return binding.root
    }
}