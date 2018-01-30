package com.farhanarrafi.picontroller

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.farhanarrafi.picontroller.R.id.*
import com.farhanarrafi.picontroller.model.Components
import java.util.*

class RoomAdapter(private var componentList: ArrayList<Components>) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

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
        holder.iv_component_image.setImageDrawable(component.image)

    }

    class RoomViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        val iv_component_image: ImageView = itemView!!.findViewById(component_image)
        val tv_component_name: TextView = itemView!!.findViewById(component_name)
        val sw_component_switch: Switch = itemView!!.findViewById(component_switch)
        val sw_component_description: TextView = itemView!!.findViewById(component_description)
        init {
            itemView!!.setOnClickListener {
                toggle()
            }
        }

        private fun toggle() {
            itemView.findViewById<Switch>(component_switch).toggle()
            if(itemView.findViewById<Switch>(component_switch).isChecked) {
                itemView.findViewById<ImageView>(component_image).setImageResource(R.drawable.light_on)
            } else {
                itemView.findViewById<ImageView>(component_image).setImageResource(R.drawable.light_off)
            }
        }
    }



}