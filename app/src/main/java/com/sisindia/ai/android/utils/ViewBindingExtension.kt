package com.sisindia.ai.android.utils

//import com.sisindia.ai.android.features.chatbot.ChatBotAdapter
//import com.sisindia.ai.android.features.chatbot.ChatBotListener
//import com.sisindia.ai.android.features.chatbot.ChildQuestionsAdapter
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.droidcommons.views.roundedimageview.RoundedImageView
import com.sisindia.ai.android.R
import com.sisindia.ai.android.commons.ChatWidgetEnum
import com.sisindia.ai.android.commons.CollectionMode
import com.sisindia.ai.android.commons.SpinnersListener
import com.sisindia.ai.android.features.akr.AKRListener
import com.sisindia.ai.android.features.akr.adapters.AKRAdapter
import com.sisindia.ai.android.features.akr.adapters.KitAssignedDistributedAdapter
import com.sisindia.ai.android.features.akr.adapters.KitItemsAdapter
import com.sisindia.ai.android.features.barracks.listing.BarrackListener
import com.sisindia.ai.android.features.barracks.listing.BarrackListingAdapter
import com.sisindia.ai.android.features.billcollection.BillCollectionListener
import com.sisindia.ai.android.features.billcollection.adapter.BillCollectionAdapter
import com.sisindia.ai.android.features.billcollection.adapter.CollectionsCardAdapter
import com.sisindia.ai.android.features.billsubmit.BillCollectionRadioCheckedListener
import com.sisindia.ai.android.features.billsubmit.BillSubmissionListener
import com.sisindia.ai.android.features.billsubmit.RadioCheckedListener
import com.sisindia.ai.android.features.billsubmit.adapter.BillSubmissionCardsAdapter
import com.sisindia.ai.android.features.billsubmit.sheet.BillCheckListAdapter
import com.sisindia.ai.android.features.clientcoordination.ClientCoordinationListener
import com.sisindia.ai.android.features.clientcoordination.adapters.CCRPerformanceRatingAdapter
import com.sisindia.ai.android.features.conveyance.ConveyanceListener
import com.sisindia.ai.android.features.conveyance.MonthlyConveyanceAdapter
import com.sisindia.ai.android.features.dynamictask.DynamicTaskAdapter
import com.sisindia.ai.android.features.dynamictask.DynamicTaskAdapterV2
import com.sisindia.ai.android.features.dynamictask.DynamicTasksListener
import com.sisindia.ai.android.features.improvementplans.IPPoaDetailsAdapter
import com.sisindia.ai.android.features.improvementplans.SitesWithPoaAdapter
import com.sisindia.ai.android.features.kpi.MyKPIAdapter
import com.sisindia.ai.android.features.mask.MaskAdapter
import com.sisindia.ai.android.features.moninput.MonInputListener
import com.sisindia.ai.android.features.moninput.adapter.MonInputCardAdapter
import com.sisindia.ai.android.features.nudges.NudgesDashboardAdapter
import com.sisindia.ai.android.features.nudges.NudgesListener
import com.sisindia.ai.android.features.performance.MonthYearListener
import com.sisindia.ai.android.features.performance.SortItemSelectionListener
import com.sisindia.ai.android.features.performance.adapters.IncentiveAdapter
import com.sisindia.ai.android.features.performance.adapters.SiteTaskSummaryAdapter
import com.sisindia.ai.android.features.practo.GuardCheckedAdapter
import com.sisindia.ai.android.features.recruitment.adapter.RecommendedRecruitAdapter
import com.sisindia.ai.android.features.recruitment.adapter.RecruitmentGraphAdapter
import com.sisindia.ai.android.features.rotacompliance.adapters.RotaComplianceAdapter
import com.sisindia.ai.android.features.sales.SalesListener
import com.sisindia.ai.android.features.sales.SalesReferenceAdapter
import com.sisindia.ai.android.features.sync.ManualSyncAdapter
import com.sisindia.ai.android.features.taskcheck.clienthandshake.ClientHandshakeListener
import com.sisindia.ai.android.features.taskcheck.clienthandshake.adapters.FeedbackQuestionAdapter
import com.sisindia.ai.android.features.taskcheck.clienthandshake.adapters.HandshakeClientsAdapter
import com.sisindia.ai.android.features.taskcheck.postcheck.PostCheckViewListeners
import com.sisindia.ai.android.features.taskcheck.strengthcheck.StrengthCheckAdapter
import com.sisindia.ai.android.features.uar.UARAdapter
import com.sisindia.ai.android.features.uar.UARListener
import com.sisindia.ai.android.features.uar.poa.POAAdapter
import com.sisindia.ai.android.features.units.UnitListener
import com.sisindia.ai.android.features.units.UnitsAdapter
import com.sisindia.ai.android.features.units.addedit.EquipmentAdapter
import com.sisindia.ai.android.features.units.addedit.EquipmentSpinnerListener
import com.sisindia.ai.android.features.units.details.general.ClientContactAdapter
import com.sisindia.ai.android.features.units.details.posts.PostsListener
import com.sisindia.ai.android.features.units.details.posts.UnitPostsAdapter
import com.sisindia.ai.android.features.units.details.strength.UnitStrengthAdapter
import com.sisindia.ai.android.features.units.registermap.MapRegistersAdapter
import com.sisindia.ai.android.features.webviews.EventClickListener
import com.sisindia.ai.android.features.webviews.EventsAdapter
import org.threeten.bp.LocalDate
import timber.log.Timber
import java.text.DecimalFormat
import kotlin.math.abs

