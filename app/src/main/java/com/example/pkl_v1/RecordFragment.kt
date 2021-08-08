package com.example.pkl_v1

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pkl_v1.databinding.FragmentRecordBinding
import com.example.pkl_v1.model.ModelAcc
import com.example.pkl_v1.model.ModelDataset
import com.example.pkl_v1.model.ModelGyro
import com.example.pkl_v1.viewmodel.SensorViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.nield.kotlinstatistics.*
import java.math.RoundingMode
import java.text.DecimalFormat


class RecordFragment : Fragment() {
    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!
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
            handler!!.postDelayed(this, 500)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handler = Handler(Looper.getMainLooper())
        sensor()
        mSensorViewModel = ViewModelProvider(this).get(SensorViewModel::class.java)
        mSensorViewModel.deleteALLSensor()
        mSensorViewModel.readAllSensorGyro.observe(viewLifecycleOwner, {
            Log.d("Jumlah Sensor", it.size.toString())
            if (it.size >= 10) {
                GlobalScope.launch {
                    val arrayXacc = mSensorViewModel.arrayXAcc()
                    val arrayYacc = mSensorViewModel.arrayYAcc()
                    val arrayZacc = mSensorViewModel.arrayZAcc()

                    val arrayXgyro = mSensorViewModel.arrayXGyro()
                    val arrayYgyro = mSensorViewModel.arrayYGyro()
                    val arrayZgyro = mSensorViewModel.arrayZGyro()

                    val xaccavg = arrayXacc.average()
                    val xaccstd = arrayXacc.standardDeviation()
                    val xaccmin = arrayXacc.minOrNull()!!
                    val xaccmax = arrayXacc.maxOrNull()
                    val xaccmedian = arrayXacc.median()
                    val xaccskewness = arrayXacc.skewness
                    val xacckurtosis = arrayXacc.kurtosis
                    val xaccvariance = arrayXacc.variance()
                    val xaccpercentil = arrayXacc.percentile(75.0)
                    val Q1X = arrayXacc.percentile(25.0)
                    val Q3X = arrayXacc.percentile(75.0)
                    val iqX = Q3X - Q1X
                    val xacciq = iqX

                    val yaccavg = arrayYacc.average()
                    val yaccstd = arrayYacc.standardDeviation()
                    val yaccmin = arrayYacc.minOrNull()
                    val yaccmax = arrayYacc.maxOrNull()
                    val yaccmedian = arrayYacc.median()
                    val yaccskewness = arrayYacc.skewness
                    val yacckurtosis = arrayYacc.kurtosis
                    val yaccvariance = arrayYacc.variance()
                    val yaccpercentil = arrayYacc.percentile(75.0)
                    val Q1Y = arrayYacc.percentile(25.0)
                    val Q3Y = arrayYacc.percentile(75.0)
                    val iqY = Q3Y - Q1Y
                    val yacciq = iqY

                    val zaccavg = arrayZacc.average()
                    val zaccstd = arrayZacc.standardDeviation()
                    val zaccmin = arrayZacc.minOrNull()
                    val zaccmax = arrayZacc.maxOrNull()
                    val zaccmedian = arrayZacc.median()
                    val zaccskewness = arrayZacc.skewness
                    val zacckurtosis = arrayZacc.kurtosis
                    val zaccvariance = arrayZacc.variance()
                    val zaccpercentil = arrayZacc.percentile(75.0)
                    val Q1Z = arrayZacc.percentile(25.0)
                    val Q3Z = arrayZacc.percentile(75.0)
                    val iqZ = Q3Z - Q1Z
                    val zacciq = iqZ

                    val xgyroavg = arrayXgyro.average()
                    val xgyrostd = arrayXgyro.standardDeviation()
                    val xgyromin = arrayXgyro.minOrNull()
                    val xgyromax = arrayXgyro.maxOrNull()
                    val xgyromedian = arrayXgyro.median()
                    val xgyroskewness = arrayXgyro.skewness
                    val xgyrokurtosis = arrayXgyro.kurtosis
                    val xgyrovariance = arrayXgyro.variance()
                    val xgyropercentil = arrayXgyro.percentile(75.0)
                    val Q1Xgyro = arrayXgyro.percentile(25.0)
                    val Q3Xgyro = arrayXgyro.percentile(75.0)
                    val iqXgyro = Q3Xgyro - Q1Xgyro
                    val xgyroiq = iqXgyro

                    val ygyroavg = arrayYgyro.average()
                    val ygyrostd = arrayYgyro.standardDeviation()
                    val ygyromin = arrayYgyro.minOrNull()
                    val ygyromax = arrayYgyro.maxOrNull()
                    val ygyromedian = arrayYgyro.median()
                    val ygyroskewness = arrayYgyro.skewness
                    val ygyrokurtosis = arrayYgyro.kurtosis
                    val ygyrovariance = arrayYgyro.variance()
                    val ygyropercentil = arrayXgyro.percentile(75.0)
                    val Q1Ygyro = arrayYgyro.percentile(25.0)
                    val Q3Ygyro = arrayYgyro.percentile(75.0)
                    val iqYgyro = Q3Ygyro - Q1Ygyro
                    val ygyroiq = iqYgyro

                    val zgyroavg = arrayZgyro.average()
                    val zgyrostd = arrayZgyro.standardDeviation()
                    val zgyromin = arrayZgyro.minOrNull()
                    val zgyromax = arrayZgyro.maxOrNull()
                    val zgyromedian = arrayZgyro.median()
                    val zgyroskewness = arrayZgyro.skewness
                    val zgyrokurtosis = arrayZgyro.kurtosis
                    val zgyrovariance = arrayZgyro.variance()
                    val zgyropercentil = arrayZgyro.percentile(75.0)
                    val Q1Zgyro = arrayZgyro.percentile(25.0)
                    val Q3Zgyro = arrayZgyro.percentile(75.0)
                    val iqZgyro = Q3Zgyro - Q1Zgyro
                    val zgyroiq = iqZgyro


                    Log.d("Sensor X_ACC AVG ", arrayXacc.average().toString())
                    Log.d("Sensor X_ACC STD ", arrayXacc.standardDeviation().toString())
                    Log.d("Sensor X_ACC MIN ", arrayXacc.minOrNull().toString())
                    Log.d("Sensor X_ACC MAX ", arrayXacc.maxOrNull().toString())
                    Log.d("Sensor X_ACC MEDIAN ", arrayXacc.median().toString())
                    Log.d("Sensor X_ACC SKEWNESS ", arrayXacc.skewness.toString())
                    Log.d("Sensor X_ACC KURTOSIS ", arrayXacc.kurtosis.toString())

                    Log.d("Sensor X_ACC VARIANCE ", arrayXacc.variance().toString())

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

                    Log.d("Sensor Z_ACC Q1", Q1Z.toString())
                    Log.d("Sensor Z_ACC Q3", Q3Z.toString())
                    Log.d("Sensor Z_ACC IQ", iqZ.toString())


                    Log.d("Sensor X_GYRO AVG ", arrayXgyro.average().toString())
                    Log.d("Sensor X_GYRO STD ", arrayXgyro.standardDeviation().toString())
                    Log.d("Sensor X_GYRO MIN ", arrayXgyro.minOrNull().toString())
                    Log.d("Sensor X_GYRO MAX ", arrayXgyro.maxOrNull().toString())
                    Log.d("Sensor X_GYRO MEDIAN ", arrayXgyro.median().toString())
                    Log.d("Sensor X_GYRO SKEWNESS ", arrayXgyro.skewness.toString())
                    Log.d("Sensor X_GYRO KURTOSIS ", arrayXgyro.kurtosis.toString())

                    Log.d("Sensor X_GYRO VARIANCE ", arrayXgyro.variance().toString())

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

                    Log.d("Sensor Z_GYRO Q1", Q1Zgyro.toString())
                    Log.d("Sensor Z_GYRO Q3", Q3Zgyro.toString())
                    Log.d("Sensor Z_GYRO IQ", iqZgyro.toString())

                    mSensorViewModel.deleteALLSensor()
                    val data = ModelDataset(
                        xaccavg,
                        xaccstd,
                        xaccmin,
                        xaccmax!!,
                        xaccmedian,
                        xaccskewness,
                        xacckurtosis,
                        xaccvariance,
                        xaccpercentil,
                        xacciq,
                        yaccavg,
                        yaccstd,
                        yaccmin!!,
                        yaccmax!!,
                        yaccmedian,
                        yaccskewness,
                        yacckurtosis,
                        yaccvariance,
                        yaccpercentil,
                        yacciq,
                        zaccavg,
                        zaccstd,
                        zaccmin!!,
                        zaccmax!!,
                        zaccmedian,
                        zaccskewness,
                        zacckurtosis,
                        zaccvariance,
                        zaccpercentil,
                        zacciq,
                        xgyroavg,
                        xgyrostd,
                        xgyromin!!,
                        xgyromax!!,
                        xgyromedian,
                        xgyroskewness,
                        xgyrokurtosis,
                        xgyrovariance,
                        xgyropercentil,
                        xgyroiq,
                        ygyroavg,
                        ygyrostd,
                        ygyromin!!,
                        ygyromax!!,
                        ygyromedian,
                        ygyroskewness,
                        ygyrokurtosis,
                        ygyrovariance,
                        ygyropercentil,
                        ygyroiq,
                        zgyroavg,
                        zgyrostd,
                        zgyromin!!,
                        zgyromax!!,
                        zgyromedian,
                        zgyroskewness,
                        zgyrokurtosis,
                        zgyrovariance,
                        zgyropercentil,
                        zgyroiq,label()

                    )
                    sensorManager.unregisterListener(accDetector)
                    sensorManager.unregisterListener(gyroDetector)
                    FirebaseDatabase.getInstance().reference.child("PKL/Dataset").push()
                        .setValue(data).addOnSuccessListener {
                            Toast.makeText(requireContext(), "Succes", Toast.LENGTH_SHORT).show()
                        mSensorViewModel.deleteALLSensor()
                        findNavController().popBackStack()
                    }.addOnFailureListener {
                            Toast.makeText(requireContext(), "Failed"+it.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                }
            }
        })
    }
fun label():String{
    var label = ""
    arguments?.let{
        label= it.getString("key") as String
    }
    return label
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

    override fun onDestroyView() {
        super.onDestroyView()
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