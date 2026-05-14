package com.suryashakti.solarmonitor.ui.consumption

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.suryashakti.solarmonitor.databinding.FragmentConsumptionBinding
import com.suryashakti.solarmonitor.viewmodel.EnergyViewModel
import java.text.SimpleDateFormat
import java.util.*

class ConsumptionFragment : Fragment() {

    private var _binding: FragmentConsumptionBinding? = null
    private val binding get() = _binding!!
    private val vm: EnergyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConsumptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDate.text = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(Date())

        vm.todayLog.observe(viewLifecycleOwner) { log ->
            if (log != null) {
                if (log.consumedKwh > 0f)
                    binding.etCurrentReading.setText("%.2f".format(log.consumedKwh))
                binding.tvRate.text   = "₹%.2f/kWh".format(log.perUnitRate)
                binding.etRate.setText("%.2f".format(log.perUnitRate))
            }
        }

        binding.btnCalculate.setOnClickListener {
            val current  = binding.etCurrentReading.text.toString().toFloatOrNull()
            val previous = binding.etPreviousReading.text.toString().toFloatOrNull()
            val rate     = binding.etRate.text.toString().toFloatOrNull() ?: 8.0f

            if (current == null) {
                binding.etCurrentReading.error = "Enter current meter reading"
                return@setOnClickListener
            }

            val consumed = if (previous != null && previous < current) current - previous else current
            val generated = vm.todayLog.value?.generatedKwh ?: 0f
            val net = generated - consumed

            vm.updatePerUnitRate(rate)
            vm.saveTodayConsumption(consumed)

            binding.tvResultConsumed.text = "Consumed: %.2f kWh".format(consumed)
            binding.tvResultNet.text = if (net >= 0)
                "Net Surplus: +%.2f kWh ✅".format(net)
            else
                "Grid Import: %.2f kWh ⚡".format(-net)
            binding.tvResultNet.setTextColor(Color.parseColor(if (net >= 0) "#00E676" else "#FF5252"))

            val savings = generated.coerceAtMost(consumed) * rate
            val exportEarnings = if (net > 0) net * (rate * 0.75f) else 0f
            binding.tvResultSavings.text = "Solar Savings: ₹%.2f".format(savings)
            binding.tvResultExport.text = if (net > 0)
                "Export Earnings: ₹%.2f (Net Metering)".format(exportEarnings) else ""
            binding.tvResultExport.visibility = if (net > 0) View.VISIBLE else View.GONE

            val gridCost = if (net < 0) (-net) * rate else 0f
            binding.tvResultGridCost.text = if (gridCost > 0)
                "Grid Cost: ₹%.2f".format(gridCost) else "Grid Cost: ₹0.00 (Fully Solar!)"
            binding.tvResultGridCost.setTextColor(Color.parseColor(if (gridCost > 0) "#FF5252" else "#00E676"))

            binding.cardResults.visibility = View.VISIBLE

            if (net > 0) {
                binding.tvOverGenBanner.visibility = View.VISIBLE
                binding.tvOverGenBanner.text =
                    "🚀 Over-generation! You're exporting %.2f kWh to the grid (Prosumer mode)".format(net)
            } else {
                binding.tvOverGenBanner.visibility = View.GONE
            }
        }

        binding.btnSave.setOnClickListener {
            val consumed = binding.etCurrentReading.text.toString().toFloatOrNull()
            if (consumed == null || consumed < 0f) {
                binding.etCurrentReading.error = "Enter a valid reading"
                return@setOnClickListener
            }
            vm.saveTodayConsumption(consumed)
            Toast.makeText(requireContext(), "✅ Consumption saved!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
