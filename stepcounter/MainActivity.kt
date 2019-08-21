//Sensor Info from https://expertise.jetruby.com/how-to-implement-motion-sensor-in-a-kotlin-app-b70db1b5b8e5

package com.example.stepcounter

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    var x = 0.0f
    var y = 0.0f
    var z = 0.0f
    var keyX = "x"
    var keyY = "y"
    var keyZ = "z"
    private var arrayX = ArrayList<Float>()
    private var arrayY = ArrayList<Float>()
    private var arrayZ = ArrayList<Float>()
    private var stepMap: HashMap<String, ArrayList<Float>> = hashMapOf()
    private var stairMap: HashMap<String, ArrayList<Float>> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        stepMap[keyX] = arrayX
//        stepMap[keyY] = arrayY
//        stepMap[keyZ] = arrayZ
//        stairMap[keyX] = arrayX
//        stairMap[keyY] = arrayY
//        stairMap[keyZ] = arrayZ
        val fileStep = File(filesDir, "StepData")
        val fileStair = File(filesDir, "StairData")
        fileInput(fileStep, stepMap)
        fileInput(fileStair, stairMap)
        step.setOnCheckedChangeListener { _, isChecked ->
            this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            var accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            if (isChecked) {
                //start sensor
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            } else {
                //save data
                sensorManager.unregisterListener(this@MainActivity, accelerometer)
                saveData(fileStep, stepMap, arrayX, arrayY, arrayZ)
//                arrayX.clear()
//                arrayY.clear()
//                arrayZ.clear()
            }
        }

        stairs.setOnCheckedChangeListener { _, isChecked ->
            this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            var accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            if (isChecked) {
                //start
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            } else {
                //save data
                sensorManager.unregisterListener(this@MainActivity, accelerometer)
                saveData(fileStair, stairMap, arrayX, arrayY, arrayZ)
//                arrayX.clear()
//                arrayY.clear()
//                arrayZ.clear()
            }
        }

        reportButton.setOnClickListener {
            reportButton
            if (reportButton.isPressed) {
                // start your next activity
//                val mIntent = Intent(this@MainActivity, report::class.java)
//                val vIntent = Intent(this@MainActivity, report::class.java)
//                mIntent.putExtra("stepMap", stepMap)
//                vIntent.putExtra("stairMap", stairMap)
//                startActivity(mIntent)
                val intent = Intent(this, report::class.java)
                val extras = Bundle()
//                Log.i("values", "stepMap send = {$stepMap}")
//                Log.i("values", "stepMap send = {$stairMap")
//                extras.putSerializable("stepMap", stepMap)
//                extras.putSerializable("stairMap", stairMap)
//                intent.putExtras(extras)
                startActivity(intent)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                x += event.values[0]
                Log.i("values", "x = $x")
                arrayX.add(x)
                y += event.values[1]
                Log.i("values", "y = $y")
                arrayY.add(y)
                z += event.values[2]
                Log.i("values", "z = $z")
                arrayZ.add(z)
            }
        }
    }

    private fun fileInput(file: File, entireMap: HashMap<String, ArrayList<Float>>){
        if(file.exists()){
            ObjectInputStream(FileInputStream(file)).use { it ->
                val newMap = it.readObject() as HashMap<String, ArrayList<Float>>
                Log.i("newMap", "newMap values ${newMap.values}")
                Log.i("newMap", "arrayX values $arrayX")
                Log.i("newMap", "arrayY values $arrayY")
                Log.i("newMap", "arrayZ values $arrayZ")
                when (newMap) {
                    //We can't use <String, String> because of type erasure
                    is Map<*, *> -> newMap.toMap(entireMap)

                    else -> Log.i("error" ,"Deserialization failed")
                }
                arrayX = entireMap[keyX]!!
                arrayY = entireMap[keyY]!!
                arrayZ = entireMap[keyZ]!!
                Log.i("newMap", "newMap values ${newMap.values}")
                Log.i("newMap", "arrayX values $arrayX")
                Log.i("newMap", "arrayY values $arrayY")
                Log.i("newMap", "arrayZ values $arrayZ")
            }
        }
        else{
            file.createNewFile()
        }
    }

    private fun saveData(
        file: File, entireMap: HashMap<String, ArrayList<Float>>,
        arrayX: ArrayList<Float>, arrayY: ArrayList<Float>, arrayZ: ArrayList<Float>){
        entireMap[keyX] = arrayX
        entireMap[keyY] = arrayY
        entireMap[keyZ] = arrayZ
        Log.i("values", "Here the old arrayX save = $arrayX")
        Log.i("values", "Here the old Map save = $entireMap")
        ObjectOutputStream(FileOutputStream(file)).writeObject(entireMap)
        arrayX.clear()
        arrayY.clear()
        arrayZ.clear()
        Log.i("values", "Here the new arrayX save= $arrayX")
        Log.i("values", "Here the new Map save= $entireMap")
    }
}

