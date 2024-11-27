package com.sisindia.ai.android.features.billsubmit

import com.sisindia.ai.android.commons.CollectionMode


/**
 * Created by Ashu Rajput on 4/2/2020.
 */
interface RadioCheckedListener {
    fun onRadioButtonChecked(value: String)
}

interface BillCollectionRadioCheckedListener{
    fun onRadioButtonChecked(collectionMode: CollectionMode)
}