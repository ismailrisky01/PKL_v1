package com.example.pkl_v1

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pkl_v1.ml.*
import com.example.pkl_v1.util.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.nio.ByteBuffer
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), SensorEventListener {
    var state: State = State.STAND
    private lateinit var sensorManager: SensorManager
    private var delay = true
    private var handler: Handler? = null
    private val dataSets = arrayListOf<DataSet>()

    private val processSensors: Runnable = object : Runnable {
        override fun run() {
            // Do work with the sensor values.
            delay = true
            // The Runnable is posted to run again here:
            handler!!.postDelayed(this, 500)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.nav_host_fragment)
        bottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.dashboardFragment || destination.id == R.id.profileFragment || destination.id == R.id.notifFragment) {
                bottomNavigation.visibility = View.VISIBLE
            } else {
                bottomNavigation.visibility = View.GONE

            }
        }
//        createNotificationChannel()
        handler = Handler(Looper.getMainLooper())
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        onStartRecord()

    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "fosaa"
            val desc = "Alarm"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("Hallo", name, importance)
            channel.description = desc
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }

    }
    fun onStartRecord() {
        machineLearning = MachineLearning(baseContext)

        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL,
            SensorManager.SENSOR_DELAY_UI
        )
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
            SensorManager.SENSOR_DELAY_NORMAL,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    private fun onStopRecord() {

        sensorManager.unregisterListener(this)

    }

    var acc = Vector(0f, 0f, 0f)
    var gyro = Vector(0f, 0f, 0f)
    val stack = Stack()
    lateinit var machineLearning: MachineLearning

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> acc =
                Vector(event.values[0], event.values[1], event.values[2])
            Sensor.TYPE_GYROSCOPE -> gyro =
                Vector(event.values[0], event.values[1], event.values[2])
        }
        val dataset = DataSet(acc, gyro, state)
        dataSets.add(dataset)
        stack.insert(dataset)
        val result = machineLearning.predict(stack.getArray())
        when (result.toString()) {
            "WALK" -> addWalk(result.toString())
            "STAND" -> addStand(result.toString())
            "SIT" -> addSit(result.toString())
            "SLEEP" -> addSleep(result.toString())
        }
    }

    fun addWalk(result: String) {
        Log.d("Activity", result.toString())
        val myPref = SharedPref(this)
        myPref.setWalk(myPref.getWalk() + 1)
    }

    fun addStand(result: String) {
        Log.d("Activity", result.toString())
        val myPref = SharedPref(this)
        myPref.setStand(myPref.getStand() + 1)
    }

    fun addSit(result: String) {
        Log.d("Activity", result.toString())

        val myPref = SharedPref(this)
        myPref.setSit(myPref.getSit() + 1)
    }

    fun addSleep(result: String) {
        Log.d("Activity", result.toString())

        val myPref = SharedPref(this)
        myPref.setRun(myPref.getRun() + 1)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onPause() {
        super.onPause()
        handler!!.removeCallbacks(processSensors)
        onStopRecord()
    }

    override fun onResume() {
        super.onResume()
        handler!!.post(processSensors)
    }
}

data class Vector(val x: Float, val y: Float, val z: Float)

data class DataSet(var accelerometer: Vector, var gyroscope: Vector, val state: State)

enum class State(val category: Int) { UNKNOWN(0), SIT(1), STAND(2), WALK(3), SLEEP(4) }

class Stack {
    private val arraylist = ArrayList<Array<Float>>().apply {
        for (i in 0 until 32) add(arrayOf(0f, 0f, 0f, 0f, 0f, 0f, 1f))
    }

    private fun convert(item: DataSet): Array<Float> {
        return arrayOf(
            item.accelerometer.x, item.accelerometer.y, item.accelerometer.z,
            item.gyroscope.x, item.gyroscope.y, item.gyroscope.z,
            item.state.category.toFloat()
        )
    }

    fun insert(item: DataSet) {
        if (arraylist.count() >= 32)
            arraylist.removeFirst()
        arraylist.add(convert(item))
    }

    fun getArray(): Array<Array<Float>> {
        return arraylist.toTypedArray()
    }
}


fun createBuffer(dataset: Array<Array<Float>>): ByteBuffer {
    val buffer = ByteBuffer.allocateDirect(dataset.size * dataset[0].size * 4)
    for (data in dataset) for (value in data) buffer.putFloat(Random().nextFloat())
    return buffer
}


class MachineLearning(context: Context) {
    private val model = Model5.newInstance(context)
    private val inputLayer = TensorBuffer.createFixedSize(intArrayOf(32, 7), DataType.FLOAT32)

    fun predict(dataset: Array<Array<Float>>): State? {
        inputLayer.loadBuffer(createBuffer(dataset), intArrayOf(32, 7))
        val output = model.process(inputLayer).outputFeature0AsTensorBuffer
        val list = arrayOf(
            output.getFloatValue(0),
            output.getFloatValue(1),
            output.getFloatValue(2),
            output.getFloatValue(3),
            output.getFloatValue(4)
        )
        val value = list.indices.maxByOrNull { list[it] } ?: 0
        return State.values().find { it.category == value }
    }
}
