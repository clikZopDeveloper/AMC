package com.example.amc_app.Adapter

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.amc_app.Model.MultipleProductBean
import com.example.amc_app.Model.ProjectDetailBean
import com.example.amc_app.R
import com.example.amc_app.Utills.RvCreateOrderClickListner
import com.google.gson.Gson
import com.stpl.antimatter.Utils.ApiContants


class ProjectDetailListAdapter(var context: Activity, var list: List<ProjectDetailBean.Data.ProjectService>, var rvClickListner: RvCreateOrderClickListner) : RecyclerView.Adapter<ProjectDetailListAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder { // infalte the item Layout
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_order_detail_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setIsRecyclable(false)

   /*     holder.tvAdd.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(20f))
        holder.tvQtyAdd.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(100f))
        holder.tvQtyMinus.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(100f))
        holder.tvQty.background = RoundView(Color.TRANSPARENT, RoundView.getRadius(20f), true, R.color.orange)
        holder.tvOff.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(20f))
        holder.tvAdd.visibility = View.VISIBLE*/

      //  val currentItem = list[position]
        holder.tvServiceName.text= list.get(0).serviceName.toString()
        holder.tvServicePrice.text=ApiContants.currency+list.get(0).servicePrice.toString()
        holder.tvDate.text= list.get(0).createdAt.toString()

        holder.itemView.setOnClickListener {
          //  rvClickListner.clickPos(listw,list[position].id)

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tvServiceName: TextView = itemview.findViewById(R.id.tvServiceName)
        val tvServicePrice: TextView = itemview.findViewById(R.id.tvServicePrice)
        val tvDate: TextView = itemview.findViewById(R.id.tvDate)

    }
}