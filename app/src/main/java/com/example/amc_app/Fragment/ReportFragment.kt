package com.example.amc_app.Fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amc_app.Adapter.ProductWiseListAdapter
import com.example.amc_app.ApiHelper.ApiController
import com.example.amc_app.ApiHelper.ApiResponseListner
import com.example.amc_app.Model.*
import com.example.amc_app.R
import com.example.amc_app.Utills.GeneralUtilities
import com.example.amc_app.Utills.RvStatusComplClickListner
import com.example.amc_app.Utills.SalesApp
import com.example.amc_app.Utills.Utility
import com.example.amc_app.databinding.FragmentReportBinding
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ReportFragment : Fragment(), ApiResponseListner {
    private var _binding: FragmentReportBinding? = null
    var statusType = "Customer Wise Report"
    private val binding get() = _binding!!
    private lateinit var apiClient: ApiController
    var customerID = 0
    var toDate = ""
    var fromDate = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.appbarLayout.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.appbarLayout.ivMenu.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.appbarLayout.tvTitle.text = "Report"

        typeMode()

        binding.ivFilter.setOnClickListener {
            dialogFilter()
        }

        val todaydate: LocalDate = LocalDate.now()
        fromDate = LocalDate.now().withDayOfMonth(1).toString()
        System.out.println("Months first date in yyyy-mm-dd: " + LocalDate.now().withDayOfMonth(1))


        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val current = LocalDateTime.now().format(formatter)
        toDate = current.toString()
        System.out.println("yyyy-mm-dd: " + fromDate + "\n" + current)

        apiCustomerWiseReport()
        apiCustomerList()

        return root

    }

    fun apiCustomerList() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(requireContext(), this)
        val params = Utility.getParmMap()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.GetCustomer, params)
    }

    fun apiCustomerWiseReport() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(requireContext(), this)
        val params = Utility.getParmMap()
        params["from_date"] = fromDate
        params["to_date"] = toDate
        params["cust_id"] = customerID.toString()
        //  apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.GetCustomerWiseReport, params)

    }

    fun apiProductWiseReport() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(requireContext(), this)
        val params = Utility.getParmMap()
        params["from_date"] = fromDate
        params["to_date"] = toDate
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.GetProductWiseReport, params)
    }

    fun setCustomerData(data: List<GetCustomerBean.Data>) {
        val state = arrayOfNulls<String>(data.size)
        for (i in data.indices) {
            state[i] = data.get(i).name
        }

        binding.stateCustomer.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1, state
            )
        )

        binding.stateCustomer.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            //  var sourceName = SalesApp.sourceList.get(position).name


            Log.d("StateID", "" + parent.getItemAtPosition(position).toString())
            for (customerBean in data) {
                if (customerBean.name.equals(
                        parent.getItemAtPosition(position).toString()
                    )
                ) {
                    customerID = customerBean.id
                    Log.d("StateID", "" + customerBean.id)

                    binding.handleViewVisible.visibility = View.VISIBLE
                    binding.tvMobNo.setText("Mobile Number : " + customerBean.mobile)
                    binding.tvEmail.setText("Email : " + customerBean.email)
                    binding.stateCustomer.setText(parent.getItemAtPosition(position).toString())

                    if (statusType.equals("Product Wise Report")) {
                        apiProductWiseReport()
                    } else {
                        apiCustomerWiseReport()
                    }
                }
            }
            Toast.makeText(
                requireContext(),
                binding.stateCustomer.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()
            setCustomerData(data)


        })
    }

    fun typeMode() {
        binding.RadioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                if (checkedId == R.id.rbCustomerReport) {
                    binding.rbCustomerReport.setTextColor(getResources().getColor(R.color.black));
                    binding.rbProductReport.setTextColor(getResources().getColor(R.color.white));
                    statusType = "Customer Wise Report"
                    binding.tvTitle.text = "Customer Wise Report"
                    binding.selectCustomer.visibility = View.VISIBLE
                    binding.rcProductWiseList.visibility = View.GONE
                    binding.llCustomerSection.visibility = View.VISIBLE
                    apiCustomerWiseReport()
                } else if (checkedId == R.id.rbProductReport) {
                    binding.rbCustomerReport.setTextColor(getResources().getColor(R.color.white));
                    binding.rbProductReport.setTextColor(getResources().getColor(R.color.black));
                    statusType = "Product Wise Report"
                    binding.tvTitle.text = "Product Wise Report"
                    binding.selectCustomer.visibility = View.GONE
                    binding.rcProductWiseList.visibility = View.VISIBLE
                    binding.llCustomerSection.visibility = View.GONE
                    apiProductWiseReport()
                }
            }
        })
    }

    fun dialogFilter() {
        /*      val builder = AlertDialog.Builder(this,R.style.CustomAlertDialog)
                  .create()
              val dialog = layoutInflater.inflate(R.layout.dialog_reamrk,null)

              builder.setView(dialog)

              builder.setCanceledOnTouchOutside(false)*/
        // builder.show()

        val dialog: Dialog = GeneralUtilities.openBootmSheetDailog(
            R.layout.dialog_filter, R.style.AppBottomSheetDialogTheme,
            requireActivity()
        )
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        val btnSearch = dialog.findViewById<TextView>(R.id.btnSearch) as TextView
        val editDate = dialog.findViewById<EditText>(R.id.editDate) as EditText
        val editToDate = dialog.findViewById<EditText>(R.id.editToDate) as EditText

        ivClose.setOnClickListener { dialog.dismiss() }
        editDate.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    //  dob.setText(dateofnews);
                    val dateofnews =
                        "${year.toString() + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth.toString()}"
                    fromDate = dateofnews
                    editDate.setText(dateofnews)
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
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    //  dob.setText(dateofnews);
                    val dateofnews =
                        "${year.toString() + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth.toString()}"
                    toDate = dateofnews
                    editToDate.setText(dateofnews)
                },
                year, month, day
            )
            datePickerDialog.show()
        })
        btnSearch.setOnClickListener {
            if (editDate.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Select From Date", Toast.LENGTH_SHORT).show()
            } else if (editToDate.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Select To Date", Toast.LENGTH_SHORT).show()
            } else {
                dialog.dismiss()
                if (statusType.equals("Product Wise Report")) {
                    apiProductWiseReport()
                } else {
                    apiCustomerWiseReport()
                }

            }

        }

    }

    fun handleProductWiseList(data: List<ProductWiseReportBean.Data>) {
        binding.rcProductWiseList.layoutManager = LinearLayoutManager(requireContext())
        var mAdapter = ProductWiseListAdapter(requireActivity(), data, object :
            RvStatusComplClickListner {
            override fun clickPos(status: String, workstatus: String, amt: String, id: Int) {
                //  dialogRemark(id)
            }
        })
        binding.rcProductWiseList.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.getCustomer) {
                val getCustomerBean = apiClient.getConvertIntoModel<GetCustomerBean>(
                    jsonElement.toString(),
                    GetCustomerBean::class.java
                )

                if (getCustomerBean.error == false) {
                    setCustomerData(getCustomerBean.data)
                }

            }
            if (tag == ApiContants.GetCustomerWiseReport) {
                val customerWiseReportBean = apiClient.getConvertIntoModel<CustomerWiseReportBean>(
                    jsonElement.toString(),
                    CustomerWiseReportBean::class.java
                )

                if (customerWiseReportBean.error == false) {
                    //  setCustomerData(customerWiseReportBean.data)
                    binding.apply {
                        tvPending.text = customerWiseReportBean.data.pendingCount
                        tvDeliverd.text = customerWiseReportBean.data.deliveredCount
                        tvApproved.text = customerWiseReportBean.data.approvedCount
                        tvRejected.text = customerWiseReportBean.data.rejectedCount
                        tvDispatched.text = customerWiseReportBean.data.dispatchedCount


                        tvAmtPending.text =
                            ApiContants.currency + customerWiseReportBean.data.pendingAmt
                        tvAmtDeliverd.text =
                            ApiContants.currency + customerWiseReportBean.data.deliveredAmt
                        tvAmtApproved.text =
                            ApiContants.currency + customerWiseReportBean.data.approvedAmt
                        tvAmtRejected.text =
                            ApiContants.currency + customerWiseReportBean.data.rejectedAmt
                        tvAmtDispatched.text =
                            ApiContants.currency + customerWiseReportBean.data.dispatchedAmt

                    }
                }

            }

            if (tag == ApiContants.GetProductWiseReport) {
                val productWiseReportBean = apiClient.getConvertIntoModel<ProductWiseReportBean>(
                    jsonElement.toString(),
                    ProductWiseReportBean::class.java
                )

                if (productWiseReportBean.error == false) {
                    Toast.makeText(requireContext(), productWiseReportBean.msg, Toast.LENGTH_SHORT)
                        .show()
                    handleProductWiseList(productWiseReportBean.data)
                }

            }
        } catch (e: Exception) {
            Log.d("error>>", e.localizedMessage)
        }

    }

    override fun failure(tag: String?, errorMessage: String) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(requireActivity(), errorMessage)
        Log.d("error", errorMessage)

    }

}