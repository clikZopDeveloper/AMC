package com.example.amc_app.Activity

import android.app.Activity
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amc_app.Adapter.ProjectDetailListAdapter
import com.example.amc_app.ApiHelper.ApiController
import com.example.amc_app.ApiHelper.ApiResponseListner
import com.example.amc_app.Model.MultipleProductBean
import com.example.amc_app.Model.ProjectDetailBean
import com.example.amc_app.R
import com.example.amc_app.Utills.*

import com.example.amc_app.databinding.ActivityProjectDetailBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class ProjectDetailActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {

    private lateinit var binding: ActivityProjectDetailBinding
    private lateinit var apiClient: ApiController
    var myReceiver: ConnectivityListener? = null
    var list: MutableList<MultipleProductBean> = ArrayList()
    var activity: Activity = this
    var customerID=0
    var orderID=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_project_detail)
        if (SalesApp.isEnableScreenshort==true){
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        binding.igToolbar.tvTitle.text = "Project Detail"
        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }

        intent.getStringExtra("project_id")?.let { apiProjectDetail(it) }
            //  orderID= intent.getStringExtra("project_id").toString()


    }

    fun apiProjectDetail(projectID: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["id"] = projectID
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getProjectsDetails, params)
    }

    fun handleOrderDetailList(
        leadProduct: List<ProjectDetailBean.Data.ProjectService>
    ) {
        binding.rcCommentList.layoutManager = LinearLayoutManager(this)
       val  mAdapter = ProjectDetailListAdapter(this, leadProduct, object :
            RvCreateOrderClickListner {
            override fun clickPos(status: List<MultipleProductBean>, id: Int) {
               // list.add(status)
              //  apiCreateReturnOrder()
            }
        })

        binding.rcCommentList.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false
    }


    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.getProjectsDetails) {
                val projectDetailBean = apiClient.getConvertIntoModel<ProjectDetailBean>(
                    jsonElement.toString(),
                    ProjectDetailBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (projectDetailBean.error==false) {
                    binding.apply {
                        tvProjectName.setText(projectDetailBean.data.projectName+"("+projectDetailBean.data.projectSubName+")")
                        tvAMCCost.setText(projectDetailBean.data.amcCost.toString())
                        tvPaymentStatus.setText(projectDetailBean.data.paymentStatus.toString())
                        tvProjectTech.setText(projectDetailBean.data.projectTechnology.toString())
                        tvProjectType.setText(projectDetailBean.data.projectType.toString())
                        tvStatus.setText(projectDetailBean.data.status.toString())
                        tvProjectDate.setText(projectDetailBean.data.deliveryDate.toString())


                        tvPaymentStatus.text = projectDetailBean.data.paymentStatus.toString()
                        if (projectDetailBean.data.paymentStatus.toString().equals("Pending"))
                            tvPaymentStatus.setTextColor(getResources().getColor(R.color.yellow_color));
                        else
                            tvPaymentStatus.setTextColor(getResources().getColor(R.color.green));


                        if (projectDetailBean.data.status.toString().equals("Active"))
                            tvStatus.background.colorFilter =
                                BlendModeColorFilter(
                                    getResources().getColor(R.color.green),
                                    BlendMode.SRC_ATOP
                                )

                        /*
                        else if (list[position].paymentStatus.toString().equals("Success"))
                             tvStatus.background.colorFilter =
                                 BlendModeColorFilter(
                                     context.getResources().getColor(R.color.green),
                                     BlendMode.SRC_ATOP
                                 )
                                 */

                        else
                            tvStatus.background.colorFilter =
                                BlendModeColorFilter(
                                    getResources().getColor(R.color.yellow_color),
                                    BlendMode.SRC_ATOP
                                )

                    }
                /*    binding.tvName.setText(projectDetailBean.data.orderMst.customerName)
                    binding.tvOrderValue.setText(ApiContants.currency+projectDetailBean.data.orderMst.orderValue)
                    binding.tvOrderDate.setText(projectDetailBean.data.orderMst.orderDate)
                    binding.tvStaus.setText(projectDetailBean.data.orderMst.status)
                     customerID=projectDetailBean.data.orderMst.customerId
*/
                    handleOrderDetailList(projectDetailBean.data.projectService)

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
