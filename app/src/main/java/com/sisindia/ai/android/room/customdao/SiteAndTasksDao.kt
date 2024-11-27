package com.sisindia.ai.android.room.customdao

import androidx.room.Dao
import androidx.room.Query
import com.sisindia.ai.android.features.units.entity.SiteTaskDetailsMO
import io.reactivex.Maybe

/**
 * Created by Ashu Rajput on 3/30/2020.
 */
@Dao
abstract class SiteAndTasksDao {
//    @Query("SELECT s.id AS UnitId, s.siteName AS UnitName, s.siteCode as UnitCode, (Select tt.name from TaskEntity t join TaskTypeEntity tt on tt.id=t.taskTypeId where estimatedTaskExecutionStartDateTime>datetime('now','0 day','localtime') and taskStatus not in (3,4)and siteId=s.id) UpcomingTask, (Select count(*) from SitePostEntity where siteid=s.id and length(dutypostcode)>0) postTagged, s.siteAddress as SiteAddress , s.siteGeoPointString, se.billCollectionDay,se.billGenerationDay, se.billSubmissionDay,se.wageDay , (Select Count(*) from SitePostEntity where siteId=s.id )NoOfPost, CASE WHEN sp.postGeoPointString is NOT NULL THEN '1' else '0' end isUnitTagged , s.parentSiteId from SiteEntity s left join SiteExtensionEntity se on s.id=se.siteId left join SitePostEntity sp on sp.siteId=s.id and isMainPost=1 left join TaskEntity t on t.siteId = s.id and t.taskStatus in (1, 2) left join SiteBarrackMapsEntity ub on ub.siteId = s.id where s.isActive = 1 and s.id > 0 and s.isNCSite=0 group by s.id order by s.siteName")
    @Query("SELECT s.id AS UnitId, s.siteName AS UnitName, s.siteCode as UnitCode, (Select tt.name from TaskEntity t join TaskTypeEntity tt on tt.id=t.taskTypeId where estimatedTaskExecutionStartDateTime>datetime('now','0 day','localtime') and taskStatus not in (3,4)and siteId=s.id) UpcomingTask, (Select count(*) from SitePostEntity where siteid=s.id and length(dutypostcode)>0 and isdelete=0) postTagged, s.siteAddress as SiteAddress , s.siteGeoPointString, se.billCollectionDay,se.billGenerationDay, se.billSubmissionDay,se.wageDay , (Select Count(*) from SitePostEntity where siteId=s.id and isDelete=0) NoOfPost, CASE WHEN sp.postGeoPointString is NOT NULL THEN '1' else '0' end isUnitTagged , s.parentSiteId from SiteEntity s left join SiteExtensionEntity se on s.id=se.siteId left join SitePostEntity sp on sp.siteId=s.id and isMainPost=1 left join TaskEntity t on t.siteId = s.id and t.taskStatus in (1, 2) left join SiteBarrackMapsEntity ub on ub.siteId = s.id where s.isActive = 1 and s.id > 0 and s.isNCSite=0 group by s.id order by s.siteName")
    abstract fun fetchAllMySites(): Maybe<List<SiteTaskDetailsMO>>
}