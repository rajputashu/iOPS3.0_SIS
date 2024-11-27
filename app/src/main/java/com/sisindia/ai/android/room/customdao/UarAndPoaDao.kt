package com.sisindia.ai.android.room.customdao

import androidx.room.Dao
import androidx.room.Query
import com.sisindia.ai.android.uimodels.uarpoa.AtRiskAndPoaCountModel
import com.sisindia.ai.android.uimodels.uarpoa.AtRiskAndPoaDetailsMO
import com.sisindia.ai.android.uimodels.uarpoa.POADetailsMO
import io.reactivex.Maybe

/**
 * Created by Ashu Rajput on 4/7/2020.
 */
@Dao
abstract class UarAndPoaDao {

    @Query("SELECT (Select Count(DISTINCT(sr.siteId)) from SiteAtRiskEntity sr join SiteEntity s on sr.siteId=s.id where sr.id in (Select siteRiskId from SiteRiskPoaEntity)) AS TotalSite, (SELECT COUNT(id) FROM SiteRiskPoaEntity) AS TotalPOAs, (SELECT COUNT(id) FROM SiteRiskPoaEntity WHERE poAStatus=3) AS TotalCompletePoAs")
    abstract fun fetchSiteAndPoaCount(): Maybe<AtRiskAndPoaCountModel>

    @Query("select t.siteId as SiteId,t.siteName as SiteName,substr(t.targetCompletionDate,1,10) as targetCompletionDate, " +
            "CASE WHEN length(Pending)>1 THEN Pending ELSE '0'||Pending END as Pending, " +
            "CASE WHEN length(TotalPOAs)>1 THEN TotalPOAs ELSE '0'||TotalPOAs END as TotalPOAs, " +
            "CASE WHEN length(Complete)>1 THEN Complete ELSE '0'||Complete END as Complete " +
            ",((complete*100)/totalpoas) as Progress " + "from (select sr.siteId,s.siteName,sr.id,srPoa.targetCompletionDate, " +
            " (SELECT COUNT(id) FROM SiteRiskPoaEntity where poAStatus=1 and siteRiskId=sr.id ) AS Pending, " +
            "( SELECT COUNT(id) FROM SiteRiskPoaEntity where siteRiskId=sr.id ) AS TotalPOAs, " +
            "( SELECT COUNT(id) FROM SiteRiskPoaEntity WHERE poAStatus=3 and siteRiskId=sr.id ) AS Complete " +
            "from SiteAtRiskEntity sr join SiteRiskPoaEntity srPoa on sr.id=srPoa.siteRiskId " +
            "join SiteEntity s on s.id=sr.siteId group by sr.siteId) t")
    abstract fun fetchUARDetails(): Maybe<List<AtRiskAndPoaDetailsMO>>

    @Query("select t.siteId as SiteId,t.siteName as SiteName,substr(t.targetCompletionDate,1,10) as targetCompletionDate, " +
            "CASE WHEN length(Pending)>1 THEN Pending ELSE '0'||Pending END as Pending, " +
            "CASE WHEN length(TotalPOAs)>1 THEN TotalPOAs ELSE '0'||TotalPOAs END as TotalPOAs, " +
            "CASE WHEN length(Complete)>1 THEN Complete ELSE '0'||Complete END as Complete, " +
            "((complete*100)/totalpoas) as Progress from (select sr.siteId,s.siteName,sr.id,srPoa.targetCompletionDate, " +
            " (SELECT COUNT(id) FROM SiteRiskPoaEntity where poAStatus=1 and siteRiskId=sr.id ) AS Pending, " +
            "( SELECT COUNT(id) FROM SiteRiskPoaEntity where siteRiskId=sr.id ) AS TotalPOAs, " +
            "( SELECT COUNT(id) FROM SiteRiskPoaEntity WHERE poAStatus=3 and siteRiskId=sr.id ) AS Complete " +
            "from SiteAtRiskEntity sr join SiteRiskPoaEntity srPoa on sr.id=srPoa.siteRiskId " +
            "join SiteEntity s on s.id=sr.siteId group by sr.siteId) t where t.siteid=:siteId")
    abstract fun fetchUARDetailsFromSiteId(siteId: Int): Maybe<AtRiskAndPoaDetailsMO>

    //    @Query("select distinct(substr(finalclosuredate,1,10)) from siteAtRiskEntity where finalclosuredate is not null")
    @Deprecated("Need to replace with new query")
    @Query("select distinct(substr(t.targetCompletionDate,1,10)) as targetCompletionDate from (select srPoa.targetCompletionDate from SiteAtRiskEntity sr join SiteRiskPoaEntity srPoa on sr.id=srPoa.siteRiskId join SiteEntity s on s.id=sr.siteId group by sr.siteId) t")
    abstract fun fetchAllDueDates(): Maybe<List<String>>

    @Query("select distinct(substr(srp.targetCompletionDate,1,10)) as targetCompletionDate from SiteRiskPoaEntity srp join SiteAtRiskEntity sr on sr.id=srp.siteRiskId where sr.siteId=:siteId and srp.poAStatus=:pOAStatus")
    abstract fun fetchAllDueDatesOfPOA(siteId: Int, pOAStatus: Int): Maybe<List<String>>

    //    @Query("select srp.id as poaId,Reason.displayName as Name,actionPlan.displayName as Category,srp.assignedToName as AssignedTo,substr(srp.targetCompletionDate,1,10) as targetCompletionDate,srp.poAStatus from SiteRiskPoaEntity srp join (select * from LookUpEntity where lookupTypeId=23) ActionPlan on ActionPlan.lookupIdentifier=srp.atRiskActionPlan join (select * from LookUpEntity where lookupTypeId=24) Reason on Reason.lookupIdentifier=srp.poAReason join SiteAtRiskEntity sr on sr.id=srp.siteRiskId where sr.siteId=:siteID and srp.poAStatus=:pOAStatus")
    //    @Query("select srp.id as poaId,Reason.displayName as Name,actionPlan.actionPlanName as Category, srp.assignedToName as AssignedTo,substr(srp.targetCompletionDate,1,10) as targetCompletionDate, srp.poAStatus from SiteRiskPoaEntity srp left join ActionPlanEntity ActionPlan on ActionPlan.id=srp.atRiskActionPlan left join (select * from LookUpEntity where lookupTypeId=24) Reason on Reason.lookupIdentifier=srp.poAReason left join SiteAtRiskEntity sr on sr.id=srp.siteRiskId where sr.siteId=:siteID and srp.poAStatus=:pOAStatus")
    @Query("select srp.id as poaId,Reason.displayName as Name,actionPlan.actionPlanName as Category, srp.assignedToName as AssignedTo,substr(srp.targetCompletionDate,1,10) as targetCompletionDate, srp.poAStatus as poAStatus from SiteRiskPoaEntity srp join ActionPlanEntity ActionPlan on ActionPlan.id=srp.atRiskActionPlan join (select * from LookUpEntity where lookupTypeId=23) Reason on Reason.lookupIdentifier=srp.poAReason join SiteAtRiskEntity sr on sr.id=srp.siteRiskId where sr.siteId=:siteID and srp.poAStatus=:status")
    abstract fun fetchAllPOAs(siteID: Int, status: Int): Maybe<List<POADetailsMO>>
}