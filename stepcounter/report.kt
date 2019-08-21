package com.example.stepcounter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_report.*
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream


class report : AppCompatActivity() {

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
        setContentView(R.layout.activity_report)

        val fileStep = File(filesDir, "StepData")
        val fileStair = File(filesDir, "StairData")
        fileInput(fileStep, stepMap)
        fileInput(fileStair, stairMap)

//        val intent = intent
//        val extras = intent.extras
//        val stepMap = extras.getSerializable("stepMap") as HashMap<String,ArrayList<Float>>
//        val stairMap = extras.getSerializable("stepMap") as HashMap<String,ArrayList<Float>>

        var keyX = "x"
        var keyY = "y"
        var keyZ = "z"

        var stepX = stepMap[keyX]!!
        var stepY = stepMap[keyY]!!
        var stepZ = stepMap[keyZ]!!
        var stairX = stairMap[keyX]!!
        var stairY = stairMap[keyY]!!
        var stairZ = stairMap[keyZ]!!
        Log.i("values", "stepX = ${stepMap[keyX]}")
        Log.i("values", "stepX = $stepX")
        Log.i("values", "stepY = ${stepMap[keyY]}")
        Log.i("values", "stepZ = ${stepMap[keyZ]}")
        Log.i("values", "stairX = ${stairMap[keyX]}")
        Log.i("values", "stairY = ${stairMap[keyY]}")
        Log.i("values", "stairZ = ${stairMap[keyZ]}")


        val avgStepX = stepX.min()!! / stepX.size
        Log.i("values", "$avgStepX")
        val avgStepY = stepY.max()!! / stepY.size
        Log.i("values", "$avgStepY")
        val avgStepZ = stepZ.max()!! / stepZ.size
            Log.i("values", "$avgStepZ")
        val avgStairX = stairX.max()!! / stairX.size
        Log.i("values", "$avgStairX")
        val avgStairY = stepY.max()!! / stairY.size
        Log.i("values", "$avgStairY")
        val avgStairZ = stairZ.max()!! / stairZ.size
        Log.i("values", "$avgStairZ")

        rawvalues.setOnCheckedChangeListener{ _, isChecked->
         if (isChecked){
             spinnerX.visibility = View.VISIBLE
             spinnerX2.visibility = View.VISIBLE
             spinnerY.visibility = View.VISIBLE
             spinnerY2.visibility = View.VISIBLE
             spinnerZ.visibility = View.VISIBLE
             spinnerZ2.visibility = View.VISIBLE
             textView5.visibility = View.VISIBLE
             textView6.visibility = View.VISIBLE
             textView7.visibility = View.VISIBLE
             textView8.visibility = View.VISIBLE
             textView9.visibility = View.VISIBLE
             textView10.visibility = View.VISIBLE
         } else {
             spinnerX.visibility = View.INVISIBLE
             spinnerX2.visibility = View.INVISIBLE
             spinnerY.visibility = View.INVISIBLE
             spinnerY2.visibility = View.INVISIBLE
             spinnerZ.visibility = View.INVISIBLE
             spinnerZ2.visibility = View.INVISIBLE
             textView5.visibility = View.INVISIBLE
             textView6.visibility = View.INVISIBLE
             textView7.visibility = View.INVISIBLE
             textView8.visibility = View.INVISIBLE
             textView9.visibility = View.INVISIBLE
             textView10.visibility = View.INVISIBLE
         }
        }

        stats.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                minX.text = "Min X: ${stepX.min()}"
                minX.visibility = View.VISIBLE
                minX2.text = "Min X: ${stairX.min()}"
                minX2.visibility = View.VISIBLE
                minY.text = "Min Y: ${stepY.min()}"
                minY.visibility = View.VISIBLE
                minY2.text = "Min Y: ${stairY.min()}"
                minY2.visibility = View.VISIBLE
                minZ.text = "Min Z: ${stepZ.min()}"
                minZ.visibility = View.VISIBLE
                minZ2.text = "Min Z: ${stairZ.min()}"
                minZ2.visibility = View.VISIBLE
                maxX.text = "Max X: ${stepX.max()}"
                maxX.visibility = View.VISIBLE
                maxX2.text = "Max X: ${stairX.max()}"
                maxX2.visibility = View.VISIBLE
                maxY.text = "Max Y: ${stepY.max()}"
                maxY.visibility = View.VISIBLE
                maxY2.text = "Max Y: ${stairY.max()}"
                maxY2.visibility = View.VISIBLE
                maxZ.text = "Max Z: ${stepZ.max()}"
                maxZ.visibility = View.VISIBLE
                maxZ2.text = "Max Z: ${stairZ.max()}"
                maxZ2.visibility = View.VISIBLE
                averageX.text = "Avg X: $avgStepX"
                averageX.visibility = View.VISIBLE
                averageY.text = "Avg Y: $avgStepY"
                averageY.visibility = View.VISIBLE
                averageZ.text = "Avg Z: $avgStepZ"
                averageZ.visibility = View.VISIBLE
                avgX.text = "Avg X: $avgStairX"
                avgX.visibility = View.VISIBLE
                avgY.text = "Avg Y: $avgStairY"
                avgY.visibility = View.VISIBLE
                avgZ.text = "Avg Z: $avgStairZ"
                avgZ.visibility = View.VISIBLE
            }
            else{
                minX.visibility = View.INVISIBLE
                minX2.visibility = View.INVISIBLE
                minY.visibility = View.INVISIBLE
                minY2.visibility = View.INVISIBLE
                minZ.visibility = View.INVISIBLE
                minZ2.visibility = View.INVISIBLE
                maxX.visibility = View.INVISIBLE
                maxX2.visibility = View.INVISIBLE
                maxY.visibility = View.INVISIBLE
                maxY2.visibility = View.INVISIBLE
                maxZ.visibility = View.INVISIBLE
                maxZ2.visibility = View.INVISIBLE
                averageX.visibility = View.INVISIBLE
                averageY.visibility = View.INVISIBLE
                averageZ.visibility = View.INVISIBLE
                avgX.visibility = View.INVISIBLE
                avgY.visibility = View.INVISIBLE
                avgZ.visibility = View.INVISIBLE
            }
        }

        returnTo.setOnClickListener {
            returnTo
            if (returnTo.isPressed) {
//                val intent = Intent(this, MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                startActivity(intent)
                val intent = Intent(this, MainActivity::class.java)
                val extras = Bundle()
                extras.putSerializable("stepMap", stepMap)
                extras.putSerializable("stairMap", stairMap)
                intent.putExtras(extras)
                startActivity(intent)
            }
        }

        spinnerX.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stepX)
        spinnerX2.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stairX)
        spinnerY.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stepY)
        spinnerY2.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stairY)
        spinnerZ.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stepZ)
        spinnerZ2.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stairZ)

        fun spin (list : Spinner){
            list.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                }
            }
        }
        spin(spinnerX)
        spin(spinnerX2)
        spin(spinnerY)
        spin(spinnerY2)
        spin(spinnerZ)
        spin(spinnerZ2)
    }

    fun fileInput(file: File, entireMap: HashMap<String, ArrayList<Float>>){
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
}

