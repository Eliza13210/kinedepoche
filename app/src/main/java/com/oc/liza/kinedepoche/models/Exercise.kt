package com.oc.liza.kinedepoche.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(entity = ExerciseDate::class,
            parentColumns = ["id"],
            childColumns = ["dateId"],
            onDelete = ForeignKey.CASCADE)])
class Exercise(@PrimaryKey(autoGenerate = true) val id: Long? = null,
               var dateId: Long?,
               var url: String, var description: String, var nav_text: String,
               var time: Int, var repeat: Int, var completed: Boolean = false)