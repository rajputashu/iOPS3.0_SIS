package com.sisindia.ai.android.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Created by Ashu Rajput on 2/9/2021.
 */
object MigrationFactory {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE EmployeeFineRewardEntity ADD COLUMN employeeNo TEXT")
            database.execSQL("ALTER TABLE CheckedGuardEntity ADD COLUMN employeeNo TEXT")
        }
    }
}