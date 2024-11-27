package com.sisindia.ai.android.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sisindia.ai.android.room.BaseDao
import com.sisindia.ai.android.room.entities.ClosedImprovementPoaEntity
import com.sisindia.ai.android.room.entities.ImprovementPoaEntity
import com.sisindia.ai.android.uimodels.uarpoa.IPPoaPendingCompletedMO
import com.sisindia.ai.android.uimodels.uarpoa.ImprovePlanHeaderCountsMO
import com.sisindia.ai.android.uimodels.uarpoa.SitesWithImprovePlansMO
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Created by Ashu Rajput on 12/14/2020.
 */
@Dao
abstract class ImprovementPoaDao : BaseDao<ImprovementPoaEntity> {

    /*@Query("SELECT * from ImprovementPoaEntity")
    abstract fun fetchAllDetails(): Single<ImprovementPoaEntity>*/

    @Query("DELETE FROM ImprovementPoaEntity")
    abstract fun deleteImprovementTable(): Completable

    //    @Query("Select (Select Count(DISTINCT siteId) from ImprovementPoaEntity) sitesWithPoa, (Select Count(*) from ImprovementPoaEntity where status!=4) pendingPlanPoa, (Select Count(*) from ImprovementPoaEntity where status=4) completedPlanPoa")
    @Query("Select (Select Count(DISTINCT siteId) from ImprovementPoaEntity) sitesWithPoa, (Select Count(*) from ImprovementPoaEntity where status!=3) pendingPlanPoa, (Select Count(*) from ImprovementPoaEntity where status=3) completedPlanPoa")
    abstract fun fetchIPSiteAndPoaCount(): Single<ImprovePlanHeaderCountsMO>

//    @Query("Select s.siteName,s.siteCode,s.siteAddress,ip.siteId, (Select Count(*) from ImprovementPoaEntity where siteId=s.id) totalIP, (Select Count(*) from ImprovementPoaEntity where status!=3 and siteId=s.id) pendingIP, (Select Count(*) from ImprovementPoaEntity where status=3 and siteId=s.id) completedIP from ImprovementPoaEntity ip join SiteEntity s on s.id=ip.siteId join ActionPlanEntity ap on ap.id=ip.actionPlanId left join (Select * from LookUpEntity where lookupTypeId=26) cat on cat.lookupIdentifier=ip.categoryId left join (Select * from LookUpEntity where lookuptypeid=27) st on st.lookupIdentifier=ip.status Group by s.siteName,s.siteCode,s.siteAddress,ip.siteId")
    @Query("Select s.siteName,s.siteCode,s.siteAddress,ip.siteId, (Select Count() from ImprovementPoaEntity where siteId=s.id) totalIP, (Select Count() from ImprovementPoaEntity where status!=3 and siteId=s.id) pendingIP,  (Select Count(*) from ImprovementPoaEntity where status=3 and siteId=s.id) completedIP   from ImprovementPoaEntity ip   join SiteEntity s on s.id=ip.siteId   left join ActionPlanEntity ap on ap.id=ip.actionPlanId  left join (Select * from LookUpEntity where lookupTypeId=26) cat on cat.lookupIdentifier=ip.categoryId   left join (Select * from LookUpEntity where lookuptypeid=27) st on st.lookupIdentifier=ip.status Group by s.siteName,s.siteCode,s.siteAddress,ip.siteId")
    abstract fun fetchSitesWithIPList(): Single<List<SitesWithImprovePlansMO>>

    @Query("Select ip.serverid as poaId, s.siteName,ip.assignedToEmployeeName,ip.assignedToEmployeeNo, ip.targetDateTime,ip.description,st.displayName status,cat.displayName category,1 as isPending from ImprovementPoaEntity ip join SiteEntity s on s.id=ip.siteId left join ActionPlanEntity ap on ap.id=ip.actionPlanId left join (Select * from LookUpEntity where lookupTypeId=26) cat on cat.lookupIdentifier=ip.categoryId left join (Select * from LookUpEntity where lookuptypeid=27) st on st.lookupIdentifier=ip.status where ip.siteId=:siteId and ip.status!=3")
    abstract fun fetchPendingPoaAtSite(siteId: Int): Single<List<IPPoaPendingCompletedMO>>

