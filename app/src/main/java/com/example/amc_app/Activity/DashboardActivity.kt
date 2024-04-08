package com.example.amc_app.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amc_app.Adapter.AllStatusAdapter
import com.example.amc_app.Adapter.CommonFieldDrawerAdapter
import com.example.amc_app.ApiHelper.ApiController
import com.example.amc_app.ApiHelper.ApiResponseListner
import com.example.amc_app.Model.*
import com.example.amc_app.R
import com.example.amc_app.Utills.*
import com.example.amc_app.databinding.ActivityMainBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class DashboardActivity : AppCompatActivity(), ApiResponseListner , GoogleApiClient.OnConnectionFailedListener,
ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var apiClient: ApiController
    lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var rcStatus: RecyclerView
    lateinit var rcMaster: RecyclerView
    lateinit var llMaster: LinearLayout
    lateinit var ivDownArrowMaster: ImageView
    var myReceiver: ConnectivityListener? = null
    private var currentLoc: String? = null
    var isActive = true
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        //  val navView: NavigationView = binding.navView
        val navBottomView: BottomNavigationView = binding.appBarMain.bottomNavView
        myReceiver = ConnectivityListener()
        val headerView: View = binding.navView.getHeaderView(0)
        rcStatus = headerView.findViewById<RecyclerView>(R.id.rcStatus)
        rcMaster = headerView.findViewById<RecyclerView>(R.id.rcMaster)
        llMaster = headerView.findViewById<LinearLayout>(R.id.llMaster)
        ivDownArrowMaster = headerView.findViewById<ImageView>(R.id.ivDownArrowMaster)

         navController = findNavController(R.id.nav_host_fragment_activity_main)

        binding.appBarMain.appbarLayout.ivMenu.setOnClickListener {
            drawerLayout.open()
        }
       // ApiContants.getLocation(mFusedLocationClient,this)
        handleRcMaster()

      //  apiGetStatus()

        if (SalesApp.isEnableScreenshort==true){
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
         startService(Intent(this, LocationService::class.java))
        //   binding.appBarMain.appbarLayout.switchDayStart="Day Start"

        Log.d("token>>>>>", PrefManager.getString(ApiContants.AccessToken, ""))



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        /* appBarConfiguration = AppBarConfiguration(
             setOf(
                 R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
             ), drawerLayout
         )*/


        //  setupActionBarWithNavController(navController, appBarConfiguration)
        navBottomView.setupWithNavController(navController)



        llMaster.setOnClickListener(View.OnClickListener {
            if (isActive) {
                isActive = false
                ivDownArrowMaster.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                rcMaster.setVisibility(View.VISIBLE)

            } else {
                isActive = true
                ivDownArrowMaster.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                rcMaster.setVisibility(View.GONE)

            }
        })

    }


    fun replaceFrag(fragment: Int, tag: String, transid: String, title: String){
        val bundle = Bundle()
        //     bundle.putInt(Constants.Frag_Type, fragmentType2)
        bundle.putString("catID", transid)
        bundle.putString("title", title)
        navController.navigate(fragment, bundle)
    }
    fun apiGetStatus() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.GetStatus, params)

    }


    override fun success(tag: String?, jsonElement: JsonElement) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.logout) {
                val baseResponseBean = apiClient.getConvertIntoModel<BaseResponseBean>(
                    jsonElement.toString(),
                    BaseResponseBean::class.java
                )
                Toast.makeText(this, baseResponseBean.msg, Toast.LENGTH_SHORT).show()
                PrefManager.clear()
                GeneralUtilities.launchActivity(this, LoginActivity::class.java)
                finishAffinity()
            }


            if (tag == ApiContants.GetStatus) {
                val allStatusBean = apiClient.getConvertIntoModel<GetAllStatusBean>(
                    jsonElement.toString(),
                    GetAllStatusBean::class.java
                )
          /*      if (allStatusBean.error == false) {
                    SalesApp.allStatusList.clear()
                    SalesApp.allStatusList.addAll(allStatusBean.data)
                }*/
                if (allStatusBean.error==false) {
                //    dayStatus = allStatusBean.dayStatus
                   handleRcStatus(allStatusBean.data)
                }
            }



        } catch (e: Exception) {
            Log.d("error>>", e.localizedMessage)
        }

    }

    override fun failure(tag: String?, errorMessage: String) {
        apiClient.progressView.hideLoader()
        // Toast.makeText(this, "4", Toast.LENGTH_SHORT).show()
        Utility.showSnackBar(this, errorMessage)
    }

    fun handleRcStatus(data: List<GetAllStatusBean.Data>) {
        rcStatus.layoutManager = LinearLayoutManager(this)
        var mAdapter = AllStatusAdapter(this, data, object :
            RvStatusClickListner {
            override fun clickPos(status: String, id: Int) {
                startActivity(
                    Intent(
                        this@DashboardActivity,
                        CustTelecallerActivity::class.java
                    ).putExtra("status_id", id.toString()).putExtra("status_name", status)
                )

                binding.drawerLayout.closeDrawers()
            }
        })
        rcStatus.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    fun handleRcMaster() {
        rcMaster.layoutManager = LinearLayoutManager(this)
        var mAdapter = CommonFieldDrawerAdapter(this, getMaster(), object :
            RvClickListner {
            override fun clickPos(pos: Int) {
                if (pos == 0) {
                    binding.drawerLayout.closeDrawers()
                }else if (pos == 1) {
                    startActivity(
                        Intent(
                            this@DashboardActivity,
                            CreateExpensesActivity::class.java
                        ).putExtra("way","CreateExpenses")
                    )
                } else if (pos == 2) {
                    startActivity(
                        Intent(
                            this@DashboardActivity,
                            AllCustomerTypeActivity::class.java
                        ).putExtra("customerType","customer")
                    )
                } else if (pos == 3) {
                    startActivity(
                        Intent(
                            this@DashboardActivity,
                            AllCustomerTypeActivity::class.java
                        ).putExtra("customerType","visitor")
                    )

                }  else if (pos == 4) {
                    startActivity(
                        Intent(
                            this@DashboardActivity,
                            ContactListActivity::class.java
                        )
                    )
                } /*else if (pos == 5) {
                    startActivity(
                        Intent(
                            this@DashboardActivity,
                            CustTelecallerActivity::class.java
                        )
                    )

                }*/
                else if (pos == 5) {
                    startActivity(
                        Intent(
                            this@DashboardActivity,
                            AllExpensesActivity::class.java
                        )
                    )

                }else if (pos == 6) {
                    startActivity(
                        Intent(
                            this@DashboardActivity,
                            ContactListActivity::class.java
                        )
                    )

                }else if (pos == 7) {
                    startActivity(
                        Intent(
                            this@DashboardActivity,
                            PasswordChnageActivity::class.java
                        )
                    )

                } else if (pos == 8) {

                }
                binding.drawerLayout.closeDrawers()
            }

        })
        rcMaster.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    private fun getMaster(): ArrayList<MenuModelBean> {
        var menuList = ArrayList<MenuModelBean>()

            menuList.add(MenuModelBean(0, "Dashboard", "", R.drawable.ic_dashbord))
            menuList.add(MenuModelBean(1, "Create Expense", "", R.drawable.ic_dashbord))
        //    menuList.add(MenuModelBean(4, "Team Contact", "", R.drawable.ic_dashbord))
            menuList.add(MenuModelBean(5, "All Expenses", "", R.drawable.ic_dashbord))
            menuList.add(MenuModelBean(6, "All Contact", "", R.drawable.ic_dashbord))
            menuList.add(MenuModelBean(7, "Password Changed", "", R.drawable.ic_dashbord))
            menuList.add(MenuModelBean(8, "Logout", "", R.drawable.ic_dashbord))


        return menuList
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
             //   ApiContants.getLocation(mFusedLocationClient,this)
            }
        } else {
            //  checkPermissions()
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