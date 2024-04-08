package com.example.amc_app.Fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amc_app.Activity.CreateMeetingActivity
import com.example.amc_app.Adapter.MettingListAdapter
import com.example.amc_app.ApiHelper.ApiController
import com.example.amc_app.ApiHelper.ApiResponseListner
import com.example.amc_app.Model.CreateMeetingBean
import com.example.amc_app.Model.MettingListBean
import com.example.amc_app.R
import com.example.amc_app.Utills.RvStatusComplClickListner
import com.example.amc_app.Utills.SalesApp
import com.example.amc_app.Utills.Utility
import com.example.amc_app.databinding.FragmentMettingBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants
import java.util.*


class MettingFragment : Fragment(), ApiResponseListner {

    private var _binding: FragmentMettingBinding? = null
    private lateinit var apiClient: ApiController
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val binding get() = _binding!!
    var list: List<Address>? = null
    private var currentLoc: String? = null
    private val permissionId = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentMettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        binding.appbarLayout.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.appbarLayout.ivMenu.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.appbarLayout.tvTitle.text = "Meetings"
        binding.fbAddMeting.setOnClickListener {
            startActivity(Intent(requireContext(),CreateMeetingActivity::class.java))
        }
        apiGetMetting()
        getLocation()
        return root
    }

    fun apiGetMetting() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(requireContext(), this)
        val params = Utility.getParmMap()
      //  apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.GetMettingList,params)

    }
    fun apiUpdateMeeting(remark: String, ids: Int) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(requireContext(), this)
        val params = Utility.getParmMap()
        params["id"] = ids.toString()
        params["remarks"] = remark
        params["stop_location"] =  "${list?.get(0)?.latitude},${list?.get(0)?.longitude}"
        params["meeting_status"] = "stop"
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.GetClientMeeting, params)
    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.GetMettingList) {
                val mettingListBean = apiClient.getConvertIntoModel<MettingListBean>(
                    jsonElement.toString(),
                    MettingListBean::class.java
                )
                if (mettingListBean.error == false) {
                    SalesApp.stateList.clear()
                    handleMettingList(mettingListBean.data)
                }
            }

            if (tag == ApiContants.GetClientMeeting) {
                val UpdateMeetingBean = apiClient.getConvertIntoModel<CreateMeetingBean>(
                    jsonElement.toString(),
                    CreateMeetingBean::class.java
                )
                if (UpdateMeetingBean.error == false) {
                   Toast.makeText(requireContext(),UpdateMeetingBean.msg,Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e:Exception){

        }
    }
    override fun failure(tag: String?, errorMessage: String?) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(requireActivity(), errorMessage!!)
    }
    fun handleMettingList(data: List<MettingListBean.Data>) {
        binding.rcTeamContactList.layoutManager = LinearLayoutManager(requireContext())
        var mAdapter = MettingListAdapter(requireActivity(), data, object :
            RvStatusComplClickListner {
            override fun clickPos(status: String,workstatus: String,amt: String, id: Int) {
                dialogRemark(id)
            }
        })
        binding.rcTeamContactList.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                        list =geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        Log.d("zxxzv", "Lat" + Gson().toJson(list?.get(0)?.latitude))
                        Log.d("zxxzv", "Long" + Gson().toJson(list?.get(0)?.longitude))
                        Log.d("zxxzv", Gson().toJson(list?.get(0)?.countryName))
                        Log.d("zxxzv", Gson().toJson(list?.get(0)?.locality))
                        Log.d("zxxzv", Gson().toJson(list?.get(0)?.getAddressLine(0)))

                        currentLoc = list?.get(0)?.getAddressLine(0)
                        /*    mainBinding.apply {
                                tvLatitude.text = "Latitude\n${list[0].latitude}"
                                tvLongitude.text = "Longitude\n${list[0].longitude}"
                                tvCountryName.text = "Country Name\n${list[0].countryName}"
                                tvLocality.text = "Locality\n${list[0].locality}"
                                tvAddress.text = "Address\n${list[0].getAddressLine(0)}"
                            }*/
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        } else {
            //  checkPermissions()
        }
    }

    fun dialogRemark(ids: Int) {
        val builder = AlertDialog.Builder(requireContext(),R.style.CustomAlertDialog)
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
       val editReamrk = dialog.findViewById<TextInputEditText>(R.id.editReamrk) as TextInputEditText

        val btnSubmit = dialog.findViewById<TextView>(R.id.btnSubmit) as TextView

        ivClose.setOnClickListener {  builder.dismiss() }
        btnSubmit.setOnClickListener {

            if (editReamrk.text.isNullOrEmpty()){
                Toast.makeText(requireContext(),"Enter Remark",Toast.LENGTH_SHORT).show()
            }else{
                builder.dismiss()
                apiUpdateMeeting(editReamrk.text.toString(),ids)
            }

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        apiGetMetting()
    }
}