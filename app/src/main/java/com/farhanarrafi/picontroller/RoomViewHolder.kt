package com.farhanarrafi.picontroller

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView

class RoomViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
    val iv_component_image: ImageView = itemView!!.findViewById(R.id.component_image)
    val tv_component_name: TextView = itemView!!.findViewById(R.id.component_name)
    val sw_component_switch: Switch = itemView!!.findViewById(R.id.component_switch)
    val sw_component_description: TextView = itemView!!.findViewById(R.id.component_description)
    init {
        itemView!!.setOnClickListener {
            toggle()
        }
    }

    private fun toggle() {
        itemView.findViewById<Switch>(R.id.component_switch).toggle()
        if(itemView.findViewById<Switch>(R.id.component_switch).isChecked) {
            itemView.findViewById<ImageView>(R.id.component_image).setImageResource(R.drawable.light_on)
        } else {
            itemView.findViewById<ImageView>(R.id.component_image).setImageResource(R.drawable.light_off)
        }
    }
}