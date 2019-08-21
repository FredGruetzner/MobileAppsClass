//Camera usage found at https://developer.android.com/training/camera/photobasics
package com.example.babytracker

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.graphics.Bitmap
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
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.io.File
import java.io.IOException

class Cam : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cam)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        val new:String = intent.getStringExtra("profileName")

        navView.setNavigationItemSelectedListener(this)
        dispatchTakePictureIntent()
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
        menuInflater.inflate(R.menu.cam, menu)
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

    private val requestImageCapture = 1

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, requestImageCapture)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestImageCapture && resultCode == RESULT_OK) {
//            val imageBitmap = data?.extras?.get("data") as Bitmap
//            imageView.setImageBitmap(imageBitmap)
//            // Save image to gallery
//            MediaStore.Images.Media.insertImage(
//                contentResolver,
//                imageBitmap,
//                title.toString(), "Image of $title")
            var g = Intent(this, SlideGallery::class.java)
            startActivity(g)
        }
    }


}
