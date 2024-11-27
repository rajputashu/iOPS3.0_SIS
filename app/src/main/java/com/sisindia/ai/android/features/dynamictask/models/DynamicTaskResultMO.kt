package  com.sisindia.ai.android.features.dynamictask.models

/**
 * Created by Ashu_Rajput on 6/28/2021.
 */
data class DynamicTaskResultMO(
    var siteId: Int? = null,
    var taskId: Int? = null,
    var taskTypeId: Int? = null,
    var question: List<QuestionsMO>? = null)

data class QuestionsMO(
    var controlId: String? = null,
    var controlName: String? = null,
    var question: String? = null,
    var response: String? = null)