package com.example.fbs

import android.content.Context
import android.hardware.camera2.CameraManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.media.ToneGenerator
import android.media.AudioManager
import android.os.AsyncTask
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tg = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
        val vb = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        var cameraId = cameraManager.cameraIdList[0]
        var abc = MyTask()
        flash.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                abc.execute()
            }
            else {
                abc.cancel(true)
            }
        }

        bleep.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tg.startTone(ToneGenerator.TONE_DTMF_0)
            } else {
                tg.stopTone()
            }
        }

        shake.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val effect = VibrationEffect.createOneShot(20000, VibrationEffect.DEFAULT_AMPLITUDE)
                vb.vibrate(effect)
            } else {
                vb.cancel()
            }
        }
    }
        private inner class MyTask : AsyncTask<String, Void, String>() {
            override fun doInBackground(vararg params: String?): String {
                val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
                var cameraId = cameraManager.cameraIdList[0]
                var i = 0
                while (true) {
                    if (i % 2 == 0) {
                        cameraManager.setTorchMode(cameraId, true)
                        Thread.sleep(250)
                    }
                        cameraManager.setTorchMode(cameraId, false)
                        Thread.sleep(250)
                        i++
                        Log.i(i.toString(), "i is $i")
                }
                return "Button Pressed"
            }
        }

}
