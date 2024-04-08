package com.example.amc_app.Adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.amc_app.R
import com.example.amc_app.Model.CategoryBean
import com.example.amc_app.Utills.RvListClickListner

import com.google.gson.Gson


class CategoryAdapter(var context: Activity, var list: List<CategoryBean.Data>, var rvClickListner: RvListClickListner) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>(){
    private val data = mutableListOf<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder { // infalte the item Layout
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
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


        holder.tvName.text= list[position].name
      //  holder.tvDate.text= list[position].createdAt.toString()
        holder.ivCheck.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                data.add(list[position].id)
            } else {
                data.remove(list[position].id)
            }

            Log.d("zxczxc", Gson().toJson(data))
            rvClickListner.clickPos(data, list[position].id)
        }
       // holder.ivImage.setImageDrawable(context.resources.getDrawable(list[position].drawableId))

      /*  if ("Retailer"=="Retailer"){
      //      holder.itemView.visibility=View.GONE
        }*/
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tvName: TextView = itemview.findViewById(R.id.tvName)
        val tvDate: TextView = itemview.findViewById(R.id.tvDate)
        val ivCheck: CheckBox = itemview.findViewById(R.id.ivCheck)

    }

}