// Code to get Image from vision project
package com.example.babytracker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import kotlinx.android.synthetic.main.activity_slide_gallery.*
import kotlinx.android.synthetic.main.content_slide_gallery.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.slide_gallery.*


class SlideGallery : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var light: Sensor? = null
    private var bitmap: Bitmap? = null
    private var contentURI: Uri = Uri.EMPTY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_gallery)
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
        getAnImage()
        newImage.setOnClickListener {
            getAnImage()
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
        menuInflater.inflate(R.menu.main, menu)
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

    /**
     * Build an Intent to retrieve an image from the gallery
     * The image is returned in onActivityResult()
     */
    private fun getAnImage() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    /**
     * Called by the Android system when the image's URI is returned from the Gallery
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }
        if (data != null) {
            contentURI = data.data
            Log.i("values", "file path = $contentURI")
            showImage(contentURI)
        }
    }

    private fun showImage(uri:Uri){
        val bmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val wid = bmap.width
        val factor = wid / GOAL_SIZE    //amount to reduce picture by
        bitmap = Bitmap.createScaledBitmap(bmap,bmap.width/factor, bmap.height/factor, false)
        the_image.setImageBitmap(bitmap)
    }

    companion object {
        private const val GALLERY = 7734
        private const val GOAL_SIZE = 400       //good image width size for quick response from Google
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
