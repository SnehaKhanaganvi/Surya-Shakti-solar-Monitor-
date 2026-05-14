package com.suryashakti.solarmonitor.ui.savings

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.suryashakti.solarmonitor.databinding.FragmentSavingsBinding
import com.suryashakti.solarmonitor.viewmodel.EnergyViewModel

class SavingsFragment : Fragment() {

    private var _binding: FragmentSavingsBinding? = null
    private val binding get() = _binding!!
    private val vm: EnergyViewModel by activityViewModels()
    private lateinit var adapter: SavingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SavingsAdapter()
        binding.recyclerSavings.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerSavings.adapter = adapter

        setupLineChart()

        vm.last30Logs.observe(viewLifecycleOwner) { logs ->
            adapter.submitList(logs)
            vm.computeMonthStats(logs)
            if (logs.isEmpty()) {
                binding.tvEmpty.visibility   = View.VISIBLE
                binding.cardSummary.visibility = View.GONE
                binding.lineChart.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility   = View.GONE
                binding.cardSummary.visibility = View.VISIBLE
                binding.lineChart.visibility = View.VISIBLE
                updateLineChart(logs.reversed())
            }
        }

        vm.monthStats.observe(viewLifecycleOwner) { stats ->
            binding.tvTotalGenerated.text = "%.2f kWh".format(stats.totalGenerated)
            binding.tvTotalConsumed.text  = "%.2f kWh".format(stats.totalConsumed)
            binding.tvTotalSavings.text   = "₹%.2f".format(stats.totalSavings)
            binding.tvAvgGreenScore.text  = "${stats.avgGreenScore}"
            binding.tvOverGenDays.text    = "${stats.overGenerationDays} day(s)"
            val scoreColor = when {
                stats.avgGreenScore >= 80 -> "#00E676"
                stats.avgGreenScore >= 50 -> "#FFD600"
                else                      -> "#FF5252"
            }
            binding.tvAvgGreenScore.setTextColor(Color.parseColor(scoreColor))
        }
    }

    private fun setupLineChart() {
        binding.lineChart.apply {
            description.isEnabled = false
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
                setDrawGridLines(false)
                granularity = 1f
                labelRotationAngle = -45f
            }
            setTouchEnabled(true)
            setPinchZoom(true)
        }
    }

    private fun updateLineChart(logs: List<com.suryashakti.solarmonitor.data.EnergyLog>) {
        val last = logs.takeLast(14)
        val labels = last.map { it.date.substring(5) }

        val genEntries = last.mapIndexed { i, log -> Entry(i.toFloat(), log.generatedKwh) }
        val conEntries = last.mapIndexed { i, log -> Entry(i.toFloat(), log.consumedKwh) }

        val genSet = LineDataSet(genEntries, "Generated (kWh)").apply {
            color = Color.parseColor("#FFD600")
            setCircleColor(Color.parseColor("#FFD600"))
            lineWidth = 2f
            circleRadius = 3f
            setDrawValues(false)
        }
        val conSet = LineDataSet(conEntries, "Consumed (kWh)").apply {
            color = Color.parseColor("#FF5252")
            setCircleColor(Color.parseColor("#FF5252"))
            lineWidth = 2f
            circleRadius = 3f
            setDrawValues(false)
        }

        binding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.lineChart.data = LineData(genSet, conSet)
        binding.lineChart.invalidate()
        binding.lineChart.animateX(800)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
