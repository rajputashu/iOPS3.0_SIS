package com.sisindia.ai.android.mlcore.qrdetection;


/**
 * State set of the application workflow.
 */
public enum WorkflowState {
    NOT_STARTED,
    DETECTING,
    DETECTED,
    CONFIRMING,
    CONFIRMED,
    SEARCHING,
    SEARCHED
}
