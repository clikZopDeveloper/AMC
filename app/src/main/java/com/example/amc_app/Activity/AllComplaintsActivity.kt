package com.example.amc_app.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.amc_app.Adapter.AllComplaintsAdapter
import com.example.amc_app.ApiHelper.ApiController
import com.example.amc_app.ApiHelper.ApiResponseListner
import com.example.amc_app.Model.AllComplaintsBean
import com.example.amc_app.R
import com.example.amc_app.Utills.*
import com.example.amc_app.databinding.ActivityAllComplaintsBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


class AllComplaintsActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityAllComplaintsBinding
    private lateinit var apiClient: ApiController
    private  var editReamrk : TextInputEditText?=null
    private  var editRecommendation : TextInputEditText?=null
    private  var editAmount : TextInputEditText?=null
    private  var editSuggestion : TextInputEditText?=null

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var currentLoc: String? = null
    private val permissionId = 2
    var list: List<Address>? = null
    var myReceiver: ConnectivityListener? = null
    private lateinit var btnAadharFront: ImageView
    private lateinit var tvImageCount: TextView
    val PERMISSION_CODE = 12345
    val CAMERA_PERMISSION_CODE1 = 123
    var SELECT_PICTURES1 = 1
    var file2: File? = null
    var activity: Activity = this
    val imgList: MutableList<File> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_complaints)
        if (SalesApp.isEnableScreenshort==true){
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        binding.igToolbar.tvTitle.text = "All Complaints"
        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }

        intent.getStringExtra("Status")?.let { apiAllCompaints(it) }
        requestPermission()
    }

    fun apiAllCompaints(status: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["status"] = status
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getComplaints, params)

    }

    fun handleComplaints(data: List<AllComplaintsBean.Data>) {
        binding.rcOfficeTeam.layoutManager = LinearLayoutManager(this)
        var mAdapter = AllComplaintsAdapter(this, data, object :
            RvStatusComplClickListner {
            override fun clickPos(status: String,workstatus: String,payableAmt: String, id: Int) {
                if (workstatus.equals("under_process")||workstatus.equals("stop")||workstatus.equals("rejected")||workstatus.equals("start")){
                    dialogRemark(status,workstatus,id)
                }else if (workstatus.equals("completed")){
                    openCompletdDialog(status,workstatus,id,payableAmt)
                }else{
                    dialog(status,workstatus,id)
                }
            }
        })
        binding.rcOfficeTeam.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    fun apiStatrt(status: String, workstatus: String, id: Int) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        val params = Utility.getParmMap()
        builder.addFormDataPart("status",status)
        builder.addFormDataPart("work_status",workstatus)
        builder.addFormDataPart("id", id.toString())
        builder.addFormDataPart("comment",editReamrk?.text.toString())
        builder.addFormDataPart("suggestion",editSuggestion?.text.toString())
        builder.addFormDataPart("recommendation",editRecommendation?.text.toString())
        builder.addFormDataPart("lat_long","${list?.get(0)?.latitude},${list?.get(0)?.latitude}")
        builder.addFormDataPart("paid_amount",editAmount?.text.toString())


        for (i in 0 until imgList.size) {
            builder.addFormDataPart("files[]", imgList.get(i).name,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imgList.get(i)))
        }

        Log.d("requestParms", Gson().toJson(builder))
        apiClient.progressView.showLoader()
        apiClient.makeCallMultipart(ApiContants.getUpdateComplaint, builder.build())

    }

    fun apiAccept(status: String, id: Int) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["status"] = status
        params["id"] = id.toString()
        params["comment"] = editReamrk?.text.toString()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getUpdateAllocateRequest, params)
    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.getComplaints) {
                val officeTeamBean = apiClient.getConvertIntoModel<AllComplaintsBean>(
                    jsonElement.toString(),
                    AllComplaintsBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (officeTeamBean.error==false) {
                    handleComplaints(officeTeamBean.data)
                }

            }
            if (tag == ApiContants.getUpdateComplaint) {
                val officeTeamBean = apiClient.getConvertIntoModel<AllComplaintsBean>(
                    jsonElement.toString(),
                    AllComplaintsBean::class.java
                )

                if (officeTeamBean.error==false) {
                      Toast.makeText(this, officeTeamBean.msg, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
/*
            if (tag == ApiContants.getUpdateAllocateRequest) {
                val officeTeamBean = apiClient.getConvertIntoModel<AlocateRequestBean>(
                    jsonElement.toString(),
                    AlocateRequestBean::class.java
                )

                if (officeTeamBean.error==false) {
                    Toast.makeText(this, officeTeamBean.msg, Toast.LENGTH_SHORT).show()
                }

            }*/
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

    fun dialog(status: String, workstatus: String, ids: Int) {

            val builder = AlertDialog.Builder(this@AllComplaintsActivity)
            builder.setMessage("Are you sure you want to start service?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // Delete selected note from database
                    apiStatrt(status,workstatus,ids)
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()

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
            if (workstatus.equals("rejected")){
                apiStatrt(status,workstatus,ids)
           //     apiAccept(status,ids)
            }else{
                apiStatrt(status,workstatus,ids)
            }


        }

    }

}

    fun ClickPicCamera(CAMERA_PERMISSION_CODE: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_PERMISSION_CODE)
    }

    fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES),
            PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==PERMISSION_CODE){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission is Granted",Toast.LENGTH_SHORT).show()

            }
            else{
                requestPermission()
            }
        }
    }

    private fun uploadImage(SELECT_PICTURES: Int) {
        if (Build.VERSION.SDK_INT < 19) {
            var intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose Pictures")
                , SELECT_PICTURES
            )
        }
        else { // For latest versions API LEVEL 19+
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, SELECT_PICTURES);
        }
    }

    fun openCameraDialog(SELECT_PICTURES: Int,CAMERA_PERMISSION_CODE: Int) {
        val dialog: Dialog = GeneralUtilities.openBootmSheetDailog(R.layout.dialog_camera, R.style.AppBottomSheetDialogTheme,
            this
        )
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        val llInternalPhoto = dialog.findViewById<View>(R.id.llInternalPhoto) as LinearLayout
        val llClickPhoto = dialog.findViewById<View>(R.id.llClickPhoto) as LinearLayout

        llInternalPhoto.setOnClickListener { dialog.dismiss()
            requestPermission()
            uploadImage(SELECT_PICTURES)
        }

        llClickPhoto.setOnClickListener { dialog.dismiss()
            requestPermission()
            ClickPicCamera(CAMERA_PERMISSION_CODE)

        }
        ivClose.setOnClickListener { dialog.dismiss() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURES1){
            if (data?.getClipData() != null) { // if multiple images are selected
                var count = data.clipData?.itemCount
                tvImageCount.visibility=View.VISIBLE
                tvImageCount.text="$count Images"
                Log.d("wewwe", "$count")
                for (i in 0..count!! - 1) {
                    var imageUri: Uri = data.clipData?.getItemAt(i)!!.uri
                    val picturePath: String = GeneralUtilities.getPath(
                        applicationContext, imageUri
                    )
                    file2 = File(picturePath)
                    //val custImg = CustProdImgBean(file2)
                    imgList.add(file2!!)
                    //   Log.d("MultiPicturePath", picturePath)

                    //     iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews
                }

            } else if (data?.getData() != null) {   // if single image is selected
                tvImageCount.visibility=View.GONE
                var imageUri: Uri = data.data!!
                val picturePath: String = GeneralUtilities.getPath(
                    applicationContext, imageUri)
                file2 = File(picturePath)
                val myBitmap = BitmapFactory.decodeFile(file2!!.absolutePath)
                btnAadharFront.setImageBitmap(myBitmap)
                imgList.add(file2!!)
                /*  val custImg = CustProdImgBean(file2)
                  val arrlis:ArrayList<File>
                  cutomProdImgList.add(custImg)*/
                Log.d("SinglePicturePath", picturePath)
                //   iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview
            }
        }
        if (requestCode == CAMERA_PERMISSION_CODE1) {
            try {
                Toast.makeText(this@AllComplaintsActivity,"sdfsd",Toast.LENGTH_SHORT).show()

                val imageBitmap = data?.extras?.get("data") as Bitmap
                btnAadharFront.setImageBitmap(imageBitmap)
                val tempUri =GeneralUtilities.getImageUri(applicationContext, imageBitmap)
                file2= File(GeneralUtilities.getRealPathFromURII(this,tempUri))
                imgList.add(file2!!)
                Log.e("Path", file2.toString())

                //Toast.makeText(getContext(), ""+picturePath, Toast.LENGTH_SHORT).show();
            } catch (e: java.lang.Exception) {
                Log.e("Path Error", e.toString())
                Toast.makeText(applicationContext, "" + e, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openCompletdDialog(status: String, workstatus: String, ids: Int,payableAmt: String) {
        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.dialog_complete_img,null)
        val  submit = view.findViewById<Button>(R.id.dialogDismiss_button)
          editRecommendation = view.findViewById<TextInputEditText>(R.id.editRecommendation)
          editAmount = view.findViewById<TextInputEditText>(R.id.editAmount)
          editSuggestion = view.findViewById<TextInputEditText>(R.id.editSuggestion)
        editReamrk = view.findViewById<TextInputEditText>(R.id.editComment)
        btnAadharFront = view.findViewById<ImageView>(R.id.btnAadharFront)
       val tvPayableAmt = view.findViewById<TextView>(R.id.tvPayableAmt)
        val  ivClose = view.findViewById<ImageView>(R.id.ivClose)
        tvImageCount = view.findViewById<TextView>(R.id.tvImageCount)
        builder.setView(view)
        ivClose.setOnClickListener {
            builder.dismiss()
        }
        submit.setOnClickListener {
            builder.dismiss()
            apiStatrt(status,workstatus,ids)
        }
        tvPayableAmt.setText(ApiContants.currency+payableAmt)
        btnAadharFront.setOnClickListener {
            //  uploadImage(SELECT_PICTURES1)
            openCameraDialog(SELECT_PICTURES1,CAMERA_PERMISSION_CODE1)
        }

        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        list =

                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
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
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        // Start the LocationService when the app is closed
       // startService(Intent(this, LocationService::class.java))
    }

    fun ency(){
        val masterKey: MasterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            this,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        // use the shared preferences and editor as you normally would

        // use the shared preferences and editor as you normally would
        val editor = sharedPreferences.edit()
    }
}
