package com.sisindia.ai.android.models;

public enum CheckListOptionResponseType {
    NONE("None"), IMAGE("Image"), TEXT("Text"), RADIO_BUTTON_LIST("RadioButtonList");

    private final String responseType;

    CheckListOptionResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getResponseType() {
        return responseType;
    }
}
