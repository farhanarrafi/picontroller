package com.farhanarrafi.picontroller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.farhanarrafi.picontroller.model.Components

class RoomUpdateBroadcastReceiver(private var roomDataUpdater: RoomDataUpdater) : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent!!.action == Constants.COMPONENT_SINGLE_CHILD_UPDATE) {
            //Log.d("RoomUpdateBroadReceiver", "onReceive")
            val component = intent.getSerializableExtra("component") as Components
            roomDataUpdater.updateComponentStatus(component)
        } else if (intent.action == Constants.COMPONENT_SINGLE_CHILD_ADDED) {
            //Log.d("RoomUpdateBroadReceiver", "onReceive")
            val component = intent.getSerializableExtra("component") as Components
            roomDataUpdater.addComponent(component)
        } else if (intent.action == Constants.COMPONENT_SINGLE_CHILD_REMOVED) {
            //Log.d("RoomUpdateBroadReceiver", "onReceive")
            val component = intent.getSerializableExtra("component") as Components
            roomDataUpdater.removeComponent(component)
        }
    }
}