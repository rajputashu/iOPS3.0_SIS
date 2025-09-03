package com.sisindia.ai.android.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sisindia.ai.android.features.uar.PoaAndImprovePlansMO
import com.sisindia.ai.android.features.uar.add.PoaActionPointMO
import com.sisindia.ai.android.models.poa.ClosePoaApiBodyMO
import com.sisindia.ai.android.room.BaseDao
import com.sisindia.ai.android.room.entities.LookUpEntity
import com.sisindia.ai.android.room.entities.SiteRiskPoaEntity
import com.sisindia.ai.android.room.entities.SiteRiskReasonsEntity
import com.sisindia.ai.android.uimodels.collection.CollectionAttachmentMO
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Created by Ashu Rajput on 4/6/2020.
 */
@Dao
abstract class SiteRiskPoaDao : BaseDao<SiteRiskPoaEntity> {

    @Query("SELECT * from SiteRiskPoaEntity")
    abstract fun fetchAll(): Maybe<List<SiteRiskPoaEntity>>

    @Query("UPDATE SiteRiskPoaEntity SET closureProofAttachment=1, closureDateTime= :closureDate, poAStatus=:status,closingRemarks=:remarks WHERE id = :poaId")
    abstract fun updateOnClosingPOA(closureDate: String,
        remarks: String,
        status: Int,
        poaId: Int): Maybe<Int>

    @Query("SELECT id,siteRiskId,closureDateTime,closingRemarks,closureProofAttachment from SiteRiskPoaEntity where poAStatus=3 and isSynced=0")
    abstract fun fetchAllCompletedButNotSynced(): Maybe<List<ClosePoaApiBodyMO>>

    @Query("UPDATE SiteRiskPoaEntity SET isSynced=1 where id=:poaId")
    abstract fun updateSyncedStatus(poaId: Int): Maybe<Int>

