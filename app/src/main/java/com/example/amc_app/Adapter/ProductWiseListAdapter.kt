package com.example.amc_app.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.amc_app.Model.ProductWiseReportBean

import com.example.amc_app.Utills.RvStatusComplClickListner
import com.stpl.antimatter.Utils.ApiContants


class ProductWiseListAdapter(
    var context: Activity,
    var list: List<ProductWiseReportBean.Data>,
    var rvClickListner: RvStatusComplClickListner
) : RecyclerView.Adapter<ProductWiseListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder { // infalte the item Layout
        val v =
            LayoutInflater.from(parent.context).inflate(com.example.amc_app.R.layout.item_product_wise, parent, false)
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


        holder.tvClientName.text = list[position].productName
        holder.tvType.text = list[position].type
        holder.tvColor.text = list[position].color
        holder.tvCatName.text = list[position].categoryName.toString()
        holder.tvQty.text = list[position].qty?.toString()
        holder.tvAmount.text = ApiContants.currency+list[position].amount.toString()



    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tvClientName: TextView = itemview.findViewById(com.example.amc_app.R.id.tvClientName)
        val tvType: TextView = itemview.findViewById(com.example.amc_app.R.id.tvType)
        val tvColor: TextView = itemview.findViewById(com.example.amc_app.R.id.tvColor)
        val tvCatName: TextView = itemview.findViewById(com.example.amc_app.R.id.tvCatName)
        val tvQty: TextView = itemview.findViewById(com.example.amc_app.R.id.tvQty)
        val tvAmount: TextView = itemview.findViewById(com.example.amc_app.R.id.tvAmount)


    }

}