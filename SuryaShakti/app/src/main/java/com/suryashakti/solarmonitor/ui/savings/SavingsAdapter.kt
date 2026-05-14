package com.suryashakti.solarmonitor.ui.savings

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suryashakti.solarmonitor.data.EnergyLog
import com.suryashakti.solarmonitor.databinding.ItemSavingsRowBinding
import com.suryashakti.solarmonitor.utils.WeatherSimulator

class SavingsAdapter :
    ListAdapter<EnergyLog, SavingsAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemSavingsRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(log: EnergyLog) {
            binding.tvDate.text    = log.date
            binding.tvGen.text     = "⚡ %.2f kWh".format(log.generatedKwh)
            binding.tvCon.text     = "🔌 %.2f kWh".format(log.consumedKwh)
            binding.tvSavings.text = "₹%.2f".format(log.totalBenefitRs)

            val net = log.netKwh
            binding.tvNet.text = if (net >= 0) "+%.2f".format(net) else "%.2f".format(net)
            binding.tvNet.setTextColor(Color.parseColor(if (net >= 0) "#00E676" else "#FF5252"))

            val emoji = WeatherSimulator.WeatherCondition.values()
                .firstOrNull { it.label == log.weatherCondition }?.emoji ?: "☀️"
            binding.tvWeather.text = emoji

            binding.tvGreenScore.text = "${log.greenScore}"
            val scoreColor = when {
                log.greenScore >= 80 -> "#00E676"
                log.greenScore >= 50 -> "#FFD600"
                else                 -> "#FF5252"
            }
            binding.tvGreenScore.setTextColor(Color.parseColor(scoreColor))
            binding.tvOverGen.visibility = if (log.isOverGenerated) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSavingsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EnergyLog>() {
            override fun areItemsTheSame(a: EnergyLog, b: EnergyLog) = a.id == b.id
            override fun areContentsTheSame(a: EnergyLog, b: EnergyLog) = a == b
        }
    }
}
