package com.example.amc_app.Activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amc_app.Adapter.AllExpensesAdapter
import com.example.amc_app.ApiHelper.ApiController
import com.example.amc_app.ApiHelper.ApiResponseListner
import com.example.amc_app.Model.AllExpensesBean
import com.example.amc_app.R

import com.example.amc_app.Utills.ConnectivityListener
import com.example.amc_app.Utills.GeneralUtilities
import com.example.amc_app.Utills.RvStatusClickListner
import com.example.amc_app.Utills.SalesApp
import com.example.amc_app.Utills.Utility
import com.example.amc_app.databinding.ActivityExpensesListBinding

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants
import java.util.*

class AllExpensesActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityExpensesListBinding
    private lateinit var apiClient: ApiController

    var fromDate = ""
    var toDate = ""
    var myReceiver: ConnectivityListener? = null
    var activity: Activity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expenses_list)
        if (SalesApp.isEnableScreenshort == true) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            );
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

            intent.getStringExtra("thisMonth")?.let {
                binding.tvThisMonthExpenses.text=ApiContants.currency+it
            }
            intent.getStringExtra("lastMonth")?.let {
                binding.tvLastMonthExpenses.text=ApiContants.currency+it
            }

        binding.apply {
            igToolbar.tvTitle.text = "Expenses"
            igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
            igToolbar.ivMenu.setOnClickListener { finish() }
            igToolbar.ivFillter.visibility = View.GONE
        }

        binding.ivFilter.setOnClickListener {
            openFilterDialog()
        }

        //tvWalletAmt
        apiAllExpenses()
    }

    fun apiAllExpenses() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["from_date"] = fromDate
        params["to_date"] = toDate
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getExpenses, params)

    }

    fun handleExpenses(data: List<AllExpensesBean.Data>) {
        if (data.size > 0) {
            binding.animationView1.visibility = View.GONE
            binding.rcOfficeTeam.visibility = View.VISIBLE
        } else {
            binding.animationView1.visibility = View.VISIBLE
            binding.rcOfficeTeam.visibility = View.GONE
        }
        binding.rcOfficeTeam.layoutManager = LinearLayoutManager(this)
        var mAdapter = AllExpensesAdapter(this, data, object :
            RvStatusClickListner {
            override fun clickPos(status: String, id: Int) {
                /* startActivity(
                     Intent(
                         this@AllExpensesActivity,
                         AllExpnImagesActivity::class.java
                     ).putExtra("id",id.toString())
                 )
 */

            }
        })
        binding.rcOfficeTeam.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.getExpenses) {
                val officeTeamBean = apiClient.getConvertIntoModel<AllExpensesBean>(
                    jsonElement.toString(),
                    AllExpensesBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (officeTeamBean.error == false) {
                    handleExpenses(officeTeamBean.data)
                }

            }
        } catch (e: Exception) {
            Log.d("error>>", e.localizedMessage)
        }


    }

    fun openFilterDialog() {
        val dialog: Dialog = GeneralUtilities.openBootmSheetDailog(
            R.layout.dialog_filter, R.style.AppBottomSheetDialogTheme,
            this
        )
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        val editDate = dialog.findViewById<View>(R.id.editDate) as EditText
        val editToDate = dialog.findViewById<View>(R.id.editToDate) as EditText
        val btnSearch = dialog.findViewById<View>(R.id.btnSearch) as TextView

        btnSearch.setOnClickListener {
            dialog.dismiss()
            apiAllExpenses()
        }

        editDate.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                this@AllExpensesActivity,
                { view, year, monthOfYear, dayOfMonth ->
                    //  dob.setText(dateofnews);
                    val dateofnews =
                        "${year.toString() + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth.toString()}"

                    //   val dateofnews = (monthOfYear + 1).toString() + "/" + dayOfMonth + "/" + year
                    editDate.setText(dateofnews)
                    fromDate = dateofnews
                },
                year, month, day
            )
            datePickerDialog.show()
        })

        editToDate.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                this@AllExpensesActivity,
                { view, year, monthOfYear, dayOfMonth ->
                    //  dob.setText(dateofnews);
                    val dateofnews =
                        "${year.toString() + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth.toString()}"

                    //   val dateofnews = (monthOfYear + 1).toString() + "/" + dayOfMonth + "/" + year
                    editToDate.setText(dateofnews)
                    toDate = dateofnews
                },
                year, month, day
            )
            datePickerDialog.show()
        })

        ivClose.setOnClickListener { dialog.dismiss() }
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
        // startService(Intent(this, LocationService::class.java))
    }
}
