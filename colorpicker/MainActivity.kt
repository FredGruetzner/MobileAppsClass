//Found String formatting color here
// https://medium.com/@skydoves/how-to-implement-color-picker-in-android-61d8be348683
//Spinner tutorial here
//https://www.youtube.com/watch?v=D5l7MNlqA3M
//Object Serialization
//https://stonesoupprogramming.com/2017/11/25/kotlin-object-serialization/
package com.example.colorpicker


import android.app.Activity
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import android.widget.*
import kotlin.collections.HashMap
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.AdapterView
import java.io.*
import android.content.Intent







class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logo.setImageResource(R.drawable.logo)
        var check = 0
        var rp = 0
        var bp = 0
        var gp = 0
        var myRgb = 0
        var myRgbHex = ""
        var color_list = mutableListOf("")
        var color_map: HashMap<String, String> = hashMapOf()
        val file = File(filesDir, "colorList")
        if(file.exists()){
        ObjectInputStream(FileInputStream(file)).use { it ->
            val newMap = it.readObject() as HashMap<String, String>
            Log.i("newMap", "newMap values ${newMap.values}")
            when (newMap) {
                //We can't use <String, String> because of type erasure
                is Map<*, *> -> newMap.toMap(color_map)

                else -> Log.i("error" ,"Deserialization failed")
           }
            color_list.addAll(color_map.keys)
        }
        }
        else{
            file.createNewFile()
        }

        Red.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(
                seekBar: SeekBar, progress: Int,
                fromUser: Boolean
            ) {
                rp = Red.progress
                textView5.text = "$rp"
                //surfaceView.setBackgroundColor(Color.rgb(rp, gp, bp))
                myRgb = (rp * 65536) + (gp * 256) + bp
                myRgbHex = "#" + String.format("%06X", 0xFFFFFF and myRgb)
                surfaceView.setBackgroundColor(Color.parseColor(myRgbHex))
            }
        })

        Green.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(
                seekBar: SeekBar, progress: Int,
                fromUser: Boolean
            ) {
                gp = Green.progress
                textView6.text = "$gp"
                //surfaceView.setBackgroundColor(Color.rgb(rp, gp, bp))
                myRgb = (rp * 65536) + (gp * 256) + bp
                myRgbHex = "#" + String.format("%06X", 0xFFFFFF and myRgb)
                surfaceView.setBackgroundColor(Color.parseColor(myRgbHex))
            }
        })

        Blue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(
                seekBar: SeekBar, progress: Int,
                fromUser: Boolean
            ) {
                bp = Blue.progress
                textView7.text = "$bp"
                //surfaceView.setBackgroundColor(Color.rgb(rp, gp, bp))
                myRgb = (rp * 65536) + (gp * 256) + bp
                myRgbHex = "#" + String.format("%06X", 0xFFFFFF and myRgb)
                surfaceView.setBackgroundColor(Color.parseColor(myRgbHex))
            }
        })

        saveButton.setOnClickListener {
            saveButton
            if (saveButton.isPressed) {
                if (colorEntry.text != null && !colorEntry.text.isEmpty()) {
                    var entry = colorEntry.text.toString()
                    color_list.add(entry)
                    color_list.sort()
                    color_map.put(entry, myRgbHex)
                    color_map.toSortedMap()
                    val toast = Toast.makeText(applicationContext, myRgbHex, Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    Log.i("colorMap", "color_map values ${color_map.values} + ${color_map.keys}")
                    ObjectOutputStream(FileOutputStream(file)).writeObject(color_map)
                } else {
                    val toast = Toast.makeText(
                        applicationContext,
                        "Please enter a Color Name",
                        Toast.LENGTH_SHORT
                    )
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                }
            }
        }
        colorList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, color_list)

        colorList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (++check > 1) {
                    var picked = color_map.get(color_list[position])
                    val toast = Toast.makeText(applicationContext, picked, Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    surfaceView.setBackgroundColor(Color.parseColor(picked))
                }
            }
        }

        fun finish() {
            // put return color information in a new Intent's extras
            val returnIntent = Intent()
            returnIntent.putExtra("color", myRgbHex)
            setResult(Activity.RESULT_OK, returnIntent)
            Log.i("color", "hex is $returnIntent")
            super.finish()
        }

        val info = intent.extras
        if (info != null) {
            send.visibility = View.VISIBLE
            send.setOnClickListener { send
                if(send.isPressed){
                    finish()
                }
            }
        }
    }
}