/**
 * Created by Ashu Rajput on 3/25/2020.
 */
@BindingAdapter(value = ["collectionDetailAdapter"])
fun bindBillCollectionAdapter(recyclerView: RecyclerView, adapter: BillCollectionAdapter) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.adapter = adapter
}

@BindingAdapter(value = ["ccrClientItems", "ccrListener"])
fun AppCompatSpinner.bindCCRSpinner(contactList: ArrayList<String>,
                                    listener: ClientCoordinationListener) {
    if (contactList.isNotEmpty()) {
        contactList.apply {
            val ccrAdapter: ArrayAdapter<String> =
                ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, contactList)
            adapter = ccrAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                    listener.onContactSelected(parent!!.selectedItem.toString())
                }
            }
        }
    }
}

@BindingAdapter(value = ["performanceRatingAdapter"])
fun bindCCRRatingAdapter(recyclerView: RecyclerView, adapter: CCRPerformanceRatingAdapter) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.adapter = adapter
}

//Slider->UNIT
@BindingAdapter(value = ["unitsAdapter", "unitListener"])
fun bindUnitsAdapter(recyclerView: RecyclerView, adapter: UnitsAdapter, listener: UnitListener) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.adapter = adapter
}

//Units->General Tab
@BindingAdapter(value = ["clientContactAdapter"])
fun bindGeneralAdapter(recyclerView: RecyclerView, adapter: ClientContactAdapter) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, RecyclerView.HORIZONTAL, false)
    val pagerSnapHelper = PagerSnapHelper()
    pagerSnapHelper.attachToRecyclerView(recyclerView)
    recyclerView.adapter = adapter
}

//Units->Strength Tab
@BindingAdapter(value = ["strengthAdapter"])
fun bindStrengthAdapter(recyclerView: RecyclerView, adapter: UnitStrengthAdapter) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.adapter = adapter
}

//Units->POSTS Tab
@BindingAdapter(value = ["unitPostsAdapter", "postListener"])
fun RecyclerView.bindPostsAdapter(unitPostsAdapter: UnitPostsAdapter, listener: PostsListener) {
    layoutManager = LinearLayoutManager(context)
    unitPostsAdapter.initListener(listener)
    adapter = unitPostsAdapter
}

//Units->PostsTab->Equipment
@BindingAdapter(value = ["equipmentAdapter"])
fun bindEquipmentsAdapter(recyclerView: RecyclerView, adapter: EquipmentAdapter) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.adapter = adapter
}

@BindingAdapter(value = ["providedBy", "equipmentListener"])
fun bindEquipmentProvidedBySpinner(spinner: AppCompatSpinner,
                                   providedList: ArrayList<String>,
                                   listener: EquipmentSpinnerListener) {
    if (!providedList.isNullOrEmpty()) {
        providedList.apply {
            val adapter: SpinnerAdapter =
                ArrayAdapter(spinner.context, R.layout.support_simple_spinner_dropdown_item, providedList)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                    listener.onProvidedBySelected(parent!!.selectedItem.toString())
                }
            }
        }
    }
}

@BindingAdapter(value = ["equipmentType", "equipmentListener"])
fun bindEquipmentTypeSpinner(spinner: AppCompatSpinner,
                             equipmentTypeList: ArrayList<String>,
                             listener: EquipmentSpinnerListener) {
    if (!equipmentTypeList.isNullOrEmpty()) {
        equipmentTypeList.apply {
            val adapter: SpinnerAdapter = ArrayAdapter(spinner.context,
                R.layout.support_simple_spinner_dropdown_item,
                equipmentTypeList)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                    listener.onEquipmentTypeSelected(parent!!.selectedItem.toString())
                }
            }
        }
    }
}

