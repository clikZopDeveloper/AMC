package com.example.amc_app.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.amc_app.ApiHelper.ApiController
import com.example.amc_app.ApiHelper.ApiResponseListner
import com.example.amc_app.Model.LoginBean
import com.example.amc_app.Model.LoginNewBean
import com.example.amc_app.R
import com.example.amc_app.Utills.GeneralUtilities
import com.example.amc_app.Utills.PrefManager
import com.example.amc_app.Utills.Utility
import com.example.amc_app.databinding.ActivityLoginBinding
import com.example.amc_app.databinding.ActivityLoginNewBinding
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class LoginNewActivity : AppCompatActivity() , ApiResponseListner {
    private lateinit var binding: ActivityLoginNewBinding
    private lateinit var apiClient: ApiController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_new)
    //    setContentView(R.layout.activity_login_new)
        binding.btnLogin.setOnClickListener {
            doLogin()
        }

    }


    private fun doLogin() {
        if (TextUtils.isEmpty(binding.editMobNo.text.toString())) {
            Utility.showSnackBar(this, "Please enter mobile number")
        } else if (binding.editMobNo.text.toString().length < 10) {
            Utility.showSnackBar(this, "Please enter valid mobile number")
        } else if (TextUtils.isEmpty(binding.editPassword.text.toString())) {
            Utility.showSnackBar(this, "Please enter password")
        } else {
            //    loginViewModel.apiCallLogin()
            apiCallLogin()
        }
    }
    fun apiCallLogin() {
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["UserID"] = binding.editMobNo.text.toString()
        params["Password"] = binding.editPassword.text.toString()
        apiClient.progressView.showLoader()
        apiClient.makeCallPostMLM(ApiContants.loginMLM, params)

    }

    override fun success(tag: String?, jsonElement: JsonElement) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.loginMLM) {
                val loginModel = apiClient.getConvertIntoModel<LoginNewBean>(
                    jsonElement.toString(),
                    LoginNewBean::class.java
                )
                PrefManager.putString(
                    ApiContants.Type,"Success"

                )
                    PrefManager.putString(
                        ApiContants.mobileNumber,
                        binding.editMobNo.text.toString()
                    )

                    PrefManager.putString(
                        ApiContants.password,
                        binding.editPassword.text.toString()
                    )

                startActivity(Intent(this,WebviewActivity::class.java)
                    .putExtra("url",loginModel.d.data))

                    finishAffinity()

            }
        } catch (e: Exception) {
            Log.d("error>>", e.localizedMessage)
        }

    }

    override fun failure(tag: String?, errorMessage: String) {

        apiClient.progressView.hideLoader()

        Utility.showSnackBar(this, errorMessage)
    }
}