package com.example.amc_app.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amc_app.Activity.CreateOrderActivity
import com.example.amc_app.Activity.ProjectDetailActivity
import com.example.amc_app.Adapter.ProjectListAdapter
import com.example.amc_app.ApiHelper.ApiController
import com.example.amc_app.ApiHelper.ApiResponseListner
import com.example.amc_app.Model.GetProjectBean
import com.example.amc_app.R
import com.example.amc_app.Utills.RvStatusComplClickListner
import com.example.amc_app.Utills.SalesApp
import com.example.amc_app.Utills.Utility
import com.example.amc_app.databinding.FragmentOrderBinding
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class OrderFragment : Fragment(), ApiResponseListner {

    private var _binding: FragmentOrderBinding? = null
    private lateinit var apiClient: ApiController
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.appbarLayout.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.appbarLayout.ivMenu.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.appbarLayout.tvTitle.text = "My All Project"
        binding.fbAddMeting.setOnClickListener {
            startActivity(
                Intent(requireContext(), CreateOrderActivity::class.java).putExtra(
                    "way",
                    "CreateOrder"
                )
            )
        }
        apiClient = ApiController(requireContext(), this)
        apiAllProjectsList()
        return root
    }

    fun apiAllProjectsList() {
        SalesApp.isAddAccessToken = true
        val params = Utility.getParmMap()
        apiClient.progressView.showLoader()
        //  params["status"]=statusType
        apiClient.getApiPostCall(ApiContants.getProjects, params)

    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.getProjects) {
                val projectBean = apiClient.getConvertIntoModel<GetProjectBean>(
                    jsonElement.toString(),
                    GetProjectBean::class.java
                )
                if (projectBean.error == false) {
                    handleProjectList(projectBean.data)
                }
            }
        } catch (e: Exception) {

        }
    }

    override fun failure(tag: String?, errorMessage: String?) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(requireActivity(), errorMessage!!)
    }

    fun handleProjectList(data: List<GetProjectBean.Data>) {
        binding.rcTeamContactList.layoutManager = LinearLayoutManager(requireContext())
        var mAdapter = ProjectListAdapter(requireActivity(), data
        ) { status, workstatus, amt, id ->
            startActivity(
                Intent(
                    requireContext(),
                    ProjectDetailActivity::class.java
                ).putExtra("project_id", id.toString())
            )
        }
        binding.rcTeamContactList.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}