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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amc_app.Adapter.ProductListAdapter

import com.example.amc_app.ApiHelper.ApiController
import com.example.amc_app.ApiHelper.ApiResponseListner
import com.example.amc_app.Model.*

import com.example.amc_app.R
import com.example.amc_app.Utills.*
import com.example.amc_app.databinding.ActivityCreateOrderBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants
import java.io.File
import java.util.*


class CreateOrderActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityCreateOrderBinding
    val list: MutableList<MultipleProductBean> = ArrayList()
    val imgList: MutableList<File> = ArrayList()
    private lateinit var apiClient: ApiController
    var myReceiver: ConnectivityListener? = null
    var way = ""
    var productID = 0
    var customerID = 0
    var catID = 0
    var activity: Activity = this
    val PERMISSION_CODE = 12345
    val CAMERA_PERMISSION_CODE1 = 123
    var SELECT_PICTURES1 = 1
    var custID = ""
    var file2: File? = null
    var statusType=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_order)

        if (SalesApp.isEnableScreenshort == true) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            );
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }

        way = intent.getStringExtra("way")!!
        //      requestPermission()

        //   callCityListAdapter()
        if (way.equals("CreateOrder")) {
            binding.igToolbar.tvTitle.text = "Create Order"
        } else {
                  binding.igToolbar.tvTitle.text = "Update Order"
         //   intent.getStringExtra("cust_ID")?.let { apiCustomerDetail(it) }
            custID = intent.getStringExtra("cust_ID")!!

            //   val custResponse:CustomerListBean.Data = (intent.getSerializableExtra("custResponse") as CustomerListBean.Data?)!!
            //   Log.d("sdfdf",Gson().toJson(custResponse))

        }

        apiCatCustmorList()

        binding.apply {

            btnUplaodImages.setOnClickListener {
                openCameraDialog(SELECT_PICTURES1, CAMERA_PERMISSION_CODE1)
            }

            btnSubmit.setOnClickListener {
                apiCreateOrder()
            }

            btnAddProduct.setOnClickListener {
                addDataList()
            }

        }
    }

    fun typeMode(customerBean: GetCustomerBean.Data) {
        binding.handleViewVisible.visibility=View.VISIBLE
        binding.tvMobNo.setText("Mobile Number : "+customerBean.mobile)
        binding.tvEmail.setText("Email : "+customerBean.email)
        if (customerBean.billingAddress!=null){
            binding.rbBillingAdd.setText(customerBean.billingAddress)
            statusType= customerBean.billingAddress
        }

        if (customerBean.billingAddress!=null){
            binding.rbShippingAdd.setText(customerBean.shippingAddress)
        }


        binding.RadioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                if (checkedId == R.id.rbBillingAdd) {
                    if (customerBean.billingAddress!=null){
                        statusType = customerBean.billingAddress
                    }
                } else if (checkedId == R.id.rbShippingAdd) {
                    if (customerBean.shippingAddress!=null){
                        statusType = customerBean.shippingAddress
                    }
                }
            }
        })

    }

    fun apiProducts() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["cat_id"]=catID.toString()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.GetProducts, params)

    }

    fun apiCatCustmorList() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getCategory, params)
        apiClient.getApiPostCall(ApiContants.GetCustomer, params)

    }

    fun setCategoryData(data: List<CategoryBean.Data>) {
        val state = arrayOfNulls<String>(data.size)
        for (i in data.indices) {
            state[i] = data.get(i).name
        }

        binding.stateProductCategory.setAdapter(
            ArrayAdapter(
                this@CreateOrderActivity,
                android.R.layout.simple_list_item_1, state
            )
        )

        binding.stateProductCategory.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            //  var sourceName = SalesApp.sourceList.get(position).name

            binding.stateProductCategory.setText(parent.getItemAtPosition(position).toString())
            Log.d("StateID", "" + parent.getItemAtPosition(position).toString())
            for (customerBean in data) {
                if (customerBean.name.equals(
                        parent.getItemAtPosition(position).toString()
                    )
                ) {
                    binding.stateProductCategory.setText(parent.getItemAtPosition(position).toString())
                    catID = customerBean.id
                    Log.d("StateID", "" + customerBean.id)

                }
            }
            Toast.makeText(
                applicationContext,
                binding.stateProductCategory.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()
            apiProducts()
            setCategoryData(data)

        })
    }

    fun setCustomerData(data: List<GetCustomerBean.Data>) {
        val state = arrayOfNulls<String>(data.size)
        for (i in data.indices) {
            state[i] = data.get(i).name
        }

        binding.stateCustomer.setAdapter(
            ArrayAdapter(
                this@CreateOrderActivity,
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
                    typeMode(customerBean)
                    binding.stateCustomer.setText(parent.getItemAtPosition(position).toString())
                }
            }
            Toast.makeText(
                applicationContext,
                binding.stateCustomer.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()
            setCustomerData(data)

        })
    }

    fun setProductData(data: List<ProductBean.Data>) {
        val state = arrayOfNulls<String>(data.size)
        for (i in data.indices) {
            state[i] = data.get(i).name
        }

        binding.stateProduct.setAdapter(
            ArrayAdapter(
                this@CreateOrderActivity,
                android.R.layout.simple_list_item_1, state
            )
        )

        binding.stateProduct.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            //  var sourceName = SalesApp.sourceList.get(position).name

            Log.d("StateID", "" + parent.getItemAtPosition(position).toString())
            for (customerBean in data) {
                if (customerBean.name.equals(
                        parent.getItemAtPosition(position).toString()
                    )
                ) {
                    binding.stateProduct.setText(parent.getItemAtPosition(position).toString())
                    productID = customerBean.id
                    Log.d("StateID", "" + customerBean.id)
                }
            }
            Toast.makeText(
                applicationContext,
                binding.stateProduct.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()
            setProductData(data)

        })
    }

    override fun success(tag: String?, jsonElement: JsonElement) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.CreateOrder) {
                val createOrderBean = apiClient.getConvertIntoModel<CreateOrderBean>(
                    jsonElement.toString(),
                    CreateOrderBean::class.java
                )

                Toast.makeText(this, createOrderBean.msg, Toast.LENGTH_SHORT).show()
                finish()
            }


            if (tag == ApiContants.getUpdateExpense) {
                val updateLeadBean = apiClient.getConvertIntoModel<DashboardBean>(
                    jsonElement.toString(),
                    DashboardBean::class.java
                )

                Toast.makeText(this, updateLeadBean.msg, Toast.LENGTH_SHORT).show()
                finish()
            }


            if (tag == ApiContants.getCategory) {
                val categoryBean = apiClient.getConvertIntoModel<CategoryBean>(
                    jsonElement.toString(),
                    CategoryBean::class.java
                )

                if (categoryBean.error == false) {
                    setCategoryData(categoryBean.data)
                }

            }
            if (tag == ApiContants.getCustomer) {
                val getCustomerBean = apiClient.getConvertIntoModel<GetCustomerBean>(
                    jsonElement.toString(),
                    GetCustomerBean::class.java
                )

                if (getCustomerBean.error == false) {
                    setCustomerData(getCustomerBean.data)
                }

            }
            if (tag == ApiContants.GetProducts) {
                val productBean = apiClient.getConvertIntoModel<ProductBean>(
                    jsonElement.toString(),
                    ProductBean::class.java
                )

                if (productBean.error == false) {
                    setProductData(productBean.data)
                }

            }
        } catch (e: Exception) {
            Log.d("error>>", e.localizedMessage)
        }

    }

    override fun failure(tag: String?, errorMessage: String) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(activity, errorMessage)
        Log.d("error", errorMessage)

    }

    fun ClickPicCamera(CAMERA_PERMISSION_CODE: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_PERMISSION_CODE)
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES
            ),
            PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission is Granted", Toast.LENGTH_SHORT).show()

            } else {
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
                Intent.createChooser(intent, "Choose Pictures"), SELECT_PICTURES
            )
        } else { // For latest versions API LEVEL 19+
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, SELECT_PICTURES);
        }
    }

    fun openCameraDialog(SELECT_PICTURES: Int, CAMERA_PERMISSION_CODE: Int) {
        val dialog: Dialog = GeneralUtilities.openBootmSheetDailog(
            R.layout.dialog_camera, R.style.AppBottomSheetDialogTheme,
            this
        )
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        val llInternalPhoto = dialog.findViewById<View>(R.id.llInternalPhoto) as LinearLayout
        val llClickPhoto = dialog.findViewById<View>(R.id.llClickPhoto) as LinearLayout

        llInternalPhoto.setOnClickListener {
            dialog.dismiss()
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
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURES1) {
            if (data?.getClipData() != null) { // if multiple images are selected
                var count = data.clipData?.itemCount
                binding.tvImageCount.visibility = View.VISIBLE
                binding.tvImageCount.text = "$count Images"
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
                binding.tvImageCount.visibility = View.GONE
                var imageUri: Uri = data.data!!
                val picturePath: String = GeneralUtilities.getPath(
                    applicationContext, imageUri
                )
                file2 = File(picturePath)
                val myBitmap = BitmapFactory.decodeFile(file2!!.absolutePath)
                binding.btnAadharFront.setImageBitmap(myBitmap)
                imgList.add(file2!!)

                Log.d("SinglePicturePath", picturePath)
                //   iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview
            }
        }

        if (requestCode == CAMERA_PERMISSION_CODE1) {
            try {
                Toast.makeText(this@CreateOrderActivity, "sdfsd", Toast.LENGTH_SHORT).show()

                val imageBitmap = data?.extras?.get("data") as Bitmap
                binding.btnAadharFront.setImageBitmap(imageBitmap)
                val tempUri = GeneralUtilities.getImageUri(applicationContext, imageBitmap)
                file2 = File(GeneralUtilities.getRealPathFromURII(this, tempUri))
                imgList.add(file2!!)
                Log.e("Path", file2.toString())

                //Toast.makeText(getContext(), ""+picturePath, Toast.LENGTH_SHORT).show();
            } catch (e: java.lang.Exception) {
                Log.e("Path Error", e.toString())
                Toast.makeText(applicationContext, "" + e, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addDataList() {
        val params = Utility.getParmMap()
        params["catName"] = binding.stateProductCategory.text.toString()
        params["price"] = binding.editPrice.text.toString()
        params["product_id"] = productID.toString()
        params["qty"] = binding.editQty.text.toString()
        val multiple = MultipleProductBean(
            binding.stateProductCategory.text.toString(),
            binding.editPrice.text.toString(),
            productID.toString(),
            binding.editQty.text.toString(), ""
        )

        list.add(multiple)

        Toast.makeText(this@CreateOrderActivity, "Product Added Successfully", Toast.LENGTH_SHORT)
            .show()
        binding.editQty.text?.clear()
        Log.d("xzczxcxz", Gson().toJson(list))
        Log.d("xzczxcxz", Gson().toJson(list))

        handleProductList(list)

    }

    fun handleProductList(data: MutableList<MultipleProductBean>) {
        binding.rcAllProduct.visibility = View.VISIBLE
        binding.rcAllProduct.layoutManager = LinearLayoutManager(this)
        var mAdapter = ProductListAdapter(this,  data, object :
            RvStatusClickListner {
            override fun clickPos(status: String, pos: Int) {

            }
        })
        binding.rcAllProduct.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    fun apiCreateOrder() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(activity, this)
        val params = Utility.getParmMap()
        params["customer_id"] =customerID.toString()
        params["products"] = Gson().toJson(list)
        params["delivery_address"] = statusType
        apiClient.progressView.showLoader()
        if (way.equals("CreateOrder")) {
            apiClient.getApiPostCall(ApiContants.CreateOrder, params)
        }else{
            apiClient.getApiPostCall(ApiContants.getUpdateExpense, params)
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
