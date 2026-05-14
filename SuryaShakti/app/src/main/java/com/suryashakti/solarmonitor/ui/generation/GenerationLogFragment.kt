package com.suryashakti.solarmonitor.ui.generation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.suryashakti.solarmonitor.databinding.FragmentGenerationLogBinding
import com.suryashakti.solarmonitor.utils.WeatherSimulator
import com.suryashakti.solarmonitor.viewmodel.EnergyViewModel
import java.text.SimpleDateFormat
import java.util.*

class GenerationLogFragment : Fragment() {

    private var _binding: FragmentGenerationLogBinding? = null
    private val binding get() = _binding!!
    private val vm: EnergyViewModel by activityViewModels()

    private var selectedWeather = WeatherSimulator.WeatherCondition.SUNNY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenerationLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDate.text = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(Date())

        // Pre-fill existing today log
        vm.todayLog.observe(viewLifecycleOwner) { log ->
            if (log != null && log.generatedKwh > 0f) {
                binding.etGeneration.setText("%.2f".format(log.generatedKwh))
                highlightWeatherButton(log.weatherCondition)
            }
        }

        // Observe last 30 logs for chart
        vm.last30Logs.observe(viewLifecycleOwner) { logs ->
            if (logs.isNotEmpty()) updateChart(logs.reversed())
        }

        // Weather chips
        binding.chipSunny.setOnClickListener {
            selectedWeather = WeatherSimulator.WeatherCondition.SUNNY
            highlightWeatherButton("Sunny")
        }
        binding.chipPartlyCloudy.setOnClickListener {
            selectedWeather = WeatherSimulator.WeatherCondition.PARTLY_CLOUDY
            highlightWeatherButton("Partly Cloudy")
        }
        binding.chipCloudy.setOnClickListener {
            selectedWeather = WeatherSimulator.WeatherCondition.CLOUDY
            highlightWeatherButton("Cloudy")
        }

        // Simulate
        binding.btnSimulate.setOnClickListener {
            val capacity = binding.etPanelCapacity.text.toString().toFloatOrNull() ?: 3.0f
            val result = WeatherSimulator.simulate(selectedWeather, capacity)
            binding.etGeneration.setText("%.2f".format(result.estimatedKwh))
            binding.tvSimInfo.text =
                "Simulated: ${result.condition.emoji} ${result.condition.label}\n" +
                "Peak sun hours: ${result.peakSunHours}h | Capacity: ${capacity}kWp"
            binding.tvSimInfo.visibility = View.VISIBLE
            binding.tvTip.text = WeatherSimulator.getTip(result.condition)
            binding.tvTip.visibility = View.VISIBLE
        }

        // Save
        binding.btnSave.setOnClickListener {
            val kwh = binding.etGeneration.text.toString().toFloatOrNull()
            if (kwh == null || kwh < 0f) {
                binding.etGeneration.error = "Enter a valid kWh value"
                return@setOnClickListener
            }
            vm.saveTodayGeneration(kwh, selectedWeather.label, 0f)
            Toast.makeText(requireContext(), "✅ Generation logged: %.2f kWh".format(kwh), Toast.LENGTH_SHORT).show()
        }

        setupChart()
    }

    private fun setupChart() {
        binding.barChart.apply {
            description.isEnabled = false
            setDrawGridBackground(false)
            setBackgroundColor(Color.parseColor("#2D2D2D"))
            legend.textColor = Color.WHITE
            axisRight.isEnabled = false
            axisLeft.apply {
                textColor = Color.WHITE
                gridColor = Color.parseColor("#444444")
                axisMinimum = 0f
            }
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = Color.WHITE
                gridColor = Color.TRANSPARENT
                setDrawGridLines(false)
                granularity = 1f
                labelRotationAngle = -45f
            }
            setPinchZoom(false)
            isDoubleTapToZoomEnabled = false
            setTouchEnabled(true)
        }
    }

    private fun updateChart(logs: List<com.suryashakti.solarmonitor.data.EnergyLog>) {
        val entries = logs.takeLast(14).mapIndexed { i, log ->
            BarEntry(i.toFloat(), log.generatedKwh)
        }
        val labels = logs.takeLast(14).map { it.date.substring(5) } // MM-dd

        val dataSet = BarDataSet(entries, "kWh Generated").apply {
            color = Color.parseColor("#FFD600")
            valueTextColor = Color.WHITE
            valueTextSize = 8f
        }

        binding.barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.barChart.data = BarData(dataSet)
        binding.barChart.invalidate()
        binding.barChart.animateY(800)
    }

    private fun highlightWeatherButton(label: String) {
        binding.chipSunny.alpha        = if (label == "Sunny") 1.0f else 0.4f
        binding.chipPartlyCloudy.alpha = if (label == "Partly Cloudy") 1.0f else 0.4f
        binding.chipCloudy.alpha       = if (label == "Cloudy") 1.0f else 0.4f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
