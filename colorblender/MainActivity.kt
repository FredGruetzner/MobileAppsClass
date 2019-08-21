package com.example.colorblender

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.graphics.ColorUtils
import android.view.Gravity
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    var color1 = 0
    var color2 = 0
    var color3hex = ""
    var setColor1 =  ""
    var setColor2 = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val color = Intent("com.example.colorpicker.ACTION_COLOR")
        button.setOnClickListener {
            button
            if (button.isPressed) {
                startActivityForResult(color.putExtra("color", ""), 1)
            }
        }

        button2.setOnClickListener { button2
            if (button2.isPressed){
                startActivityForResult(color.putExtra("color", ""), 2)
            }
        }

        blendBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(
                seekBar: SeekBar, progress: Int,
                fromUser: Boolean
            ) {
                if(!setColor1.isEmpty() && !setColor2.isEmpty()){
                    setColor1 = setColor1.removePrefix("#")
                    color1 = setColor1.toInt(16)
                    setColor2 = setColor2.removePrefix("#")
                    color2 = setColor2.toInt(16)
                    var ratio = (blendBar.progress.toFloat())/100
                    var color3 = ColorUtils.blendARGB(color1, color2, ratio)
                    color3hex = "#" + String.format("%06X", 0xFFFFFF and color3)
                    blendedColor.setBackgroundColor(Color.parseColor(color3hex))
                }
                else{
                    Log.i("min", "else")
                    val toast = Toast.makeText(applicationContext, "Please select two colors first", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //val info = data?.extras
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            if (data != null) {
                setColor1 = data.getStringExtra("color")
            }
            color1surface.setBackgroundColor(Color.parseColor(setColor1))
            Log.i("onActivityResult", "color is $setColor1")
        }
        else {
            if (data != null) {
                setColor2 = data.getStringExtra("color")
            }
            color2surface.setBackgroundColor(Color.parseColor(setColor2))
            Log.i("onActivityResult", "color is $setColor2")
        }
    }
}
