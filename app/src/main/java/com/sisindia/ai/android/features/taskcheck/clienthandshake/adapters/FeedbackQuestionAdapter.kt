package com.sisindia.ai.android.features.taskcheck.clienthandshake.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.droidcommons.base.recycler.BaseRecyclerAdapter
import com.droidcommons.base.recycler.BaseViewHolder
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.RowFeedbackQuestionsBinding
import com.sisindia.ai.android.uimodels.handshake.RatingQuestionsMO

/**
 * Created by Ashu Rajput on 4/10/2020.
 */
//class FeedbackQuestionAdapter : Adapter<FeedbackQuestionAdapter.FeedbackQuesViewHolder>() {
class FeedbackQuestionAdapter : BaseRecyclerAdapter<RatingQuestionsMO>() {

//    private lateinit var list: List<RatingQuestionsMO>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackQuesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: RowFeedbackQuestionsBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.row_feedback_questions, parent, false)
        return FeedbackQuesViewHolder(view)
    }

    /*fun updateFeedbackQuestionList(list: List<RatingQuestionsMO>) {
        this.list = list
        notifyDataSetChanged()
    }*/

    /*override fun getItemCount(): Int {
        return if (::list.isInitialized) list.size else 0
    }*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewHolder: FeedbackQuesViewHolder = holder as FeedbackQuesViewHolder
        viewHolder.onBind(getItem(position))
    }

    /* override fun onBindViewHolder(holder: FeedbackQuesViewHolder, position: Int) {
         holder.bindViews()
     }*/

    /*  inner class FeedbackQuesViewHolder(private val binder: RowFeedbackQuestionsBinding) :
          ViewHolder(binder.root) {
          fun bindViews() {
              binder.question = list[layoutPosition].question
              binder.questionsCheckBox.setOnClickListener {
                  list[layoutPosition].isOpted = !list[layoutPosition].isOpted
              }
          }
      }*/

    inner class FeedbackQuesViewHolder(val binder: RowFeedbackQuestionsBinding) :
        BaseViewHolder<RatingQuestionsMO>(binder) {
        override fun onBind(item: RatingQuestionsMO) {
            binder.question = items[layoutPosition].question
            binder.questionsCheckBox.setOnClickListener {
                items[layoutPosition].isOpted = !items[layoutPosition].isOpted
            }
        }
    }

    fun getSelectedQuestion(): String {
        val questionBuilder = StringBuilder()
        for (items in items) {
            if (items.isOpted)
                questionBuilder.append(items.question).append("\n")
        }
        return questionBuilder.toString()
    }
}