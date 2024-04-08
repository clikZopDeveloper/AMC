package com.example.amc_app.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.amc_app.R
import com.example.amc_app.Model.CustomerListBean

import com.example.amc_app.Utills.RvStatusComplClickListner


class CustomerListAdapter(
    var context: Activity,
    var list: List<CustomerListBean.Data>,
    var rvClickListner: RvStatusComplClickListner
) : RecyclerView.Adapter<CustomerListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder { // infalte the item Layout
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_customer_list, parent, false)
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


        holder.tvName.text = list[position].name
        holder.tvMobile.text = list[position].mobile
        holder.tvEmailID.text = list[position].email
        holder.tvDOB.text = list[position].dob.toString()
        holder.tvDOA.text = list[position].gstNo.toString()
        holder.tvCustomerType.text = list[position].anniversary.toString()
        holder.tvDate.text = list[position].createdDate.toString()
        holder.tvAddress.text = list[position].billingAddress.toString()
        holder.tvCity.text = list[position].shippingAddress.toString()
        holder.tvState.text = list[position].area.toString()
        holder.tvRemark.text = list[position].isActive?.toString()


        holder.itemView.setOnClickListener {
            rvClickListner.clickPos("","","",list[position].id)
        }

        holder.ivuUpdate.setOnClickListener {
          /*  context.startActivityForResult(
                Intent(context,CreateExpensesActivity::class.java)
                .putExtra("custResponse",list[position])
                .putExtra("way","UpdateCustomer")
                .putExtra("cust_ID",list[position].id.toString())
                ,101  )*/
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tvName: TextView = itemview.findViewById(R.id.tvName)
        val tvMobile: TextView = itemview.findViewById(R.id.tvMobile)
        val tvEmailID: TextView = itemview.findViewById(R.id.tvEmailID)
        val tvDOB: TextView = itemview.findViewById(R.id.tvDOB)
        val tvDOA: TextView = itemview.findViewById(R.id.tvDOA)
        val tvDate: TextView = itemview.findViewById(R.id.tvDate)
        val tvCustomerType: TextView = itemview.findViewById(R.id.tvCustomerType)
        val tvAddress: TextView = itemview.findViewById(R.id.tvAddress)
        val tvCity: TextView = itemview.findViewById(R.id.tvCity)
        val tvState: TextView = itemview.findViewById(R.id.tvState)
        val tvRemark: TextView = itemview.findViewById(R.id.tvRemark)
        val ivuUpdate: ImageView = itemview.findViewById(R.id.ivuUpdate)

    }

}