@BindingAdapter(value = ["rewardFineReasons", "listener"])
fun AppCompatSpinner.bindFindRewardReasonSpinner(reasonList: ArrayList<String>,
                                                 listener: SpinnersListener) {
    if (!reasonList.isNullOrEmpty()) {
        reasonList.apply {
            val reasonAdapter: SpinnerAdapter =
                ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, reasonList)
            adapter = reasonAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                    listener.onSpinnerOptionSelected(pos)
                }
            }
        }
    }
}

/*@BindingAdapter(value = ["aiLocationMap"])
fun SupportMapFragment.bindGoogleMap(latLng: LatLng) {
//fun MapView.bindGoogleMap(latLng: LatLng) {
    apply {
        onCreate(Bundle())
        getMapAsync { map ->
            map.addMarker(MarkerOptions().position(latLng).title("Marker in India"))
        }
    }
}*/

@BindingAdapter(value = ["billCheckListAdapter"])
fun bindBillCheckListAdapter(recyclerView: RecyclerView, adapter: BillCheckListAdapter) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.adapter = adapter
}

@BindingAdapter(value = ["radioCheckedListener"])
fun RadioGroup.bindRadioGroupAdapter(listener: RadioCheckedListener) {
    setOnCheckedChangeListener { group, checkedId ->
        val radio: RadioButton = group.findViewById(checkedId)
        listener.onRadioButtonChecked(radio.text.toString())
    }
}

@BindingAdapter(value = ["radioSelectedListener"])
fun RadioGroup.bindRadioCheckedAdapter(listener: BillCollectionRadioCheckedListener) {
    setOnCheckedChangeListener { group, checkedId ->
        //        val radio: RadioButton = group.findViewById(checkedId)
        when (checkedId) {
            R.id.radioButtonCheque -> listener.onRadioButtonChecked(CollectionMode.CHEQUE)
            R.id.radioButtonRTGS -> listener.onRadioButtonChecked(CollectionMode.RTGS)
            R.id.radioButtonNEFT -> listener.onRadioButtonChecked(CollectionMode.NEFT)
            R.id.radioButtonUPI -> listener.onRadioButtonChecked(CollectionMode.UPI)
        }
    }
}

@BindingAdapter(value = ["collectionModeTypeLabel"])
fun AppCompatTextView.bindCollectionModeAdapter(mode: CollectionMode) {
    text = when (mode) {
        CollectionMode.CHEQUE -> resources.getString(R.string.string_cheque_number)
        CollectionMode.RTGS -> resources.getString(R.string.string_rtgs_details)
        CollectionMode.NEFT -> resources.getString(R.string.string_neft_details)
        CollectionMode.UPI -> resources.getString(R.string.string_upi_details)
    }
}

@BindingAdapter(value = ["statusCode", "date"])
fun AppCompatTextView.bindAssignDistributeUnpaid(statusCode: Int, date: String?) {
    text = when (statusCode) {
        1, 2 -> resources.getString(R.string.string_assigned_on, date)
        3, 4 -> resources.getString(R.string.string_distributed_on, date)
        else -> resources.getString(R.string.string_unpaid_on, date)
    }
}

@BindingAdapter(value = ["strengthCount", "isStrengthChecked"])
fun AppCompatTextView.strengthCheck(strengthCount: Int, isStrengthChecked: Boolean) {
    if (isStrengthChecked) {
        visibility = View.VISIBLE
        text = when {
            strengthCount == 0 -> resources.getString(R.string.string_strength_full)
            strengthCount > 0 -> resources.getString(R.string.string_shortage, strengthCount)
            else -> resources.getString(R.string.string_surplus,
                abs(strengthCount)) // If value coming -ve means Current Strength > Authorized Strength
        }
    } else visibility = View.INVISIBLE
}

//Slider->UAR - POA
@BindingAdapter(value = ["uarPoaAdapter", "uarPoaListener"])
fun bindUarPoaAdapter(recyclerView: RecyclerView, adapter: UARAdapter, listener: UARListener) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    adapter.setListener(listener)
    recyclerView.adapter = adapter
}

//IMPROVEMENT PLANS ADAPTER
@BindingAdapter(value = ["sitesWithPoaAdapter", "improvePlanListener"])
fun RecyclerView.bindImprovePlansAdapter(ipAdapter: SitesWithPoaAdapter, listener: UARListener) {
    layoutManager = LinearLayoutManager(context)
    ipAdapter.setListener(listener)
    adapter = ipAdapter
}

//Slider->UAR -> PENDING POA Selected
@BindingAdapter(value = ["pendingPOAAdapter", "poaListener"])
fun bindPendingPOAAdapter(recyclerView: RecyclerView, adapter: POAAdapter, listener: UARListener) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    adapter.initListener(listener)
    recyclerView.adapter = adapter
}

