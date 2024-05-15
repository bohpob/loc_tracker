package cz.cvut.fit.poberboh.loc_tracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyCallback
import android.telephony.TelephonyDisplayInfo
import android.telephony.TelephonyManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Main activity class responsible for managing the application's UI and functionality.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var telephonyManager: TelephonyManager
    private lateinit var fiveGIcon: ImageView
    private val requestPermissionsCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the 5G icon view
        fiveGIcon = findViewById(R.id.fiveGIcon)
        fiveGIcon.setImageResource(0)

        // Request necessary permissions if not already granted
        requestPermissionsIfNecessary()

        // Setup TelephonyManager for network monitoring
        setupTelephonyManager()
    }

    /**
     * Set up the TelephonyManager for monitoring network information.
     */
    private fun setupTelephonyManager() {
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        // Register TelephonyCallback for getting network information
        // This is only available on Android 12 (API level 31) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyManager.registerTelephonyCallback(mainExecutor, object : TelephonyCallback(),
                TelephonyCallback.DisplayInfoListener {
                override fun onDisplayInfoChanged(displayInfo: TelephonyDisplayInfo) {
                    updateNetworkIcon(displayInfo.networkType)
                }
            })
        }
    }

    /**
     * Updates the network icon based on the network type.
     * @param state The network type.
     */
    internal fun updateNetworkIcon(state: Int) {
        // Determine if the network is 5G
        val is5GNetwork = when (state) {
            TelephonyManager.NETWORK_TYPE_NR,
            TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_LTE_ADVANCED_PRO,
            TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA,
            TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_ADVANCED -> true

            else -> false
        }

        // Set the icon image based on the network type
        fiveGIcon.setImageResource(if (is5GNetwork) R.drawable.twotone_5g else 0)
    }

    /**
     * Requests necessary permissions if not already granted.
     */
    private fun requestPermissionsIfNecessary() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE
        )

        val permissionsToRequest = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                requestPermissionsCode
            )
        }
    }

    /**
     * Handles the results of permission request.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestPermissionsCode) {
            val allPermissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (!allPermissionsGranted) {
                // Show permission denied message and finish the activity after some delay
                showPermissionDeniedMessageAndFinish()
            }
        }
    }

    /**
     * Shows a toast message for permission denial and finishes the activity after a delay.
     */
    private fun showPermissionDeniedMessageAndFinish() {
        Toast.makeText(this, "Permissions not granted", Toast.LENGTH_LONG).show()
        Handler(Looper.getMainLooper()).postDelayed({ finish() }, 2500)
    }
}