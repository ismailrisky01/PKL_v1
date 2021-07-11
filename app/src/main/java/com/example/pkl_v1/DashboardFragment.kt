package com.example.pkl_v1

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pkl_v1.databinding.FragmentDashboardBinding
import kotlin.math.sqrt


class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var sensorManager: SensorManager
    private lateinit var sensorAcc: Sensor
    private lateinit var sensorGyr: Sensor

    private var magnitudePrevious = 0.0

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
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorGyr = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        val stepDetector = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    val x_acceleration = event.values[0]
                    val y_acceleration = event.values[1]
                    val z_acceleration = event.values[2]

                    val magnitude = sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration)
                    val delta = magnitude - magnitudePrevious
                    magnitudePrevious = magnitude.toDouble()
                    binding.IDDashboardTxtAcc.text = delta.toString()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }
        }
        val Gyro = object :SensorEventListener{
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    val x_acceleration = event.values[0]
                    val y_acceleration = event.values[1]
                    val z_acceleration = event.values[2]

                    val magnitude =
                        sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration)
                    val delta = magnitude - magnitudePrevious;
                    magnitudePrevious = magnitude.toDouble()

                    binding.IDDashboardTxtGyro.text = delta.toString()

                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }
        }
        sensorManager.registerListener(
            stepDetector,
            sensorAcc,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            Gyro,
            sensorGyr,
            SensorManager.SENSOR_DELAY_NORMAL
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}