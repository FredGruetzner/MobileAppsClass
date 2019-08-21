package com.example.babytracker

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
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
import android.graphics.drawable.Drawable
import android.text.Editable
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.content_edit_profile.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.io.*


class EditProfile : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var light: Sensor? = null
    private var name = "name"
    private var date = "date"
    private var time = "time"
    private var weight = "weight"
    private var height = "height"
    private var image = "image"
    private var profileInfo: HashMap<String, String> = hashMapOf()
    private var profileList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
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
//        textView.text = new
//        profileList = intent.getStringArrayListExtra("profileList")
        var file3 = File(filesDir, "profileList")
        ObjectInputStream(FileInputStream(file3)).use{
            profileList = it.readObject() as ArrayList<String>
        }
        if (new == "new"){

        }
        else {
            var file1 = File(filesDir, "profile$new")
            Log.i("values", "$file1")
            if (file1.exists()) {
                ObjectInputStream(FileInputStream(file1)).use { it ->
                    val newMap = it.readObject() as HashMap<String, String>
                    when (newMap) {
                        is Map<*, *> -> newMap.toMap(profileInfo)

                        else -> Log.i("error", "Deserialization failed")
                    }
                    Log.i("values", "newList values $profileInfo")
                    childName.setText(profileInfo[name].toString())
                    enterDate.setText(profileInfo[date].toString())
                    enterTime.setText(profileInfo[time].toString())
                    enter_Weight.setText(profileInfo[weight].toString())
                    enter_Height.setText(profileInfo[height].toString())
                }
            }
            else{
                Toast.makeText(this, "profile does not exist", Toast.LENGTH_LONG).show()
            }
        }

        save1.setOnClickListener {
            profileInfo.put(name , childName.text.toString())
            Log.i("values", "child's name ${profileInfo[name]}")
            profileInfo.put(date , enterDate.text.toString())
            profileInfo.put(time , enterTime.text.toString())
            profileInfo.put(weight , enter_Weight.text.toString())
            profileInfo.put(height , enter_Height.text.toString())
            profileList.add(profileInfo[name].toString())
            Log.i("values", "profileList from EP $profileList")
            Log.i("values", "profile map from EP ${profileInfo[time]}")
//            val name  = profileInfo[name].toString()
            val name = childName.text.toString()
            Log.i("values", "$name")
            var file = File(filesDir, "profile$name")
            var file1 = File(filesDir, "profileList")
            Log.i("values", "filename $file")
            ObjectOutputStream(FileOutputStream(file)).writeObject(profileInfo)
            ObjectOutputStream(FileOutputStream(file1)).writeObject(profileList)
            var t = Intent(this, Tracker::class.java)
            intent.putExtra("profileName", name)
            startActivity(t)
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
        menuInflater.inflate(R.menu.edit_profile, menu)
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
                val name : String = profileInfo[name].toString()
                var t = Intent(this, Tracker::class.java)
                t.putExtra("profileName", name)
                startActivity(t)
            }
            R.id.nav_report -> {
                val name : String = profileInfo[name].toString()
                var r = Intent(this, Report::class.java)
                r.putExtra("profileName", name)
                startActivity(r)
            }
            R.id.nav_editProfile -> {
                val name : String = profileInfo[name].toString()
                var ep = Intent(this, EditProfile::class.java)
                ep.putExtra("profileName", name)
                startActivity(ep)
            }
            R.id.nav_changeProfile -> {
                val name : String = profileInfo[name].toString()
                var cp = Intent(this, ChangeProfile::class.java)
                cp.putExtra("profileName", name)
                startActivity(cp)
            }
            R.id.nav_camera -> {
                val name : String = profileInfo[name].toString()
                var c = Intent(this, Cam::class.java)
                c.putExtra("profileName", name)
                startActivity(c)
            }
            R.id.nav_gallery -> {
                val name : String = profileInfo[name].toString()
                var g = Intent(this, SlideGallery::class.java)
                g.putExtra("profileName", name)
                startActivity(g)
            }
            R.id.nav_speaker -> {
                val name : String = profileInfo[name].toString()
                var s = Intent(this, Speaker::class.java)
                s.putExtra("profileName", name)
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
