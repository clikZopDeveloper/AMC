package com.example.amc_app.Activity

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amc_app.Adapter.CustomerListAdapter
import com.example.amc_app.ApiHelper.ApiController
import com.example.amc_app.ApiHelper.ApiResponseListner
import com.example.amc_app.Model.CustomerListBean
import com.example.amc_app.R
import com.example.amc_app.Utills.*

import com.example.amc_app.databinding.ActivityCustomerListBinding

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class AllCustomerTypeActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityCustomerListBinding
    private lateinit var apiClient: ApiController
    var myReceiver: ConnectivityListener? = null
    var activity: Activity = this
    private  var editReamrk : TextInputEditText?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_list)

        if (SalesApp.isEnableScreenshort==true){
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }
        binding.igToolbar.tvTitle.text = "All Customer"
     //   intent.getStringExtra("customerType")?.let { apiCustomerTypeList(it) }
        apiCustomerTypeList()

    }

    fun apiCustomerTypeList() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
       // params["customer_type"] = custmType
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getCustomer, params)
    }

    fun handleCustomerList(data: List<CustomerListBean.Data>) {
        binding.rcTeamContactList.layoutManager = LinearLayoutManager(this)
        var mAdapter = CustomerListAdapter(this, data, object :
            RvStatusComplClickListner {
            override fun clickPos(status: String,workstatus: String,amt: String, id: Int) {
           /*     startActivity(
                    Intent(
                        this@AllCustomerTypeActivity,
                        CustomerDetailActivity::class.java
                    ).putExtra("cust_ID", id.toString())
                )*/
            }
        })
        binding.rcTeamContactList.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    fun dialog(status: String,ids: Int) {
        val builder = AlertDialog.Builder(this@AllCustomerTypeActivity)
        builder.setMessage("Are you sure you want to start service?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // Delete selected note from database

           //     apiAccept(status,ids)
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()

    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.getCustomer) {
                val customerListBean = apiClient.getConvertIntoModel<CustomerListBean>(
                    jsonElement.toString(),
                    CustomerListBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (customerListBean.error==false) {
                    handleCustomerList(customerListBean.data)
                }

            }

        }catch (e:Exception){
            Log.d("error>>",e.localizedMessage)
        }
    }

    fun dialogRemark(status: String, workstatus: String, ids: Int) {
        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialog)
            .create()
        val dialog = layoutInflater.inflate(R.layout.dialog_reamrk,null)

        builder.setView(dialog)

        builder.setCanceledOnTouchOutside(false)
        builder.show()
        /*    val dialog: Dialog = GeneralUtilities.openBootmSheetDailog(
                R.layout.dialog_update_client, R.style.AppBottomSheetDialogTheme,
                this
            )*/
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        editReamrk = dialog.findViewById<TextInputEditText>(R.id.editReamrk) as TextInputEditText

        val btnSubmit = dialog.findViewById<TextView>(R.id.btnSubmit) as TextView

        ivClose.setOnClickListener {  builder.dismiss() }
        btnSubmit.setOnClickListener {
            builder.dismiss()
            if (editReamrk?.text.isNullOrEmpty()){
                Toast.makeText(this,"Enter Remark",Toast.LENGTH_SHORT).show()
            }else{
          //      apiAccept(status,ids)
            }

        }

    }

    override fun failure(tag: String?, errorMessage: String) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(this, errorMessage)
    }

  /*  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK && requestCode==101){
            val leadStatus: String = data?.getStringExtra("leadStatus")!!
            Log.d("zxczc",leadStatus)
            apiGetGeneratedPI()
        }

    }*/

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
     //   startService(Intent(this, LocationService::class.java))
    }

}
