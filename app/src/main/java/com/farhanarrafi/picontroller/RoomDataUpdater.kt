package com.farhanarrafi.picontroller

import com.farhanarrafi.picontroller.model.Components

interface RoomDataUpdater {
    fun updateComponentStatus(component: Components)
    fun addComponent(component: Components)
    fun removeComponent(component: Components)
}