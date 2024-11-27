package com.sisindia.ai.android.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

    public static Gson toJsonWithoutExopse() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    public static Gson toJsonWithoutExposeAnnotation() {
        return new GsonBuilder().create();
    }

}
