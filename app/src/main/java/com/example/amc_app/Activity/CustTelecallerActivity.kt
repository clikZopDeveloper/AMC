package com.example.amc_app.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amc_app.Adapter.CustTelecallerAdapter
import com.example.amc_app.ApiHelper.ApiController
import com.example.amc_app.ApiHelper.ApiResponseListner
import com.example.amc_app.Model.CustmerTelecallerBean
import com.example.amc_app.R

import com.example.amc_app.Utills.ConnectivityListener
import com.example.amc_app.Utills.GeneralUtilities
import com.example.amc_app.Utills.RvStatusClickListner
import com.example.amc_app.Utills.SalesApp
import com.example.amc_app.Utills.Utility
import com.example.amc_app.databinding.ActivityTeleclerBinding

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class CustTelecallerActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityTeleclerBinding
    private lateinit var apiClient: ApiController
    var myReceiver: ConnectivityListener? = null
    var activity: Activity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_telecler)
        if (SalesApp.isEnableScreenshort==true){
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }
        binding.igToolbar.tvTitle.text = intent.getStringExtra("status_name")?.uppercase()
        intent.getStringExtra("status_id")?.let {  apiTelecalerList(it) }

    }

    fun apiTelecalerList(id: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["status_id"] = id
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getUpdateStatus, params)
    }

    fun handleTelecalerList(data: List<CustmerTelecallerBean.Data>) {
        binding.rcOfficeTeam.layoutManager = LinearLayoutManager(this)
        var mAdapter = CustTelecallerAdapter(this, data, object :
            RvStatusClickListner {
            override fun clickPos(status: String, pos: Int) {
                startActivity(
                    Intent(
                        this@CustTelecallerActivity,
                        CustomerDetailActivity::class.java
                    ).putExtra("cust_ID", pos.toString())
                )
            }
        })
        binding.rcOfficeTeam.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.getUpdateStatus) {
                val officeTeamBean = apiClient.getConvertIntoModel<CustmerTelecallerBean>(
                    jsonElement.toString(),
                    CustmerTelecallerBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (officeTeamBean.error==false) {
                    handleTelecalerList(officeTeamBean.data)
                }

            }
        }catch (e:Exception){
            Log.d("error>>",e.localizedMessage)
        }



    }

    override fun failure(tag: String?, errorMessage: String) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(this, errorMessage)
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
        ApiContants.isconnectedtonetwork = isconnected
        GeneralUtilities.internetConnectivityAction(this, isconnected)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    override fun onDestroy() {
        super.onDestroy()
        // Start the LocationService when the app is closed
      //  startService(Intent(this, LocationService::class.java))
    }
}
