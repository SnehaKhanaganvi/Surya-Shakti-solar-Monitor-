package com.suryashakti.solarmonitor.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.suryashakti.solarmonitor.data.EnergyLog
import com.suryashakti.solarmonitor.data.EnergyRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EnergyViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = EnergyRepository(application)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val todayDate: String get() = dateFormat.format(Date())

    val todayLog: LiveData<EnergyLog?> = repo.getLogByDate(todayDate)
    val last30Logs: LiveData<List<EnergyLog>> = repo.getLast30DaysLogs()

    private val _monthStats = MutableLiveData<MonthStats>()
    val monthStats: LiveData<MonthStats> = _monthStats

    data class MonthStats(
        val totalGenerated: Float,
        val totalConsumed: Float,
        val totalSavings: Float,
        val avgGreenScore: Int,
        val overGenerationDays: Int
    )

    fun saveTodayGeneration(kwhGenerated: Float, weather: String, battery: Float) {
        viewModelScope.launch {
            val existing = repo.getLogByDateSync(todayDate)
            val log = (existing ?: EnergyLog(date = todayDate)).copy(
                generatedKwh = kwhGenerated,
                weatherCondition = weather,
                batteryLevel = battery
            )
            repo.upsertLog(log)
        }
    }

    fun saveTodayConsumption(kwhConsumed: Float) {
        viewModelScope.launch {
            val existing = repo.getLogByDateSync(todayDate)
            val log = (existing ?: EnergyLog(date = todayDate)).copy(
                consumedKwh = kwhConsumed
            )
            repo.upsertLog(log)
        }
    }

    fun updateTodayBattery(batteryPct: Float) {
        viewModelScope.launch {
            val existing = repo.getLogByDateSync(todayDate) ?: EnergyLog(date = todayDate)
            repo.upsertLog(existing.copy(batteryLevel = batteryPct))
        }
    }

    fun updatePerUnitRate(rate: Float) {
        viewModelScope.launch {
            val existing = repo.getLogByDateSync(todayDate) ?: EnergyLog(date = todayDate)
            repo.upsertLog(existing.copy(perUnitRate = rate))
        }
    }

    fun saveFullLog(log: EnergyLog) {
        viewModelScope.launch { repo.upsertLog(log) }
    }

    fun computeMonthStats(logs: List<EnergyLog>) {
        if (logs.isEmpty()) {
            _monthStats.value = MonthStats(0f, 0f, 0f, 0, 0)
            return
        }
        val totalGen  = logs.sumOf { it.generatedKwh.toDouble() }.toFloat()
        val totalCon  = logs.sumOf { it.consumedKwh.toDouble() }.toFloat()
        val totalSav  = logs.sumOf { it.totalBenefitRs.toDouble() }.toFloat()
        val avgScore  = logs.map { it.greenScore }.average().toInt()
        val overDays  = logs.count { it.isOverGenerated }
        _monthStats.value = MonthStats(totalGen, totalCon, totalSav, avgScore, overDays)
    }
}
