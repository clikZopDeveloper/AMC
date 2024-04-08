package com.example.amc_app.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.amc_app.R
import com.example.amc_app.Model.ContactListBean
import com.example.amc_app.Utills.GeneralUtilities

import com.example.amc_app.Utills.RvStatusClickListner


class ContactListAdapter(var context: Activity, var list: List<ContactListBean.Data>, var rvClickListner: RvStatusClickListner) : RecyclerView.Adapter<ContactListAdapter.MyViewHolder>(),
    Filterable {
    var mFilteredList: MutableList<ContactListBean.Data> = list as MutableList<ContactListBean.Data>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder { // infalte the item Layout
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_contact_list, parent, false)
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

        holder.tvName.text = mFilteredList[position].name
        holder.tvMobile.text = mFilteredList[position].phone
        holder.tvEmailID.text = mFilteredList[position].email
        holder.tvDesignation.text = mFilteredList[position].designation
holder.ivCall.setOnClickListener {
    GeneralUtilities.makeCall(context, mFilteredList[position].phone)

}
       // holder.ivImage.setImageDrawable(context.resources.getDrawable(list[position].drawableId))

      /*  if ("Retailer"=="Retailer"){
      //      holder.itemView.visibility=View.GONE
        }*/

   /*     Glide.with(context)
            .load(ApiContants.ImgBaseUrl+list[position].imgUrl)
            .into(holder.ivImage)*/

    }

    override fun getItemCount(): Int {
        return mFilteredList.size
    }

    inner class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val tvName: TextView = itemview.findViewById(R.id.tvName)
        val tvMobile: TextView = itemview.findViewById(R.id.tvMobile)
        val tvEmailID: TextView = itemview.findViewById(R.id.tvEmailID)
        val tvDesignation: TextView = itemview.findViewById(R.id.tvDesignation)
        val ivCall: ImageView = itemview.findViewById(R.id.ivCall)

    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    mFilteredList = list as MutableList<ContactListBean.Data>
                } else {
                    val filteredList = ArrayList<ContactListBean.Data>()
                    for (serviceBean in list) {
                        if (serviceBean.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(serviceBean)
                        }
                    }
                    mFilteredList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = mFilteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                mFilteredList = filterResults.values as ArrayList<ContactListBean.Data>
                android.os.Handler().postDelayed(Runnable { notifyDataSetChanged() }, 200)
            }
        }
    }
}