package com.sisindia.ai.android.features.dynamictask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.SpinnerAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowDateTimePickerBinding
import com.sisindia.ai.android.databinding.RowDynamicAudioBinding
import com.sisindia.ai.android.databinding.RowDynamicCheckboxBinding
import com.sisindia.ai.android.databinding.RowDynamicEditTextBinding
import com.sisindia.ai.android.databinding.RowDynamicLabelBinding
import com.sisindia.ai.android.databinding.RowDynamicPictureBinding
import com.sisindia.ai.android.databinding.RowDynamicRatingBinding
import com.sisindia.ai.android.databinding.RowDynamicScanBinding
import com.sisindia.ai.android.databinding.RowDynamicSeparatorBinding
import com.sisindia.ai.android.databinding.RowDynamicSpinnerBinding
import com.sisindia.ai.android.features.dynamictask.models.DynamicAudioMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicCheckBoxMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicDateTimePickerMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicEditTextMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicLabel
import com.sisindia.ai.android.features.dynamictask.models.DynamicPictureMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicRatingMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicScanQrMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicSeparatorMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicSpinnerMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicStaticSpinnerMO
/*import kotlinx.android.synthetic.main.row_dynamic_audio.view.tvAudioClip
import kotlinx.android.synthetic.main.row_dynamic_checkbox.view.dynamicCheckBox
import kotlinx.android.synthetic.main.row_dynamic_picture.view.dynamicPictureLayout
import kotlinx.android.synthetic.main.row_dynamic_scan.view.tvScanQrCode
import kotlinx.android.synthetic.main.row_dynamic_spinner.view.dynamicSpinner*/

/**
 * Created by Ashu_Rajput on 6/5/2021.
 */
class DynamicTaskAdapterV2 : BaseRecyclerAdapter<Any>() {

    private lateinit var taskListener: DynamicTasksListener

    companion object {
        private const val SINGLE_IMAGE: Int = 1
        private const val SEPARATOR_VIEW: Int = 2
        private const val LABEL_VIEW: Int = 3
        private const val SPINNER_VIEW: Int = 4
        private const val PICTURE_VIEW: Int = 6
        private const val CHECKBOX_VIEW: Int = 7
        private const val AUDIO_VIEW: Int = 8
        private const val SCAN_VIEW: Int = 9
        private const val EDIT_TEXT_VIEW: Int = 10
        private const val DATE_TIME_VIEW: Int = 11
        private const val STATIC_SPINNER_VIEW: Int = 12
        private const val RATING_VIEW: Int = 13
    }

    fun initListener(taskListener: DynamicTasksListener) {
        this.taskListener = taskListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {

            LABEL_VIEW -> {
                val view: RowDynamicLabelBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.row_dynamic_label, parent, false)
                LabelViewHolder(view)
            }

            EDIT_TEXT_VIEW -> {
                val view: RowDynamicEditTextBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.row_dynamic_edit_text, parent, false)
                EditTextVH(view)
            }

