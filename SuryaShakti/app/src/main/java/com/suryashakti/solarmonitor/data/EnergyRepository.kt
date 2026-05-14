package com.suryashakti.solarmonitor.data

import android.content.Context
import androidx.lifecycle.LiveData

class EnergyRepository(context: Context) {

    private val dao = AppDatabase.getInstance(context).energyDao()

    fun getLogByDate(date: String): LiveData<EnergyLog?> = dao.getLogByDate(date)
    fun getLast30DaysLogs(): LiveData<List<EnergyLog>> = dao.getLast30DaysLogs()
    fun getAllLogs(): LiveData<List<EnergyLog>> = dao.getAllLogs()

    suspend fun upsertLog(log: EnergyLog) {
        val existing = dao.getLogByDateSync(log.date)
        if (existing == null) {
            dao.insertLog(log)
        } else {
            dao.updateLog(log.copy(id = existing.id))
        }
    }

    suspend fun getLogByDateSync(date: String): EnergyLog? = dao.getLogByDateSync(date)
    suspend fun getTotalGenerated(start: String, end: String): Float =
        dao.getTotalGeneratedBetween(start, end) ?: 0f
    suspend fun getTotalConsumed(start: String, end: String): Float =
        dao.getTotalConsumedBetween(start, end) ?: 0f
}
