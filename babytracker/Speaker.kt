//Bluetooth functionally at youTube video series
// https://www.youtube.com/watch?v=Oz4CBHrxMMs&pbjreload=10
// https://www.youtube.com/watch?v=1QO3TOobci8
//https://stackoverflow.com/questions/37638665/broadcastreceiver-for-bluetooth-device-discovery-works-on-one-device-but-not-on
package com.example.babytracker

import android.Manifest
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.MacAddress
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.content_speaker.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.slide_gallery.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Speaker : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var light: Sensor? = null
    var requestEnableBT =1
    // Get the default adapter
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val dlist : ArrayList<String> = ArrayList()
    private val mapList : HashMap<BluetoothDevice, String> = HashMap()
//    private val MY_UUID : UUID = UUID.fromString("6ac71353-86e8-4bcd-bc0f-5f43c6a23b33")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speaker)
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

        checkBlue()
        if(bluetoothAdapter!!.isEnabled){
            OnOff.isChecked = true
        }
        pairList()
        refresh.setOnClickListener {
            pairList()
        }
        OnOff.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                checkBlue()
            }
            else{
                bluetoothAdapter.disable()
            }
        }
        pair.setOnClickListener {
            Log.i("values", "got here")
            var requestCode = 1
            val permissionsToRequest = arrayOf(
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            ActivityCompat.requestPermissions(this, permissionsToRequest, requestCode)
            Log.i("values", "got here next")
            bluetoothAdapter.startDiscovery()
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)

    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            Log.i("values", "got here too")
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    Log.i("values", "and here")
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    Log.i("values", "and here")
                    if(device.name.isNullOrEmpty()){
                        val toast = Toast.makeText(this@Speaker, "No Pairing Devices Found", Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                    }
                    else {
                        dlist.add(device.name)
//                        mapList[device] = device.address
//                        Log.i("values", "devices $dlist")
////                        device.createBond()
//                        popList(dlist)
//                    val deviceHardwareAddress = device.address // MAC address
                        val adapter = ArrayAdapter(this@Speaker, android.R.layout.simple_list_item_1, dlist)
                        pairList.adapter = adapter
                        pairList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                            device.createBond()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver)
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

    private fun checkBlue(){
        if(bluetoothAdapter == null){
            Toast.makeText(this, "Device does not suppose Bluetooth", Toast.LENGTH_LONG).show()
            var t = Intent(this, Tracker::class.java)
            startActivity(t)
        }
        else if (!bluetoothAdapter?.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, requestEnableBT)
        }
        else{
            val toast = Toast.makeText(this, "Bluetooth is on, Please connect device", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    private fun pairList(){
        val list : ArrayList<String> = ArrayList()
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        if(pairedDevices!!.isNotEmpty()) {
            pairedDevices.forEach { device ->
                list.add(device.name)
            }
        }
        else{
            val toast = Toast.makeText(this, "No Paired Devices Found", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
        popList(list)
    }

    private fun popList(list: ArrayList<String>){
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        pairList.adapter = adapter
        pairList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device : String = list[position]
        }
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
