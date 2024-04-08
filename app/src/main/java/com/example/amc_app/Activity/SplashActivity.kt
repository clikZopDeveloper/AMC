package com.example.amc_app.Activity

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

import com.example.amc_app.Utills.ConnectivityListener
import com.example.amc_app.Utills.GeneralUtilities
import com.example.amc_app.Utills.PrefManager
import com.example.amc_app.Utills.SalesApp
import com.example.amc_app.databinding.ActivitySplashBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.stpl.antimatter.Utils.ApiContants
import com.stpl.antimatter.Utils.ApiContants.Companion.isconnectedtonetwork


class SplashActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivitySplashBinding
    var refresher: CountDownTimer? = null
    private var refreshRate = 300000L //5min
    private var refresherDuration = 780000L //12min
    var myReceiver: ConnectivityListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    //    setContentView(R.layout.activity_splash)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (SalesApp.isEnableScreenshort==true){
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        myReceiver = ConnectivityListener()
      //  val animation4 = AnimationUtils.loadAnimation(this, com.example.kk_services.R.anim.zoom)
        //GeneralUtilities.getInstance().setStatusBarColor(SplashActivity.this, ContextCompat.getColor(context, R.color.colorPrimaryDark));
     //   binding.image.startAnimation(animation4)
     //   initNotificationRefresher()
      // GeneralUtilities.launchActivity(this, FirbaseOTPAuthActivity::class.java)


    /*    Handler(Looper.getMainLooper()).postDelayed({
            // callNextActivity()
            if (PrefManager.getString(ApiContants.Type, "") == "Success") {
                GeneralUtilities.launchActivity(this, WebviewActivity::class.java)
                finishAffinity()
            } else {
                GeneralUtilities.launchActivity(this, LoginNewActivity::class.java)
                //   GeneralUtilities.launchActivity(this, WebviewActivity::class.java)
                finishAffinity()
            }

        }, 2200)
*/
        Handler(Looper.getMainLooper()).postDelayed({
            // callNextActivity()
            if (PrefManager.getString(ApiContants.AccessToken, "") != "") {
                GeneralUtilities.launchActivity(this, DashboardActivity::class.java)
                finishAffinity()
            } else {
                GeneralUtilities.launchActivity(this, LoginActivity::class.java)
             //   GeneralUtilities.launchActivity(this, WebviewActivity::class.java)
                finishAffinity()
            }

        }, 2200)

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        GeneralUtilities.unregisterBroadCastReceiver(this, myReceiver)
    }

    override fun onResume() {
        GeneralUtilities.registerBroadCastReceiver(this, myReceiver)
        SalesApp.setConnectivityListener(this)
        super.onResume()
    }

    override fun onNetworkConnectionChange(isconnected: Boolean) {
        isconnectedtonetwork = isconnected
        GeneralUtilities.internetConnectivityAction(this, isconnected)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    fun initNotificationRefresher(){
        refresher = object : CountDownTimer(refresherDuration, refreshRate) {
            override fun onTick(millisUntilFinished: Long) {
               Toast.makeText(this@SplashActivity,"Call Code",Toast.LENGTH_SHORT).show()
            }
            override fun onFinish() {
                initNotificationRefresher()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Start the LocationService when the app is closed
    //    startService(Intent(this, LocationService::class.java))
    }
}