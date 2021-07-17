package com.example.pkl_v1.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pkl_v1.CustomMarker
import com.example.pkl_v1.R
import com.example.pkl_v1.databinding.FragmentDashboardBinding
import com.example.pkl_v1.model.ModelAcc
import com.example.pkl_v1.model.ModelGyro
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.coroutines.Runnable
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.sqrt
import kotlin.random.Random


class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!


    private lateinit var sensorManager: SensorManager
    private lateinit var sensorAcc: Sensor
    var flag = true
    var handler: Handler? = null
    val entries = ArrayList<Entry>()

    companion object {
        val TAG = "Sensor"
    }

    private val processSensors: Runnable = object : Runnable {
        override fun run() {
            // Do work with the sensor values.
            flag = true
            // The Runnable is posted to run again here:
            handler!!.postDelayed(this, 1000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handler = Handler()
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        Log.d("Sensor", "onSensorChanged: ")
        Toast.makeText(requireContext(), "Hallo", Toast.LENGTH_SHORT).show()

        val stepDetector = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    if (flag) {
                        val x_acceleration = event!!.values[0]
                        val y_acceleration = event.values[1]
                        val z_acceleration = event.values[2]

                        requireActivity().runOnUiThread {
                            val df = DecimalFormat("#.##")
                            // 1
                            df.roundingMode = RoundingMode.FLOOR
                            binding.IDDashboardTxtAcc.text =
                                df.format(x_acceleration) + " , " + df.format(y_acceleration) + " , " + df.format(
                                    z_acceleration
                                )
                            entries.add(Entry(x_acceleration, x_acceleration))

                            for (i in 0..entries.size) {
                                Log.d("Sensor", "onSensorChanged: " + i)
                            }


                        }
                        flag = false
                    }
                }

            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }
        }

        sensorManager.registerListener(
            stepDetector,
            sensorAcc,
            1000000
        )

        chart(dataValue1(), dataValue2())

    }


    fun dataValue1(): ArrayList<Entry> {

        val entries = ArrayList<Entry>()
        for (i in 1..40) {
            val a = (5..25).random()
            entries.add(Entry(i.toFloat(), a.toFloat()))
        }
        return entries
    }

    fun dataValue2(): ArrayList<Entry> {
        val entries = ArrayList<Entry>()
        for (i in 1..2) {
            val a = (5..25).random()
            entries.add(Entry(i.toFloat(), a.toFloat()))
        }
        return entries
    }

    fun chart(entries: ArrayList<Entry>, entries2: ArrayList<Entry>) {
        val vl = LineDataSet(entries, "Accelerometer")
        val vl2 = LineDataSet(entries2, "Accelerometer2")

        vl.apply {
            setDrawValues(false)
            lineWidth = 3f
            color = ContextCompat.getColor(requireContext(), R.color.yellow)
        }

        vl2.apply {
            setDrawValues(false)
            lineWidth = 3f
            color = ContextCompat.getColor(requireContext(), R.color.red)


        }




        binding.lineChart.xAxis.labelRotationAngle = 3f
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(vl)
        dataSets.add(vl2)

        binding.lineChart.data = LineData(dataSets)


        binding.lineChart.axisRight.isEnabled = false
        binding.lineChart.xAxis.axisMaximum = dataValue1().size + 10f


        binding.lineChart.setTouchEnabled(true)
        binding.lineChart.setPinchZoom(true)


        binding.lineChart.description.text = "Times"
        binding.lineChart.setNoDataText("No forex yet!")


        binding.lineChart.animateX(1800, Easing.EaseInExpo)


        val markerView = CustomMarker(requireContext(), R.layout.marker_view)
        binding.lineChart.marker = markerView
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        handler!!.post(processSensors);
    }

    override fun onPause() {
        super.onPause()
        handler!!.removeCallbacks(processSensors);
    }
}