//Slider->UAR -> COMPLETED POA Selected
@BindingAdapter(value = ["completedPOAAdapter"])
fun bindCompletedPOAAdapter(recyclerView: RecyclerView, adapter: POAAdapter) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.adapter = adapter
}

@BindingAdapter(value = ["ipPendingPOAAdapter", "poaListener"])
fun RecyclerView.bindIPPendingPOAAdapter(ipPoaAdapter: IPPoaDetailsAdapter, listener: UARListener) {
    layoutManager = LinearLayoutManager(context)
    ipPoaAdapter.initListener(listener)
    adapter = ipPoaAdapter
}

@BindingAdapter(value = ["htmlText"])
fun TextView.bindStringToHtmlAdapter(@NonNull spannedValue: String?) {
    text = spannedValue!!.toHtmlSpan()
}

@BindingAdapter(value = ["htmlText"])
fun AppCompatTextView.bindStringToHtmlAdapter(@Nullable spannedValue: String?) {
    text = spannedValue?.toHtmlSpan() ?: spannedValue
}

@BindingAdapter(value = ["srcRoundImageFromUri"])
fun setRoundImageResourceFromUri(imageView: RoundedImageView?, uri: Uri?) {
    if (uri != null) {
        Glide.with(imageView!!).load(uri).into(imageView)
    }
}

@BindingAdapter(value = ["glideUrl"])
fun RoundedImageView.setRoundImageResourceFromUri(glideUrl: String?) {
    glideUrl?.let {
        Timber.e("coming here to load URL via glide")
        Glide.with(this.context).load(glideUrl).into(this)
    }
}

@BindingAdapter(value = ["intToStrWithPercent"])
fun bindIntegerToStringAdapter(textView: TextView, value: Int) {
    textView.text = textView.context.resources.getString(R.string.string_with_percentage, value)
}

//DC{Client Handshake}
@BindingAdapter(value = ["clientHandshakeAdapter", "listener"])
fun RecyclerView.bindHandshakeAdapter(handShakeAdapter: HandshakeClientsAdapter,
                                      listener: ClientHandshakeListener) {
    val manager = LinearLayoutManager(context)
    layoutManager = manager
    handShakeAdapter.initListener(listener)
    val dividerItemDecoration = DividerItemDecoration(context, manager.orientation)
    val item = ContextCompat.getDrawable(context, R.drawable.grey_divider_1sdp)

    if (item != null) {
        dividerItemDecoration.setDrawable(item)
        addItemDecoration(dividerItemDecoration)
    }

    adapter = handShakeAdapter
}

//Used for DC{Client Handshake - [FeedBack Fragment, Final Summary Fragment]}
@BindingAdapter(value = ["clientListener"])
fun bindHandshakeAdapter(button: AppCompatButton, listener: ClientHandshakeListener) {
}

@BindingAdapter(value = ["ratingAdapter"])
fun AppCompatRatingBar.bindRatingBarAdapter(listener: RatingBar.OnRatingBarChangeListener) {
    onRatingBarChangeListener = listener
}

@BindingAdapter(value = ["feedbackQuesAdapter"])
fun RecyclerView.bindFeedbackQuestionsAdapter(questionAdapter: FeedbackQuestionAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = questionAdapter
}

//DC,NC->STRENGTH CHECK
@BindingAdapter(value = ["strengthCheckAdapter"])
fun RecyclerView.bindStrengthCheckAdapter(strengthAdapter: StrengthCheckAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = strengthAdapter
}

//Annual Kit Replacement
@BindingAdapter(value = ["akrAdapter", "akrListener"])
fun RecyclerView.bindAKRAdapter(akrAdapter: AKRAdapter, listener: AKRListener) {
    layoutManager = LinearLayoutManager(context)
    akrAdapter.initListener(listener)
    adapter = akrAdapter
}

@BindingAdapter(value = ["taskAdapter", "taskListener"])
fun RecyclerView.bindPRACTOTaskAdapter(taskAdapter: GuardCheckedAdapter,
                                       listener: PostCheckViewListeners) {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    taskAdapter.initListener(listener)
    adapter = taskAdapter
}

//Annual Kit Replacement -> Details (Assigned/Distributed)
@BindingAdapter(value = ["akrDetailsAdapter", "akrDetailsListener"])
fun RecyclerView.bindAKRDetailsAdapter(kitAdapter: KitAssignedDistributedAdapter, listener: AKRListener) {
    layoutManager = LinearLayoutManager(context)
    kitAdapter.initListener(listener)
    adapter = kitAdapter
}

//Annual Kit Replacement -> Assigned->Kit To Replace
@BindingAdapter(value = ["kitItemsAdapter"])
fun RecyclerView.bindKitReplaceAdapter(kitItemsAdapter: KitItemsAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = kitItemsAdapter
}

