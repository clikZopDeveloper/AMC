package com.example.amc_app.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.amc_app.Model.MenuModelBean
import com.example.amc_app.R

import com.example.amc_app.Utills.RvStatusComplClickListner
import com.google.android.material.button.MaterialButton


class ExpenseCategoryAdapter(var context: Activity, var list: ArrayList<MenuModelBean>, var rvClickListner: RvStatusComplClickListner) : RecyclerView.Adapter<ExpenseCategoryAdapter.MyViewHolder>(){
    private var checkedPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder { // infalte the item Layout
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_categoryes, parent, false)
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


        holder.tvCategories.text= list[position].title


       // holder.ivImage.setImageDrawable(context.resources.getDrawable(list[position].drawableId))

        if(checkedPosition == position){
           // holder.ivChecked.visibility=View.VISIBLE
            holder.tvCategories.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
        }else{
            holder.tvCategories.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.v_blue_color));

         //   holder.ivChecked.visibility=View.GONE
        }
        holder.tvCategories.setOnClickListener {
            checkedPosition = position
            holder.tvCategories.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));

          //  holder.ivChecked.visibility=View.VISIBLE
            notifyDataSetChanged();
            rvClickListner.clickPos(list[position].title,"","",0)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tvCategories: MaterialButton = itemview.findViewById(R.id.tvCategories)

    }

}