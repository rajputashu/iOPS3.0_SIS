package com.sisindia.ai.android.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sisindia.ai.android.models.StrengthCheckModel;
import com.sisindia.ai.android.room.BaseDao;
import com.sisindia.ai.android.room.entities.CacheStrengthEntity;
import com.sisindia.ai.android.room.entities.SiteStrengthEntity;
import com.sisindia.ai.android.uimodels.mysite.SiteStrengthMO;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class SiteStrengthDao implements BaseDao<SiteStrengthEntity> {

    @Query("SELECT * from SiteStrengthEntity")
    public abstract Maybe<List<SiteStrengthEntity>> fetchAll();

    @Query("select ss.* from SiteStrengthEntity ss join SitePostEntity sp on ss.postId=sp.id where sp.siteId=:siteId")
    public abstract Maybe<List<SiteStrengthEntity>> fetchStrengthDetails(int siteId);

    @Query("SELECT ss.* FROM SiteStrengthEntity ss WHERE ss.siteId=:siteId")
    public abstract Single<List<SiteStrengthEntity>> fetchAllStrengthBySiteId(int siteId);

    @Query("SELECT ss.* FROM CacheStrengthEntity AS ss  WHERE ss.taskId=:taskId")
    public abstract Single<List<CacheStrengthEntity>> fetchAllCacheStrengthByTaskId(int taskId);

    /*@Query("select ss.id,ss.authorizedStrength,ss.actualStrength, ss.authorizedStrength-ss.actualStrength as " +
            "deficience,ss.grade from SiteStrengthEntity ss left join SitePostEntity sp on ss.postId=sp.id where sp.siteid=:siteId")*/
    @Query("select ss.id,ss.authorizedStrength,case when css.actualStrength isnull then 0 else css.actualStrength end as actualStrength, ss.authorizedStrength-ss.actualStrength as deficience,ss.grade " +
            "from SiteStrengthEntity ss left join CacheStrengthEntity css on css.siteId=ss.siteId where ss.siteid=:siteId group by ss.grade")
    public abstract Single<List<SiteStrengthMO>> fetchStrengthGraphData(int siteId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllCacheStrength(List<CacheStrengthEntity> obj);

    @Query("SELECT count(*)  FROM CacheStrengthEntity WHERE taskId = :taskId")
    public abstract Single<Integer> isCacheStrengthPresent(int taskId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insertCacheStrengthEntity(CacheStrengthEntity item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insertAllCacheStrengthEntity(List<CacheStrengthEntity> items);

    @Query("SELECT ts.taskId, " +
            "(SELECT COUNT(id) FROM CacheStrengthEntity WHERE taskId=ts.taskId AND actualStrength is  NULL) AS pendingStrengthCheck, " +
            "(SELECT SUM(authorizedStrength) FROM CacheStrengthEntity WHERE taskId=ts.taskId) AS totalGuards, " +
            "(SELECT SUM(actualStrength) FROM CacheStrengthEntity WHERE taskId=ts.taskId) AS totalChecked, " +
//            "(SELECT ABS( SUM(authorizedStrength) - SUM(actualStrength)) AS shortage FROM CacheStrengthEntity WHERE taskId = ts.taskId) as shortage " +
            "(SELECT SUM(authorizedStrength) - SUM(actualStrength) AS shortage FROM CacheStrengthEntity WHERE taskId = ts.taskId) as shortage " +
            "FROM CacheStrengthEntity AS ts WHERE taskId = :taskId GROUP BY taskId")
    public abstract Single<StrengthCheckModel> fetchCheckedStrengthCheck(int taskId);

    //SiteStrength
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insertAllSiteStrength(List<SiteStrengthEntity> list);

    @Query("DELETE FROM SiteStrengthEntity")
    public abstract Completable deleteSiteStrength();

    @Query("select sum(authorizedstrength) from sitestrengthEntity where siteid=:siteId")
    public abstract Single<Integer> getSiteAuthStrength(int siteId);

    @Query("select count(*) from sitepostEntity where siteid=:siteId")
    public abstract Single<Integer> getNoOfPostAtSite(int siteId);


}



