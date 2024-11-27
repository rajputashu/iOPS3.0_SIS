package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.AddSecurityRiskEntity;
import com.sisindia.ai.android.uimodels.SecurityRiskModel;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class AddSecurityRiskDao implements BaseDao<AddSecurityRiskEntity> {

    @Query("SELECT * from AddSecurityRiskEntity")
    public abstract Maybe<List<AddSecurityRiskEntity>> fetchAll();

    @Query("SELECT sr.*,at.*,lu.*" +
            " FROM AddSecurityRiskEntity AS sr " +
            "LEFT JOIN AttachmentEntity AS at ON at.attachmentGuid=sr.addSecurityGuid " +
            "LEFT JOIN LookUpEntity AS lu ON lu.id=sr.categoryId " +
            "WHERE taskId=:taskId AND postId=:postId")
    public abstract Single<List<SecurityRiskModel>> fetchAllCheckedSecurityRiskByPostId(int postId, int taskId);
}
