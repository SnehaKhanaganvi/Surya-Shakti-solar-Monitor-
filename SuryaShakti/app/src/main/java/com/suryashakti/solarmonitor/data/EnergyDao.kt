package com.suryashakti.solarmonitor.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EnergyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: EnergyLog)

    @Update
    suspend fun updateLog(log: EnergyLog)

    @Delete
    suspend fun deleteLog(log: EnergyLog)

    @Query("SELECT * FROM energy_logs WHERE date = :date LIMIT 1")
    fun getLogByDate(date: String): LiveData<EnergyLog?>

    @Query("SELECT * FROM energy_logs WHERE date = :date LIMIT 1")
    suspend fun getLogByDateSync(date: String): EnergyLog?

    @Query("SELECT * FROM energy_logs ORDER BY date DESC LIMIT 30")
    fun getLast30DaysLogs(): LiveData<List<EnergyLog>>

    @Query("SELECT * FROM energy_logs ORDER BY date DESC")
    fun getAllLogs(): LiveData<List<EnergyLog>>

    @Query("SELECT SUM(generatedKwh) FROM energy_logs WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getTotalGeneratedBetween(startDate: String, endDate: String): Float?

    @Query("SELECT SUM(consumedKwh) FROM energy_logs WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getTotalConsumedBetween(startDate: String, endDate: String): Float?

    @Query("SELECT COUNT(*) FROM energy_logs")
    suspend fun getTotalLogCount(): Int

    @Query("DELETE FROM energy_logs WHERE date < :cutoffDate")
    suspend fun deleteOlderThan(cutoffDate: String)
}
