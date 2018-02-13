package com.farhanarrafi.picontroller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class RoomUpdateBroadcastReceiver(private var roomDataUpdater: RoomDataUpdater) : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent!!.action.equals(Constants.COMPONENT_SINGLE_CHILD_UPDATE)) {
            Log.d("RoomUpdateBroadReceiver", "onReceive")
            val id = intent.getStringExtra("id")
            val status = intent.getBooleanExtra("status",false)
            roomDataUpdater.updateRoomStatus(id, status)
        }

        if (intent!!.action.equals(Constants.COMPONENT_SINGLE_CHILD_ADDED)) {
            Log.d("RoomUpdateBroadReceiver", "onReceive")
            val id = intent.getStringExtra("id")
            val status = intent.getBooleanExtra("status",false)
            roomDataUpdater.updateRoomStatus(id, status)
        }
    }
}