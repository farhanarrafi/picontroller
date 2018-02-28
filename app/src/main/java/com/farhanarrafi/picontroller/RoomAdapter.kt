package com.farhanarrafi.picontroller

import android.view.LayoutInflater
import android.view.ViewGroup
import com.farhanarrafi.picontroller.model.Component
import com.farhanarrafi.picontroller.utils.DatabaseUtil
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


class RoomAdapter(options: FirebaseRecyclerOptions<Component>) : FirebaseRecyclerAdapter<Component, ComponentViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ComponentViewHolder {

        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.list_view_item,parent,false)
        return ComponentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int, model: Component) {
        holder.bind(model)
        holder.sw_component_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            var firebaseDatabase = DatabaseUtil.getDatabase()
            var componentsDB = firebaseDatabase.getReference("components")
            if(isChecked) {
                componentsDB.child(position.toString()).child("status").setValue(true)
                //holder.iv_component_image.setImageResource(R.drawable.light_on)
            } else {
                componentsDB.child(position.toString()).child("status").setValue(false)
                //holder.iv_component_image.setImageResource(R.drawable.light_off)
            }
        }
    }
}