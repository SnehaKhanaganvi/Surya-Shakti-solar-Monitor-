package com.suryashakti.solarmonitor.utils

import kotlin.random.Random

object WeatherSimulator {

    enum class WeatherCondition(val label: String, val emoji: String) {
        SUNNY("Sunny", "☀️"),
        PARTLY_CLOUDY("Partly Cloudy", "⛅"),
        CLOUDY("Cloudy", "☁️")
    }

    data class SimulationResult(
        val condition: WeatherCondition,
        val estimatedKwh: Float,
        val peakSunHours: Float,
        val isPeakTime: Boolean
    )

    fun simulateDay(panelCapacityKw: Float = 3.0f): SimulationResult {
        val condition = WeatherCondition.values().random()
        return simulate(condition, panelCapacityKw)
    }

    fun simulate(condition: WeatherCondition, panelCapacityKw: Float = 3.0f): SimulationResult {
        val (peakHours, efficiencyRange) = when (condition) {
            WeatherCondition.SUNNY         -> Pair(6.5f, 0.85f..0.95f)
            WeatherCondition.PARTLY_CLOUDY -> Pair(4.5f, 0.55f..0.70f)
            WeatherCondition.CLOUDY        -> Pair(2.0f, 0.20f..0.35f)
        }
        val efficiency = Random.nextFloat() *
            (efficiencyRange.endInclusive - efficiencyRange.start) + efficiencyRange.start
        val kwh = (panelCapacityKw * peakHours * efficiency * 10).toInt() / 10f
        val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        val isPeak = condition == WeatherCondition.SUNNY && currentHour in 10..14
        return SimulationResult(condition, kwh, peakHours, isPeak)
    }

    fun getTip(condition: WeatherCondition): String = when (condition) {
        WeatherCondition.SUNNY ->
            "High Sun: Ideal time to run heavy appliances like washing machine, pump & AC!"
        WeatherCondition.PARTLY_CLOUDY ->
            "Moderate generation. Run medium loads. Avoid heavy consumption from grid."
        WeatherCondition.CLOUDY ->
            "Low generation today. Conserve energy and rely on battery reserves."
    }
}
