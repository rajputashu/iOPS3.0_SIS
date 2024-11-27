package com.sisindia.ai.android.features.issues.grievance

import com.sisindia.ai.android.uimodels.GuardSuggestionItem

interface GuardSuggestionViewListeners {
    fun onGuardSelected(item: GuardSuggestionItem)
    fun fetchGuardFromServer(employeeNo: String)
}