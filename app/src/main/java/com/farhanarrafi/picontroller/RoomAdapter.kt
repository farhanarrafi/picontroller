package com.farhanarrafi.picontroller

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.farhanarrafi.picontroller.model.Components


class RoomAdapter(private var componentList: ArrayList<Components>) : RecyclerView.Adapter<RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RoomViewHolder {

        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.list_view_item,parent,false)

        return RoomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return componentList.size
    }

    override fun onBindViewHolder(holder: RoomViewHolder?, position: Int) {
        val component: Components = componentList[position]

        holder!!.tv_component_name.text = component.name
        holder.sw_component_description.text = component.description
        holder.sw_component_switch.isChecked = component.status
        if(component.status) {
            holder.iv_component_image.setImageResource(R.drawable.light_on)
        } else {
            holder.iv_component_image.setImageResource(R.drawable.light_off)
        }

    }
}