package com.suryashakti.solarmonitor.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "energy_logs")
data class EnergyLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val generatedKwh: Float = 0f,
    val consumedKwh: Float = 0f,
    val batteryLevel: Float = 0f,
    val weatherCondition: String = "Sunny",
    val perUnitRate: Float = 8.0f,
    val timestamp: Long = System.currentTimeMillis()
) {
    val netKwh: Float get() = generatedKwh - consumedKwh
    val solarSavingsRs: Float get() = generatedKwh.coerceAtMost(consumedKwh) * perUnitRate
    val exportEarningsRs: Float get() =
        if (netKwh > 0) netKwh * (perUnitRate * 0.75f) else 0f
    val totalBenefitRs: Float get() = solarSavingsRs + exportEarningsRs
    val gridCostRs: Float get() =
        if (netKwh < 0) (-netKwh) * perUnitRate else 0f
    val greenScore: Int get() = if (consumedKwh == 0f) 100
        else ((generatedKwh / consumedKwh) * 100).toInt().coerceIn(0, 100)
    val isOverGenerated: Boolean get() = netKwh > 0
}
