package com.example.amc_app.Activity

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.amc_app.Adapter.UpdateAllStatusAdapter
import com.example.amc_app.ApiHelper.ApiController
import com.example.amc_app.ApiHelper.ApiResponseListner
import com.example.amc_app.Model.GetAllStatusBean
import com.example.amc_app.R
import com.example.amc_app.Utills.*
import com.example.amc_app.databinding.ActivityUpdateLeadBinding

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants
import java.io.File
import java.util.*


class UpdateTelecallerLeadActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityUpdateLeadBinding

    val imgList: MutableList<File> = ArrayList()
    private lateinit var apiClient: ApiController
    private lateinit var btnAadharFront: ImageView
    private lateinit var tvImageCount: TextView
    private var calendar: Calendar? = null
    var myReceiver: ConnectivityListener? = null
    var fromDate = ""
    var custID = ""
    var leadStatus = ""
    var statusID = 0
    var projectType = "Completed"
    var activity: Activity = this
    val PERMISSION_CODE = 12345
    val CAMERA_PERMISSION_CODE1 = 123
    var SELECT_PICTURES1 = 1
    var file2: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_lead)
        if (SalesApp.isEnableScreenshort==true){
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()
        binding.igToolbar.tvTitle.text = "Update Status"
        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }


        custID = intent.getStringExtra("cust_ID")!!
       // leadStatus = intent.getStringExtra("leadStatus")!!
          requestPermission()
    //    typeMode()
        apiGetStatus()

        calendar = Calendar.getInstance();
        val hour: Int = calendar!!.get(Calendar.HOUR_OF_DAY)
        val min: Int = calendar!!.get(Calendar.MINUTE)

        //   callCityListAdapter()

        binding.apply {

            editTime.setOnClickListener {
                val timePickerDialog = TimePickerDialog(
                    this@UpdateTelecallerLeadActivity,
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        editTime.setText(
                            "$hourOfDay:$minute"
                        )
                    },
                    hour,
                    min,
                    false
                )
                timePickerDialog.show()
            }

            editDate.setOnClickListener(View.OnClickListener {
                val c = Calendar.getInstance()
                val year = c[Calendar.YEAR]
                val month = c[Calendar.MONTH]
                val day = c[Calendar.DAY_OF_MONTH]
                val datePickerDialog = DatePickerDialog(
                    this@UpdateTelecallerLeadActivity,
                    { view, year, monthOfYear, dayOfMonth ->
                        //  dob.setText(dateofnews);
                        val dateofnews = "${ year.toString()+ "-"+(monthOfYear + 1).toString()  + "-" + dayOfMonth.toString() }"

                        //   val dateofnews = (monthOfYear + 1).toString() + "/" + dayOfMonth + "/" + year
                        editDate.setText(dateofnews)
                    },
                    year, month, day
                )
                datePickerDialog.show()
            })

            btnSubmit.setOnClickListener {
                if (leadStatus.equals("") && leadStatus.isNullOrEmpty()) {
                    Toast.makeText(this@UpdateTelecallerLeadActivity,"Please Select Status",Toast.LENGTH_SHORT).show()
                }else{
                    apiUpdateLead()
                }
                /*  if (leadPos==3||leadPos==4){

                    }else{
                        apiUpdateLead()
                    }*/
            }
        }
    }

    fun typeMode() {
        binding.radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                if (checkedId == R.id.rbCompleted) {
                    projectType = "Completed"
                } else if (checkedId == R.id.rbPartial) {
                    projectType = "Partial"

                }
            }
        })
    }

    fun apiUpdateLead() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(activity, this)
        val params = Utility.getParmMap()
        params["status"] = statusID.toString()
        params["customer_id"] = custID
        params["remarks"] = binding.editRemark.text.toString()
        params["remind_date"] = binding.editDate.text.toString()
        params["remind_time"] = binding.editTime.text.toString()

        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getUpdateStatus, params)

    }


    override fun success(tag: String?, jsonElement: JsonElement) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.getUpdateStatus) {
                val updateLeadBean = apiClient.getConvertIntoModel<GetAllStatusBean>(
                    jsonElement.toString(),
                    GetAllStatusBean::class.java
                )

                Toast.makeText(this, updateLeadBean.msg, Toast.LENGTH_SHORT).show()
                finish()
            }


            if (tag == ApiContants.GetStatus) {
                val allStatusBean = apiClient.getConvertIntoModel<GetAllStatusBean>(
                    jsonElement.toString(),
                    GetAllStatusBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (allStatusBean.error==false) {
                    handleRcStatus(allStatusBean.data)
                }

            }
        }catch (e:Exception){
            Log.d("error>>",e.localizedMessage)
        }

    }

    fun apiGetStatus() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.GetStatus, params)

    }

    override fun failure(tag: String?, errorMessage: String) {
        apiClient.progressView.hideLoader()

        Utility.showSnackBar(activity, errorMessage)
        Log.d("error",errorMessage)

    }

    fun handleRcStatus(data: List<GetAllStatusBean.Data>) {
        binding.rcStatus.layoutManager = GridLayoutManager(this,3)
        var mAdapter = UpdateAllStatusAdapter(this, data, object :
            RvStatusClickListner {
            override fun clickPos(status: String, id: Int) {
                if (id==2||id==3)
                    binding.llDateTimeSection.visibility=View.VISIBLE
                else
                    binding.llDateTimeSection.visibility=View.GONE

                leadStatus=status
                statusID=id

            }
        })
        binding.rcStatus.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

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

        llClickPhoto.setOnClickListener {
            dialog.dismiss()
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
                Toast.makeText(this@UpdateTelecallerLeadActivity,"sdfsd",Toast.LENGTH_SHORT).show()

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
    override fun onDestroy() {
        super.onDestroy()
        // Start the LocationService when the app is closed
        startService(Intent(this, LocationService::class.java))
    }
    override fun onNetworkConnectionChange(isconnected: Boolean) {
        ApiContants.isconnectedtonetwork = isconnected
        GeneralUtilities.internetConnectivityAction(this, isconnected)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
}
