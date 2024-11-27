package com.sisindia.ai.android.features.issues

import com.sisindia.ai.android.uimodels.ClientModel

interface ClientSelectionListener {
    fun onClientSelected(model: ClientModel)
}