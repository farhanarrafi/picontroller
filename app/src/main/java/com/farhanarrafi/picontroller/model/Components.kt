package com.farhanarrafi.picontroller.model

import android.graphics.drawable.Drawable


class Components {
    var name: String
    var description: String
    var status: Boolean = false
    var image: Drawable

    constructor(image: Drawable, name: String, description: String, status: Boolean) {
        this.name = name
        this.description = description
        this.status = status
        this.image = image
    }
}