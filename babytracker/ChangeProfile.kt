package com.example.babytracker

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.content_change_profile.*
import java.io.*
import java.nio.file.Files.createFile
import java.nio.file.Files.newBufferedReader

class ChangeProfile : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var light: Sensor? = null
    private var profileList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)
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
        var profileFile = File(filesDir, "profileList")
        if (!profileFile.exists()) {
            profileFile.createNewFile()
            profileList.add("Create New Profile")
            Log.i("values", "$profileList")
            ObjectOutputStream(FileOutputStream(profileFile)).writeObject(profileList)
        }
        else{
            ObjectInputStream(FileInputStream(profileFile)).use { it ->
                val newList =it.readObject() as ArrayList<String>
                Log.i("values" ,"newList values $newList")
                when (newList){
                    is ArrayList<*> -> profileList.addAll(newList)
                }
                Log.i("values", "profileList values $profileList")
            }
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, profileList)
        profileList1.adapter = adapter
        profileList1.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            if (pos == 0){
                Log.i("values", "$pos")
                val name = "new"
                var ep = Intent(this, EditProfile::class.java)
                ep.putExtra("profileName", name)
                ObjectOutputStream(FileOutputStream(profileFile)).writeObject(profileList)
//                ep.putExtra("profileList", profileList)
                Log.i("values", "name = $name")
                Log.i("values", "$profileList")
                Log.i("values", "${intent.putExtra("profileName",name)}")
                startActivity(ep)
            }
            else{
                Log.i("values", "$pos")
                val name = profileList[pos]
                Log.i("values", "$name")
                var t = Intent(this, Tracker::class.java)
                t.putExtra("profileName", name)
                startActivity(t)
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
        menuInflater.inflate(R.menu.change_porfile, menu)
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
//                var t = Intent(this, Tracker::class.java)
//                startActivity(t)
            }
            R.id.nav_report -> {
//                var r = Intent(this, Report::class.java)
//                startActivity(r)
            }
            R.id.nav_editProfile -> {
//                var ep = Intent(this, EditProfile::class.java)
//                startActivity(ep)
            }
            R.id.nav_changeProfile -> {
//                var cp = Intent(this, ChangeProfile::class.java)
//                startActivity(cp)
            }
            R.id.nav_camera -> {
//                var c = Intent(this, Cam::class.java)
//                startActivity(c)
            }
            R.id.nav_gallery -> {
//                var g = Intent(this, SlideGallery::class.java)
//                startActivity(g)
            }
            R.id.nav_speaker -> {
//                var s = Intent(this, Speaker::class.java)
//                startActivity(s)
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
