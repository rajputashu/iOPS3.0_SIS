package com.sisindia.ai.android.uimodels;

import android.net.Uri;

import androidx.room.Ignore;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class PostRegisterModel {

    public int id;

    public int postId;

    public int siteId;

    public int taskId;

    public String displayName;

    public boolean isMissing;

    @Ignore
    public List<Uri> docUriList;

    public PostRegisterModel() {
    }

    public PostRegisterModel(int id, int postId, int siteId, int taskId, String displayName) {
        this.id = id;
        this.postId = postId;
        this.siteId = siteId;
        this.taskId = taskId;
        this.displayName = displayName;
        this.docUriList = new ArrayList<>();
    }


}
