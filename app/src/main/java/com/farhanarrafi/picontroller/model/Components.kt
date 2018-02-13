package com.farhanarrafi.picontroller.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Components
    constructor(var id: String = "",
                var name: String = "",
                var description: String = "",
                var status: Boolean = true,
                var type: String = "")