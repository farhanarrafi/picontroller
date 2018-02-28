package com.farhanarrafi.picontroller.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable


@IgnoreExtraProperties
class Components
    constructor(var id: String = "",
                var name: String = "",
                var description: String = "",
                var status: Boolean = true,
                var type: String = "") : Serializable