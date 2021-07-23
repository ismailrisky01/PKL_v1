package com.example.pkl_v1.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pkl_v1.databinding.FragmentDashboardBinding
import com.example.pkl_v1.model.ModelAcc
import com.example.pkl_v1.model.ModelGyro
import com.example.pkl_v1.viewmodel.SensorViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat



class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var mSensorViewModel: SensorViewModel
    private lateinit var sensorManager: SensorManager
    private lateinit var sensorAcc: Sensor
    private lateinit var sensorGyro: Sensor
    private var total = 0
    private var delayAcc = true
    private var delayGyro = true
    private var handler: Handler? = null

    private val processSensors: Runnable = object : Runnable {
        override fun run() {
            // Do work with the sensor values.
            delayAcc = true
            delayGyro = true
            // The Runnable is posted to run again here:
            handler!!.postDelayed(this, 1000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handler = Handler(Looper.getMainLooper())
        sensor()
        mSensorViewModel = ViewModelProvider(this).get(SensorViewModel::class.java)
        mSensorViewModel.readAllSensorGyro.observe(viewLifecycleOwner, {
            Log.d("Jumlah Sensor",it.size.toString())
            if (it.size >= 20) {
                GlobalScope.launch {
                    val avgXAcc = mSensorViewModel.avgXAcc()
                    val avgYAcc = mSensorViewModel.avgYAcc()
                    val avgZAcc = mSensorViewModel.avgZAcc()
                    val avgXgyro = mSensorViewModel.avgXGyro()
                    val avgYgyro = mSensorViewModel.avgYGyro()
                    val avgZgyro = mSensorViewModel.avgZGyro()
                    Log.d("Sensor Avg: ", avgXAcc.toString())
                    mSensorViewModel.deleteALLSensor()
                }
            }
        })



}

    fun sensor() {
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)






    }


    val accDetector = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                if (delayAcc) {
                    val x_acc = event!!.values[0]
                    val y_acc = event.values[1]
                    val z_acc = event.values[2]
                    val scale = Math.pow(10.0, 3.0)
                    val x_conv = Math.round(x_acc * scale) / scale
                    val y_conv = Math.round(y_acc * scale) / scale
                    val z_conv = Math.round(z_acc * scale) / scale
                    Log.d("Sensor Acc", "$x_conv + $y_conv + $z_conv")
                    mSensorViewModel.addSensorAcc(ModelAcc(0, x_conv, y_conv, z_conv))
                    requireActivity().runOnUiThread {
                        val df = DecimalFormat("#.##")
                        df.roundingMode = RoundingMode.FLOOR



                    }
                    delayAcc = false
                }
            }

        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }
    }

    val gyroDetector = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                if (delayGyro) {
                    val x_gyro = event!!.values[0]
                    val y_gyro = event.values[1]
                    val z_gyro = event.values[2]
                    val scale = Math.pow(10.0, 3.0)
                    val x_conv = Math.round(x_gyro * scale) / scale
                    val y_conv = Math.round(y_gyro * scale) / scale
                    val z_conv = Math.round(z_gyro * scale) / scale
                    Log.d("Sensor Gyro", "$x_conv + $y_conv + $z_conv")
                    mSensorViewModel.addSensorGyro(ModelGyro(0, x_conv, y_conv, z_conv))
                    requireActivity().runOnUiThread {
                        val df = DecimalFormat("#.##")
                        df.roundingMode = RoundingMode.FLOOR

                    }
                    delayGyro = false
                }
            }

        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        handler!!.post(processSensors)
        sensorManager.registerListener(
            accDetector,
            sensorAcc,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            gyroDetector,
            sensorGyro,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        handler!!.removeCallbacks(processSensors)
        sensorManager.unregisterListener(accDetector)
        sensorManager.unregisterListener(gyroDetector)
    }


}