    //    @Query("Select ip.serverid as poaId, s.siteName,ip.assignedToEmployeeName,ip.assignedToEmployeeNo,ip.targetDateTime,ip.description,st.displayName status,cat.displayName category,0 as isPending  from ImprovementPoaEntity ip join SiteEntity s on s.id=ip.siteId join ActionPlanEntity ap on ap.id=ip.actionPlanId left join (Select * from LookUpEntity where lookupTypeId=26) cat on cat.lookupIdentifier=ip.categoryId left join (Select * from LookUpEntity where lookuptypeid=27) st on st.lookupIdentifier=ip.status where ip.siteId=:siteId and ip.status==4")
    @Query("Select ip.serverid as poaId, s.siteName,ip.assignedToEmployeeName,ip.assignedToEmployeeNo, ip.targetDateTime,ip.description,st.displayName status,cat.displayName category,0 as isPending from ImprovementPoaEntity ip join SiteEntity s on s.id=ip.siteId left join ActionPlanEntity ap on ap.id=ip.actionPlanId left join (Select * from LookUpEntity where lookupTypeId=26) cat on cat.lookupIdentifier=ip.categoryId left join (Select * from LookUpEntity where lookuptypeid=27) st on st.lookupIdentifier=ip.status where ip.siteId=:siteId and ip.status==3")
    abstract fun fetchCompletedPoaAtSite(siteId: Int): Single<List<IPPoaPendingCompletedMO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertClosePoaData(obj: ClosedImprovementPoaEntity): Maybe<Long>

    @Query("Select * from ClosedImprovementPoaEntity where isSynced=0")
    abstract fun fetchAllClosedButNotSynced(): Single<List<ClosedImprovementPoaEntity>>

    @Query("UPDATE ClosedImprovementPoaEntity SET isSynced=1 where poaId=:poaId")
    abstract fun updateSyncedStatus(poaId: Int): Single<Int>

    @Query("UPDATE ImprovementPoaEntity SET status=3 where serverId=:poaId")
    abstract fun updateStatusOfIP(poaId: Int): Single<Int>

    //    @Query("Select s.siteName,s.siteCode,s.siteAddress,ip.siteId,(Select Count(*) from ImprovementPoaEntity where siteId=s.id) totalIP, (Select Count(*) from ImprovementPoaEntity where status!=4 and siteId=s.id) pendingIP, (Select Count(*) from ImprovementPoaEntity where status=4 and siteId=s.id) completedIP from ImprovementPoaEntity ip join SiteEntity s on s.id=ip.siteId join ActionPlanEntity ap on ap.id=ip.actionPlanId left join (Select * from LookUpEntity where lookupTypeId=26) cat on cat.lookupIdentifier=ip.categoryId left join (Select * from LookUpEntity where lookuptypeid=27) st on st.lookupIdentifier=ip.status where ip.siteId=:siteId Group by s.siteName,s.siteCode,s.siteAddress,ip.siteId")
    @Query("Select s.siteName,s.siteCode,s.siteAddress,ip.siteId, (Select Count(*) from ImprovementPoaEntity where siteId=s.id) totalIP, (Select Count(*) from ImprovementPoaEntity where status!=3 and siteId=s.id) pendingIP, (Select Count(*) from ImprovementPoaEntity where status=3 and siteId=s.id) completedIP from ImprovementPoaEntity ip join SiteEntity s on s.id=ip.siteId left join ActionPlanEntity ap on ap.id=ip.actionPlanId left join (Select * from LookUpEntity where lookupTypeId=26) cat on cat.lookupIdentifier=ip.categoryId left join (Select * from LookUpEntity where lookuptypeid=27) st on st.lookupIdentifier=ip.status where ip.siteId=:siteId Group by s.siteName,s.siteCode,s.siteAddress,ip.siteId")
    abstract fun fetchSingleSitesWithIP(siteId: Int): Single<SitesWithImprovePlansMO>

    @Query("Select * from ImprovementPoaEntity")
    abstract fun getCurrentMonthLocalIPPoa(): Single<List<ImprovementPoaEntity>>
}