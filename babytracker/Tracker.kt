//audio help from https://stackoverflow.com/questions/9461270/media-player-looping-android
//https://medium.com/@dairdr/kotlin-playing-audio-file-3eeaca0d3cb1
//Larsen from class
//https://mc2method.org/white-noise/

package com.example.babytracker

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.widget.Toast
import kotlinx.android.synthetic.main.content_edit_profile.*
import kotlinx.android.synthetic.main.content_tracker.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.io.*
import java.sql.Timestamp

class Tracker : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var light: Sensor? = null
    private var mediaPlayer:MediaPlayer? = null
    private var wet = "wet"
    private var solid = "solid"
    private var diaper = "diaper"
    private var feed = "feed"
    private var tumTime = "tumTime"
    private var diaperData: HashMap<String, String> = hashMapOf()
    private var feedData: HashMap<String, String> = hashMapOf()
    private var tTime: HashMap<String, String> = hashMapOf()
    private var profileDataMap: HashMap<String, HashMap<String, String>> = hashMapOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracker)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        val new:String = intent.getStringExtra("profileName")
        var check = new.isNullOrEmpty()
        Log.i("values", "check is $check")
//        textView.text = new
        Log.i("values", "trackerNew is $new")
        var profileData = File(filesDir, "data$new")
        if(!profileData.exists()){
            profileData.createNewFile()
        }
        else{
            ObjectInputStream(FileInputStream(profileData)).use { it ->
                val newMap =it.readObject() as HashMap<String, HashMap<String, String>>
                Log.i("values" ,"newList values $newMap")
                when (newMap){
                    is HashMap<*,*> -> newMap.toMap(profileDataMap)
                }
                Log.i("values", "profileData values $profileDataMap")
            }
            diaperData = profileDataMap[diaper]!!
            feedData = profileDataMap[feed]!!
            tTime = profileDataMap[tumTime]!!
        }

        wetPlus.setOnClickListener {
            if(diaperData[wet].isNullOrEmpty()){
                diaperData[wet] = 1.toString()
            }
            else{
                var temp = diaperData[wet]?.toInt()
                temp = temp?.plus(1)
                diaperData[wet] = temp.toString()
                Log.i("values", "wetPlus is ${diaperData[wet]}")
            }
        }
        button2.setOnClickListener {
            if(diaperData[wet].isNullOrEmpty()){
                Toast.makeText(this, "Counter at 0", Toast.LENGTH_LONG).show()
            }
            else{
                var temp =diaperData[wet]?.toInt()
                temp = temp?.minus(1)
                diaperData[wet] = temp.toString()
            }
        }

        solidPlus.setOnClickListener {
            if(diaperData[solid].isNullOrEmpty()){
                diaperData[solid] = 1.toString()
            }
            else{
                var temp = diaperData[solid]?.toInt()
                temp = temp?.plus(1)
                diaperData[solid] = temp.toString()
                Log.i("values", "wetPlus is ${diaperData[solid]}")
            }
        }
        solidMinus.setOnClickListener {
            if(diaperData[solid].isNullOrEmpty()){
                Toast.makeText(this, "Counter at 0", Toast.LENGTH_LONG).show()
            }
            else{
                var temp =diaperData[solid]?.toInt()
                temp = temp?.minus(1)
                diaperData[solid] = temp.toString()
            }
        }
        save.setOnClickListener {
            var curTime = Timestamp(System.currentTimeMillis()).toString()
            feedData.put(curTime, feedEdit.text.toString())
            tTime.put(curTime, tTime1.text.toString())
            profileDataMap[diaper] = diaperData
            profileDataMap[feed] = feedData
            profileDataMap[tumTime] = tTime
            var profileData = File(filesDir, "data$new")
            ObjectOutputStream(FileOutputStream(profileData)).writeObject(profileDataMap)
        }

        whiteNoise.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                mediaPlayer = MediaPlayer()
                mediaPlayer = MediaPlayer.create(this, R.raw.vacuum)
//                mediaPlayer?.prepare()
                mediaPlayer?.start()
                mediaPlayer?.isLooping = true
        }
            else{
                mediaPlayer?.stop()
            }
        }

    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.tracker, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_tracker -> {
                val new:String = intent.getStringExtra("profileName")
                var t = Intent(this, Tracker::class.java)
                t.putExtra("profileName", new)
                startActivity(t)
            }
            R.id.nav_report -> {
                val new:String = intent.getStringExtra("profileName")
                var r = Intent(this, Report::class.java)
                r.putExtra("profileName", new)
                startActivity(r)
            }
            R.id.nav_editProfile -> {
                val new:String = intent.getStringExtra("profileName")
                var ep = Intent(this, EditProfile::class.java)
                ep.putExtra("profileName", new)
                startActivity(ep)
            }
            R.id.nav_changeProfile -> {
                val new:String = intent.getStringExtra("profileName")
                var cp = Intent(this, ChangeProfile::class.java)
                cp.putExtra("profileName", new)
                startActivity(cp)
            }
            R.id.nav_camera -> {
                val new:String = intent.getStringExtra("profileName")
                var c = Intent(this, Cam::class.java)
                c.putExtra("profileName", new)
                startActivity(c)
            }
            R.id.nav_gallery -> {
                val new:String = intent.getStringExtra("profileName")
                var g = Intent(this, SlideGallery::class.java)
                g.putExtra("profileName", new)
                startActivity(g)
            }
            R.id.nav_speaker -> {
                val new:String = intent.getStringExtra("profileName")
                var s = Intent(this, Speaker::class.java)
                s.putExtra("profileName", new)
                startActivity(s)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        var lightValue = event!!.values[0]
        Log.i("values", "light value is $lightValue")
        if (lightValue > 20){
            delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            delegate.applyDayNight()
        }
        else{
            delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}
