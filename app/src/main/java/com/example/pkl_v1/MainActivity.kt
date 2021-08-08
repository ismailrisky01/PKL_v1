package com.example.pkl_v1

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pkl_v1.model.ModelAcc
import com.example.pkl_v1.model.ModelGyro
import com.example.pkl_v1.viewmodel.SensorViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.nield.kotlinstatistics.*
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var mSensorViewModel: SensorViewModel
    private lateinit var sensorManager: SensorManager
    private lateinit var sensorAcc: Sensor
    private lateinit var sensorGyro: Sensor
    private var delayAcc = true
    private var delayGyro = true
    private var handler: Handler? = null
    private val processSensors: Runnable = object : Runnable {
        override fun run() {
            // Do work with the sensor values.
            delayAcc = true
            delayGyro = true
            // The Runnable is posted to run again here:
            handler!!.postDelayed(this, 250)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensor()
        supportActionBar?.hide()
//        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.nav_host_fragment)
//        bottomNavigation.setupWithNavController(navController)
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            if (destination.id == R.id.dashboardFragment || destination.id == R.id.profileFragment) {
//                bottomNavigation.visibility = View.VISIBLE
//            } else {
//                bottomNavigation.visibility = View.GONE
//            }
//        }
    }
   private val accDetector = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                if (delayAcc) {
                    val x_acc = event.values[0]
                    val y_acc = event.values[1]
                    val z_acc = event.values[2]
                    val scale = Math.pow(10.0, 3.0)
                    val x_conv = Math.round(x_acc * scale) / scale
                    val y_conv = Math.round(y_acc * scale) / scale
                    val z_conv = Math.round(z_acc * scale) / scale
                    Log.d("Sensor Acc", "$x_conv + $y_conv + $z_conv")
                    mSensorViewModel.addSensorAcc(ModelAcc(0, x_conv, y_conv, z_conv))
                    runOnUiThread {
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

   private val gyroDetector = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                if (delayGyro) {
                    val x_gyro = event.values[0]
                    val y_gyro = event.values[1]
                    val z_gyro = event.values[2]
                    val scale = Math.pow(10.0, 3.0)
                    val x_conv = Math.round(x_gyro * scale) / scale
                    val y_conv = Math.round(y_gyro * scale) / scale
                    val z_conv = Math.round(z_gyro * scale) / scale
                    Log.d("Sensor Gyro", "$x_conv + $y_conv + $z_conv")
                    mSensorViewModel.addSensorGyro(ModelGyro(0, x_conv, y_conv, z_conv))
                    runOnUiThread {
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

    private fun sensor() {
        handler = Handler(Looper.getMainLooper())
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        mSensorViewModel = ViewModelProvider(this).get(SensorViewModel::class.java)

        mSensorViewModel.readAllSensorGyro.observe(this, {
            Log.d("Jumlah Sensor", it.size.toString())
            if (it.size >= 20) {
                GlobalScope.launch {
                    val arrayXacc = mSensorViewModel.arrayXAcc()
                    val arrayYacc = mSensorViewModel.arrayYAcc()
                    val arrayZacc = mSensorViewModel.arrayZAcc()
                    Log.d("Sensor X_ACC AVG ", arrayXacc.average().toString())
                    Log.d("Sensor X_ACC STD ", arrayXacc.standardDeviation().toString())
                    Log.d("Sensor X_ACC MIN ", arrayXacc.minOrNull().toString())
                    Log.d("Sensor X_ACC MAX ", arrayXacc.maxOrNull().toString())
                    Log.d("Sensor X_ACC MEDIAN ", arrayXacc.median().toString())
                    Log.d("Sensor X_ACC SKEWNESS ", arrayXacc.skewness.toString())
                    Log.d("Sensor X_ACC KURTOSIS ", arrayXacc.kurtosis.toString())

                    Log.d("Sensor X_ACC VARIANCE ", arrayXacc.variance().toString())
                    val Q1X = arrayXacc.percentile(25.0)
                    val Q3X = arrayXacc.percentile(75.0)
                    val iqX = Q3X - Q1X
                    Log.d("Sensor X_ACC Q1", Q1X.toString())
                    Log.d("Sensor X_ACC Q3", Q3X.toString())
                    Log.d("Sensor X_ACC IQ", iqX.toString())

                    Log.d("Sensor Y_ACC AVG ", arrayYacc.average().toString())
                    Log.d("Sensor Y_ACC STD ", arrayYacc.standardDeviation().toString())
                    Log.d("Sensor Y_ACC MIN ", arrayYacc.minOrNull().toString())
                    Log.d("Sensor Y_ACC MAX ", arrayYacc.maxOrNull().toString())
                    Log.d("Sensor Y_ACC MEDIAN ", arrayYacc.median().toString())
                    Log.d("Sensor Y_ACC SKEWNESS ", arrayYacc.skewness.toString())
                    Log.d("Sensor Y_ACC KURTOSIS ", arrayYacc.kurtosis.toString())

                    Log.d("Sensor Y_ACC VARIANCE ", arrayYacc.variance().toString())
                    val Q1Y = arrayXacc.percentile(25.0)
                    val Q3Y = arrayXacc.percentile(75.0)
                    val iqY = Q3Y - Q1Y
                    Log.d("Sensor Y_ACC Q1", Q1Y.toString())
                    Log.d("Sensor Y_ACC Q3", Q3Y.toString())
                    Log.d("Sensor Y_ACC IQ", iqY.toString())

                    Log.d("Sensor Z_ACC AVG ", arrayZacc.average().toString())
                    Log.d("Sensor Z_ACC STD ", arrayZacc.standardDeviation().toString())
                    Log.d("Sensor Z_ACC MIN ", arrayZacc.minOrNull().toString())
                    Log.d("Sensor Z_ACC MAX ", arrayZacc.maxOrNull().toString())
                    Log.d("Sensor Z_ACC MEDIAN ", arrayZacc.median().toString())
                    Log.d("Sensor Z_ACC SKEWNESS ", arrayZacc.skewness.toString())
                    Log.d("Sensor Z_ACC KURTOSIS ", arrayZacc.kurtosis.toString())
                    Log.d("Sensor Z_ACC VARIANCE ", arrayZacc.variance().toString())
                    val Q1Z = arrayZacc.percentile(25.0)
                    val Q3Z = arrayZacc.percentile(75.0)
                    val iqZ = Q3Z - Q1Z
                    Log.d("Sensor Z_ACC Q1", Q1Z.toString())
                    Log.d("Sensor Z_ACC Q3", Q3Z.toString())
                    Log.d("Sensor Z_ACC IQ", iqZ.toString())

                    val arrayXgyro = mSensorViewModel.arrayXGyro()
                    val arrayYgyro = mSensorViewModel.arrayYGyro()
                    val arrayZgyro = mSensorViewModel.arrayZGyro()
                    Log.d("Sensor X_GYRO AVG ", arrayXgyro.average().toString())
                    Log.d("Sensor X_GYRO STD ", arrayXgyro.standardDeviation().toString())
                    Log.d("Sensor X_GYRO MIN ", arrayXgyro.minOrNull().toString())
                    Log.d("Sensor X_GYRO MAX ", arrayXgyro.maxOrNull().toString())
                    Log.d("Sensor X_GYRO MEDIAN ", arrayXgyro.median().toString())
                    Log.d("Sensor X_GYRO SKEWNESS ", arrayXgyro.skewness.toString())
                    Log.d("Sensor X_GYRO KURTOSIS ", arrayXgyro.kurtosis.toString())

                    Log.d("Sensor X_GYRO VARIANCE ", arrayXgyro.variance().toString())
                    val Q1Xgyro = arrayXgyro.percentile(25.0)
                    val Q3Xgyro = arrayXgyro.percentile(75.0)
                    val iqXgyro = Q3Xgyro - Q1Xgyro
                    Log.d("Sensor X_GYRO Q1", Q1Xgyro.toString())
                    Log.d("Sensor X_GYRO Q3", Q3Xgyro.toString())
                    Log.d("Sensor X_GYRO IQ", iqXgyro.toString())
                    //--------

                    Log.d("Sensor Y_GYRO AVG ", arrayYgyro.average().toString())
                    Log.d("Sensor Y_GYRO STD ", arrayYgyro.standardDeviation().toString())
                    Log.d("Sensor Y_GYRO MIN ", arrayYgyro.minOrNull().toString())
                    Log.d("Sensor Y_GYRO MAX ", arrayYgyro.maxOrNull().toString())
                    Log.d("Sensor Y_GYRO MEDIAN ", arrayYgyro.median().toString())
                    Log.d("Sensor Y_GYRO SKEWNESS ", arrayYgyro.skewness.toString())
                    Log.d("Sensor Y_GYRO KURTOSIS ", arrayYgyro.kurtosis.toString())

                    Log.d("Sensor Y_GYRO VARIANCE ", arrayXgyro.variance().toString())
                    val Q1Ygyro = arrayYgyro.percentile(25.0)
                    val Q3Ygyro = arrayYgyro.percentile(75.0)
                    val iqYgyro = Q3Ygyro - Q1Ygyro
                    Log.d("Sensor Y_GYRO Q1", Q1Ygyro.toString())
                    Log.d("Sensor Y_GYRO Q3", Q3Ygyro.toString())
                    Log.d("Sensor Y_GYRO IQ", iqYgyro.toString())

                    //------
                    Log.d("Sensor Z_GYRO AVG ", arrayZgyro.average().toString())
                    Log.d("Sensor Z_GYRO STD ", arrayZgyro.standardDeviation().toString())
                    Log.d("Sensor Z_GYRO MIN ", arrayZgyro.minOrNull().toString())
                    Log.d("Sensor Z_GYRO MAX ", arrayZgyro.maxOrNull().toString())
                    Log.d("Sensor Z_GYRO MEDIAN ", arrayZgyro.median().toString())
                    Log.d("Sensor Z_GYRO SKEWNESS ", arrayZgyro.skewness.toString())
                    Log.d("Sensor Z_GYRO KURTOSIS ", arrayZgyro.kurtosis.toString())

                    Log.d("Sensor Z_GYRO VARIANCE ", arrayZgyro.variance().toString())
                    val Q1Zgyro = arrayZgyro.percentile(25.0)
                    val Q3Zgyro = arrayZgyro.percentile(75.0)
                    val iqZgyro = Q3Zgyro - Q1Zgyro
                    Log.d("Sensor Z_GYRO Q1", Q1Zgyro.toString())
                    Log.d("Sensor Z_GYRO Q3", Q3Zgyro.toString())
                    Log.d("Sensor Z_GYRO IQ", iqZgyro.toString())


                    mSensorViewModel.deleteALLSensor()
                }
            }
        })
    }
    override fun onResume() {
        super.onResume()
        handler!!.post(processSensors)
//        sensorManager.registerListener(
//            accDetector,
//            sensorAcc,
//            SensorManager.SENSOR_DELAY_NORMAL
//        )
//        sensorManager.registerListener(
//            gyroDetector,
//            sensorGyro,
//            SensorManager.SENSOR_DELAY_NORMAL
//        )
    }

    override fun onPause() {
        super.onPause()
        handler!!.removeCallbacks(processSensors)
        sensorManager.unregisterListener(accDetector)
        sensorManager.unregisterListener(gyroDetector)
    }
}