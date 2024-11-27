package com.sisindia.ai.android.features.issues

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sisindia.ai.android.R
import com.sisindia.ai.android.features.addgrievances.ActionPlanSpinnerAdapter
import com.sisindia.ai.android.features.issues.complaints.ComplaintItemListener
import com.sisindia.ai.android.features.issues.complaints.ComplaintRecyclerAdapter
import com.sisindia.ai.android.features.issues.complaints.SingleSelectionClientListAdapter
import com.sisindia.ai.android.features.issues.grievance.GuardSuggestionViewListeners
import com.sisindia.ai.android.features.issues.grievance.SiteListSpinnerAdapter
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.GuardsSuggestionAdapter
import com.sisindia.ai.android.room.entities.SiteEntity
import com.sisindia.ai.android.uimodels.ActionPlanModel
import com.sisindia.ai.android.uimodels.ComplaintSection
import com.sisindia.ai.android.uimodels.GrievanceCategory
import com.sisindia.ai.android.uimodels.GuardSuggestionItem
import timber.log.Timber

object IssueManagementViewBindings {

    @JvmStatic
    @BindingAdapter(value = ["sitesList", "siteListListener"])
    fun AppCompatSpinner.setSiteSpinner(list: List<SiteEntity>,
        listeners: SiteListListeners) {
        if (!list.isNullOrEmpty()) {
            val spinnerAdapter = SiteListSpinnerAdapter(context, R.layout.adapter_item_site, list)
            adapter = spinnerAdapter

            onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>,
                    view: View,
                    i: Int,
                    l: Long) {
                    val item = adapter.getItem(i) as SiteEntity
                    listeners.onSiteSelected(item)
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["allGuards", "setAllGuardViewListener"])
    fun AppCompatAutoCompleteTextView.setCreateGrievanceGuards(list: List<GuardSuggestionItem?>,
        viewListeners: GuardSuggestionViewListeners) {
        if (list.isNotEmpty()) {
            val suggestionAdapter = GuardsSuggestionAdapter(context, list, viewListeners)
            setAdapter(suggestionAdapter)
            onItemClickListener =
                OnItemClickListener { parent: AdapterView<*>?, view: View, position: Int, id: Long ->
                    val im =
                        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    im.hideSoftInputFromWindow(view.applicationWindowToken, 0)
                    viewListeners.onGuardSelected(suggestionAdapter.getItem(position)!!)
                }
        } /*else {
            //In case List of guard is empty : Triggering for internet fetch guard
            val suggestionAdapter = GuardsSuggestionAdapter(context, list, viewListeners)
            setAdapter(suggestionAdapter)
        }*/
    }

    @JvmStatic
    @BindingAdapter(value = ["setGrievanceCategoryChips"])
    fun ChipGroup.setGrievanceCategoryChips(categories: List<GrievanceCategory>) {
        if (categories != null && categories.isNotEmpty()) {
            visibility = View.VISIBLE
            for (category in categories) {
                val chip = LayoutInflater.from(context)
                    .inflate(R.layout.layout_chip_item, null, false) as Chip
                chip.text = category.displayName
                chip.tag = category
                addView(chip)
            }
        } else {
            visibility = View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setActionPlans", "actionPlanListener"])
    fun AppCompatSpinner.setActionPlans(list: List<ActionPlanModel?>,
        listeners: ActionPlanListener) {
        if (list != null && list.isNotEmpty()) {
            val spinnerAdapter =
                ActionPlanSpinnerAdapter(context, R.layout.adapter_action_plan_item, list)
            adapter = spinnerAdapter
            onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long) {
                    val item = adapter.getItem(i) as ActionPlanModel
                    listeners.onActionPlanSelected(item)
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                    Timber.e("")
                }
            }
        }
    }


    //setComplaintModes
    @JvmStatic
    @BindingAdapter("setComplaintModes")
    fun ChipGroup.setComplaintModes(modes: List<ComplaintSection>?) {
        removeAllViews()
        if (modes != null && modes.isNotEmpty()) {
            visibility = View.VISIBLE
            for (category in modes) {
                val chip = LayoutInflater.from(context)
                    .inflate(R.layout.layout_chip_item, null, false) as Chip
                chip.text = category.displayName
                chip.tag = category
                addView(chip)
            }
        } else {
            visibility = View.GONE
        }
    }

    //setComplaintModes
    @JvmStatic
    @BindingAdapter("setComplaintTypes")
    fun ChipGroup.setComplaintTypes(types: List<ComplaintSection>?) {
        removeAllViews()
        if (types != null && types.isNotEmpty()) {
            visibility = View.VISIBLE
            for (category in types) {
                val chip = LayoutInflater.from(context)
                    .inflate(R.layout.layout_chip_item, null, false) as Chip
                chip.text = category.displayName
                chip.tag = category
                addView(chip)
            }
        } else {
            visibility = View.GONE
        }
    }

    //setComplaintModes
    @JvmStatic
    @BindingAdapter("setComplaintNatures")
    fun ChipGroup.setComplaintNatures(natures: List<ComplaintSection>?) {
        removeAllViews()
        if (natures != null && natures.isNotEmpty()) {
            visibility = View.VISIBLE
            for (category in natures) {
                val chip = LayoutInflater.from(context)
                    .inflate(R.layout.layout_chip_item, null, false) as Chip
                chip.text = category.displayName
                chip.tag = category
                addView(chip)
            }
        } else {
            visibility = View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setClientAdapter", "clientSelectionListener"])
    fun RecyclerView.setClientAdapter(recyclerAdapter: SingleSelectionClientListAdapter,
        viewListeners: ClientSelectionListener) {
        val manager = LinearLayoutManager(context)
        layoutManager = manager
        recyclerAdapter.setListener(viewListeners)
        val dividerItemDecoration =
            DividerItemDecoration(context, manager.orientation)
        val item = ContextCompat.getDrawable(context, R.drawable.grey_divider_1sdp)
        if (item != null) {
            dividerItemDecoration.setDrawable(item)
            addItemDecoration(dividerItemDecoration)
        }
        adapter = recyclerAdapter
    }


    @JvmStatic
    @BindingAdapter(value = ["setComplaintRecyclerAdapter", "setComplaintItemListener"],
        requireAll = false)
    fun RecyclerView.setComplaintRecyclerAdapter(adapter: ComplaintRecyclerAdapter,
        listeners: ComplaintItemListener?) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        setLayoutManager(layoutManager)
        adapter.setItemListener(listeners)
        setAdapter(adapter)
    }

    @JvmStatic
    @BindingAdapter(value = ["setIssueCardColorLow"])
    fun CardView.setIssueCardColorLow(count: Int) {
        setCardBackgroundColor(ContextCompat.getColor(context,
            if (count == 0) R.color.textColorWhite_100 else R.color.textColorRed_less_48))
    }

    @JvmStatic
    @BindingAdapter(value = ["setIssueCardColorMedium"])
    fun CardView.setIssueCardColorMedium(count: Int) {
        setCardBackgroundColor(ContextCompat.getColor(context,
            if (count == 0) R.color.textColorWhite_100 else R.color.textColorRed_more_48))
    }

    @JvmStatic
    @BindingAdapter(value = ["setIssueCardColorHigh"])
    fun CardView.setIssueCardColorHigh(count: Int) {
        setCardBackgroundColor(ContextCompat.getColor(context,
            if (count == 0) R.color.textColorWhite_100 else R.color.textColorRed_more_7))
    }

    @JvmStatic
    @BindingAdapter(value = ["setIssueTitle"])
    fun AppCompatTextView.setIssueTitle(count: Int) {
        text = count.toString()
        setTextColor(ContextCompat.getColor(context,
            if (count == 0) R.color.titleTextColor_100 else R.color.textColorWhite_100))
    }

    @JvmStatic
    @BindingAdapter(value = ["setIssueSubTitle"])
    fun AppCompatTextView.setIssueSubTitle(count: Int) {
        setTextColor(ContextCompat.getColor(context,
            if (count == 0) R.color.titleTextColor_100 else R.color.textColorWhite_90))
    }

}