@BindingAdapter(value = ["tasksSummaryAdapter"])
fun RecyclerView.bindSiteTaskSummary(tasksSummaryAdapter: SiteTaskSummaryAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = tasksSummaryAdapter
}

@BindingAdapter(value = ["incentiveAdapter"])
fun RecyclerView.bindIncentive(incentiveAdapter: IncentiveAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = incentiveAdapter
}

//Barrack List
@BindingAdapter(value = ["barrackAdapter", "listener"])
fun RecyclerView.bindBarrackListAdapter(barrackAdapter: BarrackListingAdapter,
                                        listener: BarrackListener) {
    layoutManager = LinearLayoutManager(context)
    barrackAdapter.initListener(listener)
    adapter = barrackAdapter
}

//Barrack List
@BindingAdapter(value = ["nudgesAdapter", "listener"])
fun RecyclerView.bindNudgesAdapter(nudgesAdapter: NudgesDashboardAdapter, listener: NudgesListener) {
    layoutManager = LinearLayoutManager(context)
    nudgesAdapter.initListener(listener)
    adapter = nudgesAdapter
}

@BindingAdapter(value = ["recruitmentAdapter"])
fun RecyclerView.bindRecruitmentGraphAdapter(recruitAdapter: RecruitmentGraphAdapter) {
    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    adapter = recruitAdapter
}

@BindingAdapter(value = ["addedRecruitmentAdapter"])
fun RecyclerView.bindAddedRecruitmentAdapter(recommendedAdapter: RecommendedRecruitAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = recommendedAdapter
}

@BindingAdapter(value = ["addedMaskAdapter"])
fun RecyclerView.bindAddedMasksAdapter(maskAdapter: MaskAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = maskAdapter
}

@BindingAdapter(value = ["dynamicTaskAdapter", "taskListener"])
fun RecyclerView.bindDynamicTask(dynamicTaskAdapter: DynamicTaskAdapter,
                                 taskListener: DynamicTasksListener) {
    layoutManager = LinearLayoutManager(context)
    adapter = dynamicTaskAdapter
    dynamicTaskAdapter.initListener(taskListener)
}

@BindingAdapter(value = ["dynamicNudgesAdapter", "taskListener"])
fun RecyclerView.bindDynamicNudges(dynamicTaskAdapter: DynamicTaskAdapterV2,
                                   taskListener: DynamicTasksListener) {
    layoutManager = LinearLayoutManager(context)
    adapter = dynamicTaskAdapter
    dynamicTaskAdapter.initListener(taskListener)
}

@BindingAdapter(value = ["salesRefAdapter", "listener"])
fun RecyclerView.bindSalesRefAdapter(salesAdapter: SalesReferenceAdapter, listener: SalesListener) {
    layoutManager = LinearLayoutManager(context)
    salesAdapter.initListener(listener)
    adapter = salesAdapter
}

@BindingAdapter(value = ["eventAdapter", "listener"])
fun RecyclerView.bindEventsAdapter(eventAdapter: EventsAdapter, listener: EventClickListener) {
    layoutManager = LinearLayoutManager(context)
    eventAdapter.initListener(listener)
    adapter = eventAdapter
}

@BindingAdapter(value = ["recruitStatus"])
fun AppCompatTextView.bindRecruitView(statusCode: Int) {
    when (statusCode) {
        1 -> {
            text = resources.getString(R.string.dynamic_recruitment_status, "Recommended")
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_orange, 0, 0, 0)
        }

        2 -> {
            text = resources.getString(R.string.dynamic_recruitment_status, "Selected")
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_green, 0, 0, 0)
        }

        3 -> {
            text = resources.getString(R.string.dynamic_recruitment_status, "Rejected")
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_red, 0, 0, 0)
        }
    }
}

@BindingAdapter(value = ["salesStatus", "isLabel"])
fun AppCompatTextView.bindSalesStatus(status: Int, isLabel: Boolean) {
    when (status) {
        1 -> {
            if (!isLabel) text = resources.getString(R.string.string_recommend)
            setTextColor(ContextCompat.getColor(context, R.color.barcode_reticle_background))
        }

        2 -> {
            if (!isLabel) text = resources.getString(R.string.string_raise)
            setTextColor(ContextCompat.getColor(context, R.color.colorLightOrange))
        }

        3 -> {
            if (!isLabel) text = resources.getString(R.string.string_approved)
            setTextColor(ContextCompat.getColor(context, R.color.colorDarkGreen))
        }
    }
}

