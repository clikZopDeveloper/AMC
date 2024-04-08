package com.example.amc_app.Adapter

import android.app.Activity
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.amc_app.Model.GetProjectBean
import com.example.amc_app.R

import com.example.amc_app.Utills.RvStatusComplClickListner
import com.example.amc_app.databinding.ItemProjectListBinding
import com.stpl.antimatter.Utils.ApiContants


class ProjectListAdapter(
    var context: Activity,
    var list: List<GetProjectBean.Data>,
    var rvClickListner: RvStatusComplClickListner
) : RecyclerView.Adapter<ProjectListAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ItemProjectListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder { // infalte the item Layout
        val binding =
            ItemProjectListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //   val v = LayoutInflater.from(parent.context).inflate(R.layout.item_project_list, parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.binding.apply {
            tvProjectName.text = list[position].projectAmt
            tvPrice.text = ApiContants.currency + list[position].customerId.toString()
            tvDate.text = list[position].amcDate.toString()
            tvStatus.text = list[position].status.toString()

            tvPaymentStatus.text = list[position].paymentStatus.toString()
            if (list[position].paymentStatus.toString().equals("Pending"))
                tvPaymentStatus.setTextColor(context.getResources().getColor(R.color.yellow_color));
            else
                tvPaymentStatus.setTextColor(context.getResources().getColor(R.color.green));


            if (list[position].status.toString().equals("Active"))
                tvStatus.background.colorFilter =
                    BlendModeColorFilter(
                        context.getResources().getColor(R.color.green),
                        BlendMode.SRC_ATOP
                    )
           /* else if (list[position].paymentStatus.toString().equals("Success"))
                tvStatus.background.colorFilter =
                    BlendModeColorFilter(
                        context.getResources().getColor(R.color.green),
                        BlendMode.SRC_ATOP
                    )*/
            else
                tvStatus.background.colorFilter =
                    BlendModeColorFilter(
                        context.getResources().getColor(R.color.yellow_color),
                        BlendMode.SRC_ATOP
                    )

        }

        /*     holder.tvAdd.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(20f))
             holder.tvQtyAdd.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(100f))
             holder.tvQtyMinus.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(100f))
             holder.tvQty.background = RoundView(Color.TRANSPARENT, RoundView.getRadius(20f), true, R.color.orange)
             holder.tvOff.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(20f))
             holder.tvAdd.visibility = View.VISIBLE*/


        holder.itemView.setOnClickListener {
            rvClickListner.clickPos("", "", "", list[position].id)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}