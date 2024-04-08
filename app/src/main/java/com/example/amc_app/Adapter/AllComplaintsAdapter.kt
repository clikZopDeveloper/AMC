package com.example.amc_app.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.amc_app.Model.AllComplaintsBean
import com.example.amc_app.R

import com.example.amc_app.Utills.RvStatusComplClickListner


class AllComplaintsAdapter(var context: Activity, var list: List<AllComplaintsBean.Data>, var rvClickListner: RvStatusComplClickListner) : RecyclerView.Adapter<AllComplaintsAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder { // infalte the item Layout
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_complaints, parent, false)
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


        holder.tvName.text= list[position].customerName
        holder.tvMobile.text=list[position].mobile+"/"+list[position].secondaryMobile
        holder.tvDepartment.text=list[position].address
        holder.tvcoment.text=list[position].comment
        holder.tvServiceType.text=list[position].serviceType
        holder.tvStatus.text=list[position].status
        holder.tvProducts.text=list[position].products
        holder.tvComplainDescription.text=list[position].complainDescription
        holder.tvRecommendation.text=list[position].recommendation
        holder.tvSuggestion.text=list[position].suggestion

        holder.tvDate.text= list[position].lastUpdated.toString()

       // holder.ivImage.setImageDrawable(context.resources.getDrawable(list[position].drawableId))

      /*  if ("Retailer"=="Retailer"){
      //      holder.itemView.visibility=View.GONE
        }*/
if (!list[position].lastWorkStatus.isNullOrEmpty()){
    if (list[position].lastWorkStatus.equals("start")){
        holder.tvStart.visibility=View.GONE
        holder.tvInprogress.visibility=View.VISIBLE
        holder.tvStop.visibility=View.VISIBLE
        holder.tvCompleted.visibility=View.GONE
        holder.tvRejected.visibility=View.GONE
    }else if (list[position].lastWorkStatus.equals("stop")){
        holder.tvStart.visibility=View.GONE
        holder.tvInprogress.visibility=View.GONE
        holder.tvStop.visibility=View.GONE
        holder.tvCompleted.visibility=View.VISIBLE
        holder.tvRejected.visibility=View.VISIBLE
    }else if (list[position].lastWorkStatus.equals("completed")){
        holder.tvStart.visibility=View.GONE
        holder.tvInprogress.visibility=View.GONE
        holder.tvStop.visibility=View.GONE
        holder.tvCompleted.visibility=View.GONE
        holder.tvRejected.visibility=View.GONE
    }else if (list[position].lastWorkStatus.equals("under_process")){
        holder.tvStart.visibility=View.GONE
        holder.tvInprogress.visibility=View.GONE
        holder.tvResume.visibility=View.VISIBLE
        holder.tvStop.visibility=View.VISIBLE
        holder.tvCompleted.visibility=View.GONE
        holder.tvRejected.visibility=View.GONE
    }else{
        holder.tvStart.visibility=View.GONE
        holder.tvInprogress.visibility=View.GONE
        holder.tvStop.visibility=View.GONE
        holder.tvCompleted.visibility=View.GONE
        holder.tvRejected.visibility=View.GONE
    }
}

        holder.tvStart.setOnClickListener {
            rvClickListner.clickPos("processing","start","",list[position].id)
        }

        holder.tvInprogress.setOnClickListener {
            rvClickListner.clickPos("processing","under_process","",list[position].id)
        }

        holder.tvStop.setOnClickListener {
            rvClickListner.clickPos("processing","stop","",list[position].id)
        }

        holder.tvCompleted.setOnClickListener {
            rvClickListner.clickPos("completed","completed",list[position].payableAmt,list[position].id)
        }

      holder.tvResume.setOnClickListener {
            rvClickListner.clickPos("processing","start",list[position].payableAmt,list[position].id)
        }

        holder.tvRejected.setOnClickListener {
            rvClickListner.clickPos("rejected","rejected","",list[position].id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tvName: TextView = itemview.findViewById(R.id.tvName)
        val tvMobile: TextView = itemview.findViewById(R.id.tvMobile)
        val tvDepartment: TextView = itemview.findViewById(R.id.tvDepartment)
        val tvComplainDescription: TextView = itemview.findViewById(R.id.tvComplainDescription)
        val tvRecommendation: TextView = itemview.findViewById(R.id.tvRecommendation)

        val tvDate: TextView = itemview.findViewById(R.id.tvDate)
        val tvStatus: TextView = itemview.findViewById(R.id.tvStatus)
        val tvcoment: TextView = itemview.findViewById(R.id.tvcoment)
        val tvServiceType: TextView = itemview.findViewById(R.id.tvServiceType)
        val tvProducts: TextView = itemview.findViewById(R.id.tvProducts)
        val tvStart: TextView = itemview.findViewById(R.id.tvStart)
        val tvSuggestion: TextView = itemview.findViewById(R.id.tvSuggestion)
        val tvInprogress: TextView = itemview.findViewById(R.id.tvInprogress)
        val tvStop: TextView = itemview.findViewById(R.id.tvStop)
        val tvCompleted: TextView = itemview.findViewById(R.id.tvCompleted)
        val tvRejected: TextView = itemview.findViewById(R.id.tvRejectess)
        val tvResume: TextView = itemview.findViewById(R.id.tvResume)

    }

}