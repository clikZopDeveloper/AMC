package com.example.amc_app.Adapter

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.amc_app.Model.MettingListBean

import com.example.amc_app.Utills.RvStatusComplClickListner


class MettingListAdapter(
    var context: Activity,
    var list: List<MettingListBean.Data>,
    var rvClickListner: RvStatusComplClickListner
) : RecyclerView.Adapter<MettingListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder { // infalte the item Layout
        val v =
            LayoutInflater.from(parent.context).inflate(com.example.amc_app.R.layout.item_metting_list, parent, false)
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


        holder.tvClientName.text = list[position].clientName
        holder.tvMobile.text = list[position].clientContact
        holder.tvAddress.text = list[position].clientAddress.toString()
        holder.tvContactPerson.text = list[position].contactPerson
        holder.tvStartTime.text = list[position].startTime.toString()
        holder.tvStopTime.text = list[position].stopTime?.toString()
        holder.tvStartLocation.text = list[position].startLocation.toString()
        holder.tvStopLocation.text = list[position].stopLocation?.toString()
        holder.tvRemark.text = list[position].remarks?.toString()


        if (list[position].stopTime != null || !list[position].stopTime.isNullOrEmpty()) {
        //    val shapeDrawable =ShapeDrawable
           // shapeDrawable.paint.color = ContextCompat.getColor(context, com.example.sales_expertz.R.color.colorPrimary)
            holder.tvStopMeeting.background.setColorFilter(Color.parseColor("#008000"), PorterDuff.Mode.SRC_ATOP)
            holder.tvStopMeeting.setText("Meeting Over")

        } else {
            holder.tvStopMeeting.background.setColorFilter(Color.parseColor("#b90609"), PorterDuff.Mode.SRC_ATOP)
            holder.tvStopMeeting.setText("Stop Meeting")
            holder.tvStopMeeting.setOnClickListener {
                rvClickListner.clickPos("", "", "", list[position].id)
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tvClientName: TextView = itemview.findViewById(com.example.amc_app.R.id.tvClientName)
        val tvMobile: TextView = itemview.findViewById(com.example.amc_app.R.id.tvMobile)
        val tvContactPerson: TextView = itemview.findViewById(com.example.amc_app.R.id.tvContactPerson)
        val tvStartTime: TextView = itemview.findViewById(com.example.amc_app.R.id.tvStartTime)
        val tvStopTime: TextView = itemview.findViewById(com.example.amc_app.R.id.tvStopTime)
        val tvStartLocation: TextView = itemview.findViewById(com.example.amc_app.R.id.tvStartLocation)
        val tvStopLocation: TextView = itemview.findViewById(com.example.amc_app.R.id.tvStopLocation)
        val tvAddress: TextView = itemview.findViewById(com.example.amc_app.R.id.tvAddress)
        val tvRemark: TextView = itemview.findViewById(com.example.amc_app.R.id.tvRemark)
        val tvStopMeeting: TextView = itemview.findViewById(com.example.amc_app.R.id.tvStopMeeting)

    }

}