            CHECKBOX_VIEW -> {
                val view: RowDynamicCheckboxBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.row_dynamic_checkbox, parent, false)
                CheckBoxVH(view)
            }

            PICTURE_VIEW -> {
                val view: RowDynamicPictureBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.row_dynamic_picture, parent, false)
                UploadPictureVH(view)
            }

            AUDIO_VIEW -> {
                val view: RowDynamicAudioBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.row_dynamic_audio, parent, false)
                AddAudioClipVH(view)
            }

            SCAN_VIEW -> {
                val view: RowDynamicScanBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.row_dynamic_scan, parent, false)
                ScanQRVH(view)
            }

            SEPARATOR_VIEW -> {
                val view: RowDynamicSeparatorBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.row_dynamic_separator, parent, false)
                SeparatorVH(view)
            }

            SPINNER_VIEW -> {
                val view: RowDynamicSpinnerBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.row_dynamic_spinner, parent, false)
                SpinnerVH(view)
            }

            STATIC_SPINNER_VIEW -> {
                val view: RowDynamicSpinnerBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.row_dynamic_spinner, parent, false)
                StaticSpinnerVH(view)
            }

            RATING_VIEW -> {
                val view: RowDynamicRatingBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.row_dynamic_rating, parent, false)
                RatingVH(view)
            }

            DATE_TIME_VIEW -> {
                val view: RowDateTimePickerBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.row_date_time_picker, parent, false)
                DateTimeVH(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            items[position] is DynamicLabel -> LABEL_VIEW
            items[position] is DynamicEditTextMO -> EDIT_TEXT_VIEW
            items[position] is DynamicSeparatorMO -> SEPARATOR_VIEW
            items[position] is DynamicSpinnerMO -> SPINNER_VIEW
            items[position] is DynamicPictureMO -> PICTURE_VIEW
            items[position] is DynamicCheckBoxMO -> CHECKBOX_VIEW
            items[position] is DynamicAudioMO -> AUDIO_VIEW
            items[position] is DynamicScanQrMO -> SCAN_VIEW
            items[position] is DynamicStaticSpinnerMO -> STATIC_SPINNER_VIEW
            items[position] is DynamicRatingMO -> RATING_VIEW
            items[position] is DynamicDateTimePickerMO -> DATE_TIME_VIEW
            else -> SEPARATOR_VIEW
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
//            is SingleImageViewHolder -> holder.onBind(getItem(position))
            is LabelViewHolder -> holder.onBind(getItem(position))
            is SeparatorVH -> holder.onBind(getItem(position))
            is EditTextVH -> holder.onBind(getItem(position))
            is SpinnerVH -> holder.onBind(getItem(position))
            is StaticSpinnerVH -> holder.onBind(getItem(position))
            is CheckBoxVH -> holder.onBind(getItem(position))
            is UploadPictureVH -> holder.onBind(getItem(position))
            is AddAudioClipVH -> holder.onBind(getItem(position))
            is ScanQRVH -> holder.onBind(getItem(position))
            is RatingVH -> holder.onBind(getItem(position))
            is DateTimeVH -> holder.onBind(getItem(position))
//            is EmployeeSelectionVH -> holder.onBind(getItem(position))
//            is SiteSelectionVH -> holder.onBind(getItem(position))
        }
    }

    inner class SeparatorVH(separatorView: RowDynamicSeparatorBinding) :
        BaseViewHolder<Any>(separatorView) {
        override fun onBind(item: Any?) {

        }
    }

    inner class LabelViewHolder(private val labelView: RowDynamicLabelBinding) :
        BaseViewHolder<Any>(labelView) {
        override fun onBind(item: Any?) {
            labelView.model = item as DynamicLabel
        }
    }

    inner class EditTextVH(private val labelView: RowDynamicEditTextBinding) :
        BaseViewHolder<Any>(labelView) {
        override fun onBind(item: Any?) {
            labelView.model = item as DynamicEditTextMO
        }
    }

    inner class SpinnerVH(private val spinnerView: RowDynamicSpinnerBinding) :
        BaseViewHolder<Any>(spinnerView) {
        override fun onBind(item: Any?) {
            val spinnerModel = item as DynamicSpinnerMO
            spinnerView.model = spinnerModel
            val listAdapter: SpinnerAdapter = ArrayAdapter(spinnerView.root.context,
                R.layout.support_simple_spinner_dropdown_item,
                spinnerModel.spinnerList)

            spinnerView.dynamicSpinner.adapter = listAdapter
            spinnerView.dynamicSpinner.onItemSelectedListener = listener
        }

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                taskListener.onSpinnerSelected(layoutPosition, parent?.getItemAtPosition(pos).toString())
            }
        }
    }

    inner class StaticSpinnerVH(private val spinnerView: RowDynamicSpinnerBinding) :
        BaseViewHolder<Any>(spinnerView) {
        override fun onBind(item: Any?) {
            val spinnerModel = item as DynamicStaticSpinnerMO
            spinnerView.staticModel = spinnerModel
            val listAdapter: SpinnerAdapter = ArrayAdapter(spinnerView.root.context,
                R.layout.support_simple_spinner_dropdown_item,
                spinnerModel.spinnerList!!)

            spinnerView.dynamicSpinner.adapter = listAdapter
            spinnerView.dynamicSpinner.onItemSelectedListener = listener
            spinnerView.dynamicSpinner.setSelection(item.selectedSpinnerPosition)
        }

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                if (pos > 0) {
                    taskListener.onSpinnerSelected(layoutPosition,
                        parent?.getItemAtPosition(pos).toString())
                    (items[layoutPosition] as DynamicStaticSpinnerMO).selectedSpinnerPosition = pos
                }
            }
        }
    }

    inner class CheckBoxVH(private val cbView: RowDynamicCheckboxBinding) : BaseViewHolder<Any>(cbView) {
        override fun onBind(item: Any?) {
            cbView.model = item as DynamicCheckBoxMO
            cbView.dynamicCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                taskListener.onCheckBoxClicked(layoutPosition, isChecked)
            }
        }
    }

    inner class UploadPictureVH(private val pictureView: RowDynamicPictureBinding) :
        BaseViewHolder<Any>(pictureView) {
        override fun onBind(item: Any?) {
            pictureView.model = item as DynamicPictureMO
            pictureView.dynamicPictureLayout.setOnClickListener {
                taskListener.onClickPicture(layoutPosition)
            }
        }
    }

    inner class AddAudioClipVH(private val audioView: RowDynamicAudioBinding) :
        BaseViewHolder<Any>(audioView) {
        override fun onBind(item: Any?) {
            audioView.tvAudioClip.setOnClickListener {
                taskListener.onAddingAudio(layoutPosition)
            }
        }
    }

    inner class ScanQRVH(private val scannerView: RowDynamicScanBinding) :
        BaseViewHolder<Any>(scannerView) {
        override fun onBind(item: Any?) {
            scannerView.tvScanQrCode.setOnClickListener {
                taskListener.onScanningQR(layoutPosition)
            }
        }
    }

    inner class RatingVH(private val ratingView: RowDynamicRatingBinding) :
        BaseViewHolder<Any>(ratingView) {
        override fun onBind(item: Any?) {
            ratingView.model = item as DynamicRatingMO
            ratingView.ratingStars.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                    if (rating > 0.4) (items[layoutPosition] as DynamicRatingMO).rating = rating.toInt()
                }
        }
    }

    inner class DateTimeVH(private val dateTimeView: RowDateTimePickerBinding) :
        BaseViewHolder<Any>(dateTimeView) {
        override fun onBind(item: Any?) {
            dateTimeView.model = item as DynamicDateTimePickerMO
            dateTimeView.dynamicDateTimePicker.setOnClickListener {
                taskListener.onClickDateTimePicker(layoutPosition)
            }
        }
    }
}