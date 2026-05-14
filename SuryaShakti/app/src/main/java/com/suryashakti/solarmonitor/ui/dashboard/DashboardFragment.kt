package com.suryashakti.solarmonitor.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.suryashakti.solarmonitor.R
import com.suryashakti.solarmonitor.data.EnergyLog
import com.suryashakti.solarmonitor.databinding.FragmentDashboardBinding
import com.suryashakti.solarmonitor.utils.NotificationHelper
import com.suryashakti.solarmonitor.utils.WeatherSimulator
import com.suryashakti.solarmonitor.viewmodel.EnergyViewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val vm: EnergyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.todayLog.observe(viewLifecycleOwner) { log -> updateDashboard(log) }

        binding.btnSimulateWeather.setOnClickListener {
            val result = WeatherSimulator.simulateDay()
            vm.saveTodayGeneration(
                kwhGenerated = result.estimatedKwh,
                weather = result.condition.label,
                battery = binding.sliderBattery.value
            )
            val tip = WeatherSimulator.getTip(result.condition)
            val msg = "${result.condition.emoji} ${result.condition.label} — Est. ${result.estimatedKwh} kWh"
            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
            if (result.isPeakTime || result.condition == WeatherSimulator.WeatherCondition.SUNNY) {
                NotificationHelper.sendPeakSunAlert(requireContext(), tip)
            }
        }

        binding.sliderBattery.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                binding.tvBatteryPct.text = "${value.toInt()}%"
                vm.updateTodayBattery(value)
            }
        }

        binding.btnGoGeneration.setOnClickListener {
            findNavController().navigate(R.id.generationLogFragment)
        }
        binding.btnGoConsumption.setOnClickListener {
            findNavController().navigate(R.id.consumptionFragment)
        }
        binding.btnGoSavings.setOnClickListener {
            findNavController().navigate(R.id.savingsFragment)
        }

        binding.btnPeakAlert.setOnClickListener {
            val log = vm.todayLog.value
            val cond = WeatherSimulator.WeatherCondition.values()
                .firstOrNull { it.label == log?.weatherCondition }
                ?: WeatherSimulator.WeatherCondition.SUNNY
            NotificationHelper.sendPeakSunAlert(requireContext(), WeatherSimulator.getTip(cond))
            Toast.makeText(requireContext(), "Peak Sun Alert Sent! ☀️", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateDashboard(log: EnergyLog?) {
        if (log == null) {
            binding.circularProgress.setProgress(0f, "Solar", "No data")
            binding.tvGreenScore.text = "—"
            binding.tvGenerated.text  = "0.0 kWh"
            binding.tvConsumed.text   = "0.0 kWh"
            binding.tvNetEnergy.text  = "0.0 kWh"
            binding.tvSavings.text    = "₹0.00"
            binding.tvWeather.text    = "Not simulated"
            binding.tvStatus.text     = "Log your generation to begin"
            binding.tvStatus.setTextColor(Color.parseColor("#BDBDBD"))
            binding.cardOvergeneration.visibility = View.GONE
            return
        }

        val solarPct = if (log.consumedKwh > 0f)
            ((log.generatedKwh / log.consumedKwh) * 100f).coerceIn(0f, 100f)
        else if (log.generatedKwh > 0f) 100f else 0f

        val condLabel = WeatherSimulator.WeatherCondition.values()
            .firstOrNull { it.label == log.weatherCondition }?.emoji ?: "☀️"

        binding.circularProgress.setProgress(solarPct, "Solar", "$condLabel ${log.weatherCondition}")
        binding.tvGreenScore.text = "${log.greenScore}"
        colorGreenScore(log.greenScore)
        binding.tvGenerated.text = "%.2f kWh".format(log.generatedKwh)
        binding.tvConsumed.text  = "%.2f kWh".format(log.consumedKwh)

        val net = log.netKwh
        binding.tvNetEnergy.text = (if (net >= 0) "+%.2f kWh" else "%.2f kWh").format(net)
        binding.tvNetEnergy.setTextColor(Color.parseColor(if (net >= 0) "#00E676" else "#FF5252"))
        binding.tvSavings.text = "₹%.2f".format(log.totalBenefitRs)
        binding.tvWeather.text = "${log.weatherCondition} $condLabel"

        when {
            net > 0 -> {
                binding.tvStatus.text = "Over-generating! Exporting %.2f kWh to grid 🚀".format(net)
                binding.tvStatus.setTextColor(Color.parseColor("#00E676"))
                binding.cardOvergeneration.visibility = View.VISIBLE
                binding.tvExportEarnings.text = "Estimated export earnings: ₹%.2f".format(log.exportEarningsRs)
            }
            net < 0 -> {
                binding.tvStatus.text = "Grid dependent for %.2f kWh today".format(-net)
                binding.tvStatus.setTextColor(Color.parseColor("#FF5252"))
                binding.cardOvergeneration.visibility = View.GONE
            }
            else -> {
                binding.tvStatus.text = "Perfect balance — 100% self-sufficient today! ✅"
                binding.tvStatus.setTextColor(Color.parseColor("#FFD600"))
                binding.cardOvergeneration.visibility = View.GONE
            }
        }

        binding.sliderBattery.value = log.batteryLevel.coerceIn(0f, 100f)
        binding.tvBatteryPct.text   = "${log.batteryLevel.toInt()}%"
    }

    private fun colorGreenScore(score: Int) {
        val color = when {
            score >= 80 -> "#00E676"
            score >= 50 -> "#FFD600"
            else        -> "#FF5252"
        }
        binding.tvGreenScore.setTextColor(Color.parseColor(color))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
