package com.example.adminparkinson.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminparkinson.Activities.WorkoutListActivity
import com.example.adminparkinson.R
import com.example.adminparkinson.model.Workout
import kotlinx.android.synthetic.main.item_workout.view.*

open class WorkoutListAdapter(private val context: Context, private var list: ArrayList<Workout>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener : OnClickListener? = null
    private var activate: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_workout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_workout_place_holder)
                .into(holder.itemView.civ_workout_image)

            holder.itemView.tv_name.text = model.name
            holder.itemView.tv_created_by.text = "Created by:  ${model.createdBy}"

            if (activate) {
                holder.itemView.btn_assign_user.visibility = View.GONE
            }


            holder.itemView.btn_assign_user.setOnClickListener{
                if (context is WorkoutListActivity) {
                    context.assignUser(model)
                }
            }

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }


        }

    }

    open fun activateButtons(activate: Boolean) {
        this.activate = activate
        notifyDataSetChanged()
    }

    interface OnClickListener{
        fun onClick(position: Int, model: Workout)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}