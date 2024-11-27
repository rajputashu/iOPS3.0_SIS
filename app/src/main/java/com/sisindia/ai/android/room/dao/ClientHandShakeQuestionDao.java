package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.ClientHandShakeQuestionEntity;
import com.sisindia.ai.android.room.entities.ClientHandshakeRatingMapsEntity;
import com.sisindia.ai.android.uimodels.handshake.RatingQuestionsMO;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public abstract class ClientHandShakeQuestionDao implements BaseDao<ClientHandShakeQuestionEntity> {

    @Query("SELECT * from ClientHandShakeQuestionEntity")
    public abstract Single<List<ClientHandShakeQuestionEntity>> fetchAll();

    @Query("select chq.id as Id,chq.companyId,chq.questiontypeId,chq.question,chr.clienthandshakequestionId from ClientHandShakeQuestionEntity chq inner join ClientHandshakeRatingMapsEntity chr on chr.clienthandshakequestionid = chq.id where chr.ratingvalue = :rating")
    public abstract Single<List<RatingQuestionsMO>> fetchRatingQuestions(int rating);

    //ClientHandShakeQuestion
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllClientHandShakeQuestion(List<ClientHandShakeQuestionEntity> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllQuesRatingMap(List<ClientHandshakeRatingMapsEntity> list);

    @Query("DELETE FROM ClientHandShakeQuestionEntity")
    public abstract Completable deleteClientHandShakeQuestion();

    @Query("DELETE FROM ClientHandshakeRatingMapsEntity")
    public abstract Completable deleteClientHandshakeRatingMaps();
}