/*@BindingAdapter(value = ["performanceResultsAdapter", "performanceListener"])
fun RecyclerView.bindPerformanceResultsAdapter(resultAdapter: PerformanceResultsAdapter,
    listener: PerformanceListener) {
    layoutManager = LinearLayoutManager(context)
    resultAdapter.initListener(listener)
    adapter = resultAdapter
}

@BindingAdapter(value = ["performanceEffortsAdapter"])
fun RecyclerView.bindPerformanceEffortsAdapter(resultAdapter: PerformanceEffortsAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = resultAdapter
}*/

//app:performanceSortItems="@{vm.sortItems}"
//            app:performanceSortListener="@{vm.sortItemSelectionListener}"
@BindingAdapter(value = ["performanceSortItems", "performanceSortListener"])
fun AppCompatSpinner.bindPerfEffortsSpinner(itemList: ArrayList<String>,
                                            listener: SortItemSelectionListener) {
    if (!itemList.isNullOrEmpty()) {
        itemList.apply {
            val effortsAdapter: SpinnerAdapter =
                ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, itemList)
            adapter = effortsAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                    val monthNo = if (pos in 0..4) LocalDate.now().monthValue else LocalDate.now()
                        .minusMonths(1).monthValue
                    //                    listener.onItemSelected(pos + 1)
                    listener.onItemSelected(monthNo)
                }
            }
        }
    }
}

@BindingAdapter(value = ["siteSortItems", "performanceSortListener"])
fun AppCompatSpinner.bindSiteSummarySpinner(itemList: ArrayList<String>,
                                            listener: SortItemSelectionListener) {
    if (!itemList.isNullOrEmpty()) {
        itemList.apply {
            val effortsAdapter: SpinnerAdapter =
                ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, itemList)
            adapter = effortsAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                    if (pos == 0) listener.onItemSelected(LocalDate.now().monthValue)
                    else listener.onItemSelected(LocalDate.now().minusMonths(1).monthValue)
                }
            }
        }
    }
}

@BindingAdapter(value = ["filterList", "monthYearListener"])
fun AppCompatSpinner.bindMonthYearSpinner(itemList: ArrayList<String>, listener: MonthYearListener) {
    if (!itemList.isNullOrEmpty()) {
        itemList.apply {
            val effortsAdapter: SpinnerAdapter =
                ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, itemList)
            adapter = effortsAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                    if (pos == 0) listener.onMonthYearSelected(LocalDate.now().monthValue,
                        LocalDate.now().year)
                    else listener.onMonthYearSelected(LocalDate.now().minusMonths(1).monthValue,
//                            LocalDate.now().minusYears(1).year)
                        LocalDate.now().year)
                }
            }
        }
    }
}

@BindingAdapter(value = ["billCollectionAdapter", "billCollectionListener"])
fun RecyclerView.bindBillColAdapter(collectionAdapter: CollectionsCardAdapter,
                                    listener: BillCollectionListener) {
    layoutManager = LinearLayoutManager(context)
    collectionAdapter.initListener(listener)
    adapter = collectionAdapter
}

@BindingAdapter(value = ["billSubmissionAdapter", "billSubmissionListener"])
fun RecyclerView.bindBillSubmitAdapter(billSubmissionAdapter: BillSubmissionCardsAdapter,
                                       listener: BillSubmissionListener) {
    layoutManager = LinearLayoutManager(context)
    billSubmissionAdapter.initListener(listener)
    adapter = billSubmissionAdapter
}

@BindingAdapter(value = ["rotaComplianceAdapter"])
fun RecyclerView.bindRotaComplianceAdapter(rotaComplianceAdapter: RotaComplianceAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = rotaComplianceAdapter
}

@BindingAdapter(value = ["monInputAdapter", "monInputListener"])
fun RecyclerView.bindMonInputAdapter(monInputAdapter: MonInputCardAdapter, listener: MonInputListener) {
    layoutManager = LinearLayoutManager(context)
    monInputAdapter.initListener(listener)
    adapter = monInputAdapter
}

/*@BindingAdapter(value = ["raisingCardsAdapter", "raisingListener"])
fun RecyclerView.bindRaisingCardsAdapter(raisingCardsAdapter: RaisingCardsAdapter,
    listener: UnitRaisingListener) {
    layoutManager = LinearLayoutManager(context)
    raisingCardsAdapter.initListener(listener)
    adapter = raisingCardsAdapter
}*/

@BindingAdapter(value = ["syncAdapter", "syncListener"])
fun RecyclerView.bindManualSync(syncAdapter: ManualSyncAdapter,
                                listener: ManualSyncAdapter.ManualSyncListener) {
//    layoutManager = LinearLayoutManager(context)
    layoutManager = GridLayoutManager(context, 2)
    syncAdapter.initListener(listener)
    adapter = syncAdapter
}

