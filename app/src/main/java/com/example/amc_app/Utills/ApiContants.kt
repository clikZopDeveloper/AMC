package com.stpl.antimatter.Utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import java.util.*

public class ApiContants {
    companion object {
        var isconnectedtonetwork = false


        const val BaseUrl="https://clikzopinnovations.ecommercekart.in/api/"//Live URL
        const val ImgBaseUrl="https://clikzopinnovations.ecommercekart.in/api/"//Image Base URL

        const val EmailAddress = "emailID"
        const val UserType = "userType"
        const val REQ_CODE_VERSION_UPDATE = 530
        const val PlaceLocation = "location"
        const val mobileNumber = "mobileNumber"
        const val userName = "userName"
        const val password = "password"
        const val PlaceRegion = "locationCountry"
        const val PlaceLatLang = "locLatLang"
        const val PlaceLat = "locLat"
        const val PlaceLang = "PlaceLang"
        val WhatsAppNumber = "**********"
        const val PREF_IS_METRIC = "unit"
        const val UserDetails = "userDetails"
        const val UserAvailableAmt = "useravailablebal"
        const val DeviceToken = "321"
        const val AccessToken = "accessToken"
        const val Type = "android"
        const val currency = "₹"
        const val dayStatus = "dayStatus"
        const val officeBreakStatus = "officeBreakStatus"
        const val updateOfficeBreak = "updateOfficeBreak"



        const val success = "success"
        const val failure = "failure"
        const val NoInternetConnection = "Please check your internet connection"

        //        api Tags
        const val login = "login"
        const val loginMLM = "Login"
        const val logout = "logout"
        const val startDay = "start-day"
        const val endDay = "end-day"
        const val getProjects = "get-projects"
        const val getProjectsDetails = "get-project-details"
        const val createExpense = "create-expense"
        const val getComplaints = "get-complaints"
        const val getProfile = "get-profile"
        const val getCity = "get-city"
        const val getState = "get-state"
        const val GetStatus = "get-status"
        const val getCategory = "get-category"
        const val getPasswordChange = "password-change"
        const val getCustomer = "get-customers"
        const val getUpdateAllocateRequest = "update-allocation-request"
        const val getUpdateComplaint = "update-complaint"
        const val getLocationUpdate = "location-update"
        const val getCustomerData = "get-customer-data"
        const val getOrderDetail = "get-order-details"
        const val getUpdateExpense = "update-expense"
        const val getContactList = "get-contact-list"
        const val getDashboard = "dashboard"
        const val getUpdateStatus = "update-status"
        const val getExpenses = "get-expenses"
        const val getOfficeBreak = "office-break"
        const val CreateOrder = "create-order"
        const val CreateReturnOrder = "create-return-order"
        const val GetCustomer = "get-customers"
        const val GetProducts = "get-products"
        const val GetMettingList = "get-meeting-list"
        const val GetClientMeeting = "client-meeting"
        const val GetOrders = "get-orders"
        const val GetProductWiseReport = "productwise-report"
        const val GetCustomerWiseReport = "customerwise-report"


        fun callPGURL(context: Context, url: String) {
            Log.d("weburl", url)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.android.chrome")
            try {
                context.startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                // Chrome browser presumably not installed so allow user to choose instead
                intent.setPackage(null)
                context.startActivity(intent)
            }
        }











        //////////////////////////Current Location/////////////////////////
        @SuppressLint("MissingPermission", "SetTextI18n")
         fun getLocation(
            mFusedLocationClient: FusedLocationProviderClient,
            dashboardActivity: Activity
        ): List<Address>? {
            var list: List<Address>? = null
            if (checkPermissions(dashboardActivity)) {
                if (isLocationEnabled(dashboardActivity)) {
                    mFusedLocationClient.lastLocation.addOnCompleteListener(dashboardActivity) { task ->
                        val location: Location? = task.result
                        if (location != null) {
                            val geocoder = Geocoder(dashboardActivity, Locale.getDefault())
                            list =geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            Log.d("zxxzv", "Lat" + Gson().toJson(list?.get(0)?.latitude))
                            Log.d("zxxzv", "Long" + Gson().toJson(list?.get(0)?.longitude))
                            Log.d("zxxzv", Gson().toJson(list?.get(0)?.countryName))
                            Log.d("zxxzv", Gson().toJson(list?.get(0)?.locality))
                            Log.d("zxxzv", Gson().toJson(list?.get(0)?.getAddressLine(0)))

                          //  currentLoc = list?.get(0)?.getAddressLine(0)
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
                    Toast.makeText(dashboardActivity, "Please turn on location", Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    dashboardActivity.startActivity(intent)
                }
            } else {
                requestPermissions(dashboardActivity)
            }
            return list
        }

        private fun requestPermissions(dashboardActivity: Activity) {
            ActivityCompat.requestPermissions(
                dashboardActivity,
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                2
            )
        }

        private fun isLocationEnabled(dashboardActivity: Activity): Boolean {
            val locationManager: LocationManager =
                dashboardActivity.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }

        private fun checkPermissions(dashboardActivity: Activity): Boolean {
            if (ActivityCompat.checkSelfPermission(
                    dashboardActivity,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    dashboardActivity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                return true
            }
            return false
        }
    }


}