package com.oc.liza.kinedepoche.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(@PrimaryKey(autoGenerate = true) val id: Long? = null,
                var name: String= "Your user name",
                var reminder: Boolean = false,
                var last_exercise: Int = 1
)