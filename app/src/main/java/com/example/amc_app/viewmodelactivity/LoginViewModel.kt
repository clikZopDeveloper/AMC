package com.example.amc_app.viewmodelactivity

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.amc_app.Activity.DashboardActivity
import com.example.amc_app.Activity.LoginActivity
import com.example.amc_app.ApiHelper.ApiController
import com.example.amc_app.ApiHelper.ApiResponseListner
import com.example.amc_app.Model.LoginBean
import com.example.amc_app.Utills.GeneralUtilities
import com.example.amc_app.Utills.PrefManager
import com.example.amc_app.Utills.Utility
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class LoginViewModel(val context: LoginActivity) : ViewModel() , ApiResponseListner {
    private lateinit var apiClient: ApiController
    val mobileNo=MutableLiveData<List<LoginBean.Data>>()
    val errorMsg=MutableLiveData<String>()

  /*  private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
*/

    fun apiCallLogin() {
        apiClient = ApiController(context, context)
        val params = Utility.getParmMap()
      //  params["mobile"] = binding.editMobNo.text.toString()
      //  params["password"] = binding.editPassword.text.toString()

        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.login, params)

    }

    override fun success(tag: String?, jsonElement: JsonElement) {
        apiClient.progressView.hideLoader()
        if (tag == ApiContants.login) {
            val loginModel = apiClient.getConvertIntoModel<LoginBean>(jsonElement.toString(), LoginBean::class.java)
            PrefManager.putString(ApiContants.AccessToken, loginModel.data.token )
         //   PrefManager.putString(ApiContants.mobileNumber, binding.editMobNo.text.toString())
          //  PrefManager.putString(ApiContants.password,  binding.editPassword.text.toString() )
            Toast.makeText(context, loginModel.msg, Toast.LENGTH_SHORT).show()
            GeneralUtilities.launchActivity(context, DashboardActivity::class.java)
            context.finishAffinity()
        }
    }

    override fun failure(tag: String?, errorMessage: String) {

        apiClient.progressView.hideLoader()

        Utility.showSnackBar(context, errorMessage)
    }
}