/*@BindingAdapter(value = ["unitRaisingAdapter", "unitRaisingListener"])
fun RecyclerView.bindUnitRaisingAdapter(raisingHomeAdapter: RaisingHomeAdapter,
    listener: UnitRaisingListener) {
    layoutManager = LinearLayoutManager(context)
    raisingHomeAdapter.initListener(listener)
    adapter = raisingHomeAdapter
}

@BindingAdapter(value = ["takeOverAdapter"])
fun RecyclerView.bindTakeOverAdapter(takeoverAdapter: RaisingTakeoverAdapter) {
    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    adapter = takeoverAdapter
}

@BindingAdapter(value = ["raisingRecruitAdapter"])
fun RecyclerView.bindRecruitmentAdapter(raisingRecruitAdapter: RaisingRecruitmentAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = raisingRecruitAdapter
}

@BindingAdapter(value = ["raisingBarrackAdapter"])
fun RecyclerView.bindRaisingBarrackAdapter(raisingBarrackAdapter: RaisingBarrackAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = raisingBarrackAdapter
}

@BindingAdapter(value = ["raisingKittingAdapter"])
fun RecyclerView.bindRaisingKittingAdapter(raisingBarrackAdapter: KittingAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = raisingBarrackAdapter
}

@BindingAdapter(value = ["preInductionAdapter"])
fun RecyclerView.bindRaisingPreInductionAdapter(preInductionAdapter: RaisingPreInductionAdapter) {
    val manager = LinearLayoutManager(context)
    layoutManager = manager
    val divider = DividerItemDecoration(context, manager.orientation)
    val item = ContextCompat.getDrawable(context, R.drawable.grey_divider_1sdp)
    if (item != null) {
        divider.setDrawable(item)
        addItemDecoration(divider)
    }
    adapter = preInductionAdapter
}*/

@BindingAdapter(value = ["myKPIAdapter"])
fun RecyclerView.bindMyKPI(kpiAdapter: MyKPIAdapter) {
    layoutManager = LinearLayoutManager(context)
    addItemDecoration(DividerDecoration(ContextCompat.getDrawable(context, R.drawable.black_divider)!!))
    adapter = kpiAdapter
}

@BindingAdapter(value = ["mapRegisterAdapter"])
fun RecyclerView.bindMapRegister(mapRegisterAdapter: MapRegistersAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = mapRegisterAdapter
}

@BindingAdapter(value = ["setIncentiveBackground"])
fun LinearLayout.setIncentiveBackground(incentiveStatus: String) {
    if (!TextUtils.isEmpty(incentiveStatus)) {
        setBackgroundColor(if (incentiveStatus == "NO") ContextCompat.getColor(context,
            R.color.colorRed_30opct) else ContextCompat.getColor(context, R.color.colorTransparentGreen))
    }
}

/*@BindingAdapter(value = ["activityId"])
fun AppCompatTextView.bindArrowView(activityId: Int) {
    if (activityId == UnitRaisingOptions.SALES_NOTIFICATION.optionsId || activityId == UnitRaisingOptions.CONTRACT_FINALISATION.optionsId ||
        activityId == UnitRaisingOptions.UUPT_FINALISATION.optionsId || activityId == UnitRaisingOptions.INVOICE_GENERATION.optionsId ||
        activityId == UnitRaisingOptions.TIMELY_WAGE_PAYOUT.optionsId || activityId == UnitRaisingOptions.COLLECTION.optionsId) {
        this.visibility = View.INVISIBLE
    } else
        this.visibility = View.VISIBLE
}

@BindingAdapter(value = ["activityId", "updatedDateTime"])
fun LinearLayout.bindRowLayout(activityId: Int, @Nullable updatedDateTime: String?) {
    if (activityId == UnitRaisingOptions.SALES_NOTIFICATION.optionsId || activityId == UnitRaisingOptions.CONTRACT_FINALISATION.optionsId ||
        activityId == UnitRaisingOptions.UUPT_FINALISATION.optionsId || activityId == UnitRaisingOptions.INVOICE_GENERATION.optionsId ||
        activityId == UnitRaisingOptions.TIMELY_WAGE_PAYOUT.optionsId || activityId == UnitRaisingOptions.COLLECTION.optionsId) {
        this.isEnabled = false
    } else this.isEnabled = updatedDateTime == null
}*/

