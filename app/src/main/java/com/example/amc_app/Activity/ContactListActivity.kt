package com.example.amc_app.Activity

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amc_app.Adapter.ContactListAdapter
import com.example.amc_app.ApiHelper.ApiController
import com.example.amc_app.ApiHelper.ApiResponseListner
import com.example.amc_app.Model.ContactListBean
import com.example.amc_app.R
import com.example.amc_app.Utills.*

import com.example.amc_app.databinding.ActivityCustomerListBinding

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class ContactListActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityCustomerListBinding
    private lateinit var apiClient: ApiController
    var myReceiver: ConnectivityListener? = null
    private lateinit var mAllAdapter: ContactListAdapter
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
        binding.igToolbar.tvTitle.text = "Contact List"
      //  intent.getStringExtra("customerType")?.let { apiCustomerTypeList(it) }
      //  intent.getStringExtra("status_id")?.let {  }
        apiTeamContactList()
    }

    fun apiTeamContactList() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getContactList, params)

    }

    fun handleContactList(data: List<ContactListBean.Data>) {
        binding.rcTeamContactList.layoutManager = LinearLayoutManager(this)
        mAllAdapter = ContactListAdapter(this, data, object :
            RvStatusClickListner {
            override fun clickPos(status: String ,id: Int) {

            }
        })
        binding.rcTeamContactList.adapter = mAllAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false
        binding.rcTeamContactList.isNestedScrollingEnabled = false
        mAllAdapter.notifyDataSetChanged()

        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (data != null) {
                    mAllAdapter.filter.filter(s)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                mAllAdapter.filter.filter(s)
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mAllAdapter.filter.filter(s)
                /* if (s.toString().trim { it <= ' ' }.length < 1) {
                     ivClear.visibility = View.GONE
                 } else {
                     ivClear.visibility = View.GONE
                 }*/
            }
        })
    }

    fun dialog(status: String,ids: Int) {
        val builder = AlertDialog.Builder(this@ContactListActivity)
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
            if (tag == ApiContants.getContactList) {
                val contactListBean = apiClient.getConvertIntoModel<ContactListBean>(
                    jsonElement.toString(),
                    ContactListBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (contactListBean.error==false) {
                    handleContactList(contactListBean.data)
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
