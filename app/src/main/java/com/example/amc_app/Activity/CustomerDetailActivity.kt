package com.example.amc_app.Activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amc_app.Adapter.CustomerInterstedAdapter
import com.example.amc_app.Adapter.CustomerPurchaseAdapter
import com.example.amc_app.Adapter.HisListAdapter
import com.example.amc_app.Adapter.LeadCommentListAdapter
import com.example.amc_app.ApiHelper.ApiController
import com.example.amc_app.ApiHelper.ApiResponseListner
import com.example.amc_app.Model.CustomerDetailBean
import com.example.amc_app.R
import com.example.amc_app.Utills.*

import com.example.amc_app.databinding.ActivityCustDetailBinding

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class CustomerDetailActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityCustDetailBinding
    private lateinit var apiClient: ApiController
    var myReceiver: ConnectivityListener? = null
    var activity: Activity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cust_detail)
        if (SalesApp.isEnableScreenshort==true){
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        binding.igToolbar.tvTitle.text = "Customer Detail"
        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }

        intent.getStringExtra("cust_ID")?.let { apiCustomerDetail(it) }

    }

    fun apiCustomerDetail(cust_id: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["customer_id"] = cust_id
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getCustomerData, params)

    }

    fun handleCustInterestedCat(data: List<CustomerDetailBean.Data.CustomerInterestedCategory>) {
        if (data.size>0){
            binding.rcInterseted.visibility=View.VISIBLE
            binding.tvInterseted.visibility=View.VISIBLE
        }else{
            binding.rcInterseted.visibility=View.GONE
            binding.tvInterseted.visibility=View.GONE
        }
        binding.rcInterseted.layoutManager = GridLayoutManager(this,3)
        var mAdapter = CustomerInterstedAdapter(this,"Detail", data, object :
            RvListClickListner {
            override fun clickPos(status: MutableList<Any?>?, pos: Int) {
             //   catListID=status
            }
        })
        binding.rcInterseted.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false
    }

    fun handleCustPurchaseCat(data: List<CustomerDetailBean.Data.CustomerPurchasedCategory>) {
        if (data.size>0){
            binding.rcPurchase.visibility=View.VISIBLE
            binding.tvPurchase.visibility=View.VISIBLE
        }else{
            binding.rcPurchase.visibility=View.GONE
            binding.tvPurchase.visibility=View.GONE
        }
        binding.rcPurchase.layoutManager = GridLayoutManager(this,3)
        var mAdapter = CustomerPurchaseAdapter(this, "Detail", data, object :
            RvListClickListner {
            override fun clickPos(status: MutableList<Any?>?, pos: Int) {
              //  catListID=status
            }
        })
        binding.rcPurchase.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    fun handleLeadCommentList(
        leadProduct: List<CustomerDetailBean.Data.LeadComment>
    ) {
        binding.rcCommentList.layoutManager = LinearLayoutManager(this)
        var mAdapter = LeadCommentListAdapter(this, leadProduct, object :
            RvStatusClickListner {
            override fun clickPos(status: String, pos: Int) {

            }
        })
        binding.rcCommentList.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    fun handleLeadHisList(leadHistory: List<CustomerDetailBean.Data.LeadHistory>) {
        binding.rcHisList.layoutManager = LinearLayoutManager(this)
        var mAdapter = HisListAdapter(this, leadHistory, object :
            RvStatusClickListner {
            override fun clickPos(status: String, pos: Int) {
            }
        })
        binding.rcHisList.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }
    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.getCustomerData) {
                val customrDetailBean = apiClient.getConvertIntoModel<CustomerDetailBean>(
                    jsonElement.toString(),
                    CustomerDetailBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (customrDetailBean.error==false) {
                    binding.tvName.setText(customrDetailBean.data.customers.name)
                    binding.tvEmail.setText(customrDetailBean.data.customers.email)
                    binding.tvMobileNo.setText(customrDetailBean.data.customers.phone)
                    binding.tvWhatsappNo.setText(customrDetailBean.data.customers.whatsapp)
                    binding.tvDOB.setText(customrDetailBean.data.customers.dob)
                    binding.tvDOA.setText(customrDetailBean.data.customers.doa)
                    binding.tvState.setText(customrDetailBean.data.customers.state)
                    binding.tvCity.setText(customrDetailBean.data.customers.city)
                    binding.tvAddress.setText(customrDetailBean.data.customers.address)
                    binding.tvCustType.setText(customrDetailBean.data.customers.customerType)
                    binding.tvSource.setText(customrDetailBean.data.customers.source)

                    handleCustPurchaseCat(customrDetailBean.data.customerPurchasedCategory)
                    handleCustInterestedCat(customrDetailBean.data.customerInterestedCategory)
                    handleLeadCommentList(customrDetailBean.data.leadComments)
                    handleLeadHisList(customrDetailBean.data.leadHistory)
/*
                    for (name in 0 until customrDetailBean.data.storeHistory.storeName.size) {
                        litst1?.add(name.toString())
                    }
                    for (name in 0 until customrDetailBean.data.storeHistory.category.size) {
                        litst2?.add(name.toString())
                    }

                    litstfinal?.addAll(litst1!!)
                    litstfinal?.addAll(litst2!!)

                 Log.d("zxzx",Gson().toJson(litst1))
                 Log.d("zxzx",Gson().toJson(litst2))
                 Log.d("zxzx",Gson().toJson(litstfinal))*/
                  //  handleLeadHisList(customrDetailBean.data.storeHistory.storeName)
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
    //    startService(Intent(this, LocationService::class.java))
    }
}