/*@BindingAdapter(value = ["activityId", "updatedDateTime"])
fun View.bindActivityStatus(activityId: Int, @Nullable updatedDateTime: String?) {
    if (activityId == UnitRaisingOptions.SALES_NOTIFICATION.optionsId || activityId == UnitRaisingOptions.CONTRACT_FINALISATION.optionsId ||
        activityId == UnitRaisingOptions.UUPT_FINALISATION.optionsId) {
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen))
    } else if (updatedDateTime != null)
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen))
    else
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightGreyDark))
}

@BindingAdapter(value = ["modeItemsList", "trainingListener"])
fun AppCompatSpinner.bindTrainingModeSpinner(modeList: List<LookUpEntity>, listener: MTrainingModeListener) {
    if (!modeList.isNullOrEmpty()) {
        val modeNameList = ArrayList<String>()
        modeNameList.add("Mode")
        for (entity: LookUpEntity in modeList)
            modeNameList.add(entity.displayName)

        modeList.apply {
            val modeAdapter: ArrayAdapter<String> = ArrayAdapter(context,
                R.layout.support_simple_spinner_dropdown_item, modeNameList)
            adapter = modeAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                    listener.onModeSelection(pos)
                }
            }
        }
    }
}*/

@BindingAdapter(value = ["unPaidReasonList", "spinnerListener"])
fun AppCompatSpinner.bindAkrSpinner(unPaidReasonList: ArrayList<String>, listener: SpinnersListener) {
    if (!unPaidReasonList.isNullOrEmpty()) {
        val modeAdapter: ArrayAdapter<String> =
            ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, unPaidReasonList)
        adapter = modeAdapter
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                listener.onSpinnerOptionSelected(pos)
            }
        }
    }
}

@BindingAdapter(value = ["conveyanceAdapter", "conveyanceListener"])
fun RecyclerView.bindMonthConveyance(conveyanceAdapter: MonthlyConveyanceAdapter,
                                     listener: ConveyanceListener) {
    val manager = LinearLayoutManager(context)
    layoutManager = manager
    conveyanceAdapter.initListener(listener)
    val dividerItemDecoration = DividerItemDecoration(context, manager.orientation)
    val item = ContextCompat.getDrawable(context, R.drawable.grey_divider_1sdp)
    if (item != null) {
        dividerItemDecoration.setDrawable(item)
        addItemDecoration(dividerItemDecoration)
    }
    adapter = conveyanceAdapter
}

@BindingAdapter(value = ["formatDoubleOrInt"])
fun TextView.bindDoubleOrIntFormat(value: Double) {
    text = if (value.toInt() % 1 == 0) value.toInt().toString()
    else DecimalFormat("#.##").format(value)
}

/*@BindingAdapter(value = ["chatAdapter", "chatBotListener"])
fun RecyclerView.bindChatBotAdapter(chatAdapter: ChatBotAdapter, chatBotListener: ChatBotListener) {
    layoutManager = LinearLayoutManager(context)
    chatAdapter.initListener(chatBotListener)
    adapter = chatAdapter
}

@BindingAdapter(value = ["childAdapter", "listener"])
fun RecyclerView.bindChildQuesAdapter(childAdapter: ChildQuestionsAdapter,
    chatBotListener: ChatBotListener) {
    layoutManager = LinearLayoutManager(context)
    childAdapter.initListener(chatBotListener)
    adapter = childAdapter
}*/

@BindingAdapter(value = ["questionTypeId", "showWidget"])
fun AppCompatTextView.showHideChatWidgets(questionTypeId: Int, showWidget: Boolean) {
    visibility = if (showWidget) {
        if (questionTypeId == ChatWidgetEnum.DUAL_BUTTON.widgetId) View.VISIBLE
        else View.GONE
    } else View.GONE
}

@BindingAdapter(value = ["isFirstNode", "showWidget"])
fun AppCompatTextView.showHideSingleWidgets(isFirstNode: Boolean, showWidget: Boolean) {
    visibility = if (showWidget && isFirstNode) View.VISIBLE
    else View.GONE
}

@BindingAdapter(value = ["postCount", "qrTaggedCount"])
fun AppCompatTextView.postTagged(postCount: Int, qrTaggedCount: Int) {
    if (postCount > 0) {
        visibility = View.VISIBLE
        text = if (postCount == qrTaggedCount) {
            resources.getString(R.string.dynamic_all_qr_tagged, qrTaggedCount, postCount).toHtmlSpan()
        } else {
            resources.getString(R.string.dynamic_pending_qr_tagged, qrTaggedCount, postCount).toHtmlSpan()
        }
    } else visibility = View.GONE
}

/*@SuppressLint("SetJavaScriptEnabled")
@BindingAdapter(value = ["webViewUrl"])
fun WebView.bindWebView(webViewUrl: String) {
    settings.javaScriptEnabled = true
    settings.defaultTextEncodingName = "utf-8"
    webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url)
            return true
        }
    }
    loadUrl(webViewUrl)
}*/
