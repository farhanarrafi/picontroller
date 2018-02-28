package com.farhanarrafi.picontroller

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.farhanarrafi.picontroller.model.Component

class ComponentViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
    val iv_component_image: ImageView = itemView!!.findViewById(R.id.component_image)
    val tv_component_name: TextView = itemView!!.findViewById(R.id.component_name)
    val sw_component_switch: Switch = itemView!!.findViewById(R.id.component_switch)
    val sw_component_description: TextView = itemView!!.findViewById(R.id.component_description)

    fun getview() : View {
        return itemView
    }

    fun bind(component: Component) {
        tv_component_name.text = component.name
        sw_component_description.text = component.description
        sw_component_switch.isChecked = component.status
        if(component.status) {
            iv_component_image.setImageResource(R.drawable.light_on)
        } else {
            iv_component_image.setImageResource(R.drawable.light_off)
        }

    }
}