    @Query("DELETE FROM SiteRiskPoaEntity")
    abstract fun deleteSiteRiskPoa(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllAtRiskPOA(list: List<SiteRiskPoaEntity>): Single<List<Long>>

    @Query("SELECT (:sourceTypeId||'_'||:siteId||'_'||:riskId||'_'||:poaId||'_'||:guid||'.jpg') AS fileName, ai.zoneCode ||'/'||ai.regionCode||'/'||ai.branchCode||'/'||:siteId||'/'||:siteRiskId||'/'||:riskId AS storagePath FROM AIProfileEntity as ai")
    abstract fun getAttachmentDataForPoaClose(sourceTypeId: Int,
        siteId: Int,
        siteRiskId: Int,
        riskId: Int,
        poaId: Int,
        guid: String?): Single<CollectionAttachmentMO>

    @Query("Select * from LookUpEntity where LookupTypeId= :lookupTypeId")
    abstract fun getLookUpDataViaTypeId(lookupTypeId: Int): Single<List<LookUpEntity>>

    @Query("Select * from LookUpEntity where LookupTypeId=64 and LookupDependentValue=:lookupIdentifierId")
    abstract fun getReasonForRiskViaId(lookupIdentifierId: Int): Single<List<LookUpEntity>>

    @Query("Select ap.Id actionPlanId, ap.ActionPlanName from ActionPlanMapEntity apm join ActionPlanEntity ap on apm.ActionPlanId=ap.Id Join (Select LookupName,DisplayName,LookupIdentifier,LookupDependentValue from LookUpEntity where LookupTypeId=:lookupTypeId) POA on Poa.LookupIdentifier=apm.CategoryId where POA.LookupIdentifier= :lookupIdentifierId")
    abstract fun getActionPointListViaId(lookupTypeId: Int,
                                         lookupIdentifierId: Int): Single<List<PoaActionPointMO>>

    @Query("Select ipt.DisplayName planType,ipc.LookupName category, ap.ActionPlanName, ip.serverId id, ip.assignedToEmployeeName, ip.assignedDateTime,ip.closedDateTime,ip.description, ip.status from ImprovementPoaEntity ip left join SiteEntity s on s.id=ip.siteId left join (Select LookupName,DisplayName,LookupIdentifier,LookupDependentValue from LookUpEntity where LookupTypeId=25) ipt on ipt.LookupIdentifier= ip.plantype left join (Select LookupName,DisplayName,LookupIdentifier,LookupDependentValue from LookUpEntity where LookupTypeId=26) ipc on ipc.LookupIdentifier= ip.categoryId left join ActionPlanEntity ap on ap.Id=ip.actionplanId")
    abstract fun getImprovementPlanDashboard(): Single<List<PoaAndImprovePlansMO>>

    @Query("select count(*) uarCount from SiteRiskPoaEntity where poaStatus not in (3)")
    abstract fun getUarCount(): Single<Int>

    @Query("Select srp.Id id, src.DisplayName category,spr.DisplayName planType,ap.actionPlanName ,srp.assignedToName as assignedToEmployeeName,srp.targetCompletionDate as assignedDateTime, srp.closureDateTime as closedDateTime, srp.closingRemarks as description, srp.poaStatus status from SiteAtRiskEntity sr join SiteRiskPoaEntity srp on sr.Id=srp.SiteRiskId join SiteRiskReasonsEntity srr on srr.SiteRiskId=sr.id Join SiteEntity s on s.id=sr.siteId left join (Select LookupName,DisplayName,LookupIdentifier from LookUpEntity where LookupTypeId=63 ) src on src.LookupIdentifier= srr.RiskCategoryId left join (Select LookupName,DisplayName,LookupIdentifier,LookupDependentValue from LookUpEntity where LookupTypeId=23) spr on spr.LookupIdentifier= srp.PoaReason left join ActionPlanEntity ap on ap.Id=srp.atRiskActionPlan")
    abstract fun getRiskPoaDashboard(): Single<List<PoaAndImprovePlansMO>>

    @Query("Select srp.Id id, src.DisplayName category,spr.DisplayName planType,ap.actionPlanName ,srp.assignedToName as assignedToEmployeeName,srp.targetCompletionDate as assignedDateTime, srp.closureDateTime as closedDateTime,srp.openingRemarks as description, srp.poaStatus status from SiteAtRiskEntity sr join SiteRiskPoaEntity srp on sr.Id=srp.SiteRiskId join SiteRiskReasonsEntity srr on srr.SiteRiskId=sr.id Join SiteEntity s on s.id=sr.siteId left join (Select LookupName,DisplayName,LookupIdentifier from LookUpEntity where LookupTypeId=63 ) src on src.LookupIdentifier= srr.RiskCategoryId left join (Select LookupName,DisplayName,LookupIdentifier,LookupDependentValue from LookUpEntity where LookupTypeId=23) spr on spr.LookupIdentifier= srp.PoaReason left join ActionPlanEntity ap on ap.Id=srp.atRiskActionPlan where srp.Id= :id")
    abstract fun getSelectedPoa(id: Int): Single<PoaAndImprovePlansMO>

    @Query("Select ipt.DisplayName planType,ipc.LookupName category, ap.ActionPlanName, ip.serverId id, ip.assignedToEmployeeName, ip.assignedDateTime,ip.closedDateTime,ip.description, ip.status from ImprovementPoaEntity ip left join SiteEntity s on s.id=ip.siteId left join (Select LookupName,DisplayName,LookupIdentifier,LookupDependentValue from LookUpEntity where LookupTypeId=25) ipt on ipt.LookupIdentifier= ip.plantype left join (Select LookupName,DisplayName,LookupIdentifier,LookupDependentValue from LookUpEntity where LookupTypeId=26) ipc on ipc.LookupIdentifier= ip.categoryId left join ActionPlanEntity ap on ap.Id=ip.actionplanId where ip.serverId = :id")
    abstract fun getSelectedImprovementPlan(id: Int): Single<PoaAndImprovePlansMO>

    @Query("DELETE FROM SiteRiskReasonsEntity")
    abstract fun deleteSiteRiskReasons(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllAtRiskReasons(list: List<SiteRiskReasonsEntity>): Single<List<Long>>
}