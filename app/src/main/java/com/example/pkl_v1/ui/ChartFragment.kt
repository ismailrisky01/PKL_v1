package com.example.pkl_v1

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pkl_v1.databinding.FragmentChartBinding
import com.example.pkl_v1.model.ModelGyro
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.animation.Easing.EaseInExpo
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF


class ChartFragment : Fragment() {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!
    val data = ArrayList<ModelGyro>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Part1

        val entries = ArrayList<Entry>()

//Part2
        data.add(ModelGyro(1,10))
        data.add(ModelGyro(2,2))
        data.add(ModelGyro(3,7))
        data.add(ModelGyro(4,20))
        data.add(ModelGyro(5,16))
        binding.button.setOnClickListener {
data.clear()

            data.add(ModelGyro(6,18))
            data.forEach {
                entries.add(Entry(it.x.toFloat(), it.y.toFloat()))
            }
            chrt(entries)
        }

        data.forEach {
            entries.add(Entry(it.x.toFloat(), it.y.toFloat()))
        }
        chrt(entries)
//Part3

    }
fun chrt( entries:ArrayList<Entry>){
    val vl = LineDataSet(entries, "My Type")

//Part4
    vl.setDrawValues(false)
    vl.setDrawFilled(true)
    vl.lineWidth = 3f
    vl.fillColor = R.color.design_default_color_secondary
    vl.fillAlpha = R.color.design_default_color_error

//Part5
    binding.lineChart.xAxis.labelRotationAngle = 0f

//Part6
    binding.lineChart.data = LineData(vl)

//Part7
    binding.lineChart.axisRight.isEnabled = false
    binding.lineChart.xAxis.axisMaximum = 9+0.1f

//Part8
    binding.lineChart.setTouchEnabled(true)
    binding.lineChart.setPinchZoom(true)

//Part9
    binding.lineChart.description.text = "Days"
    binding.lineChart.setNoDataText("No forex yet!")

//Part10
    binding.lineChart.animateX(1800, Easing.EaseInExpo)

//Part11
    val markerView = CustomMarker(requireContext(), R.layout.marker_view)
    binding.lineChart.marker = markerView
}

}

class CustomMarker(context: Context, layoutResource: Int):  MarkerView(context, layoutResource) {
    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        val value = entry?.y?.toDouble() ?: 0.0
        var resText = ""
        if(value.toString().length > 8){
            resText = "Val: " + value.toString().substring(0,7)
        }
        else{
            resText = "Val: " + value.toString()
        }
        super.refreshContent(entry, highlight)
    }

    override fun getOffsetForDrawingAtPoint(xpos: Float, ypos: Float): MPPointF {
        return MPPointF(-width / 2f, -height - 10f)
    }
}