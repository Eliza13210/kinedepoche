package com.oc.liza.kinedepoche.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE)])
class ExerciseDate(@PrimaryKey(autoGenerate = true) val id: Long? = null,
                   var userId: Long,
                   var date: String,
                   var progress: Int = 0,
                   var exOne: Boolean = false,
                   var exTwo: Boolean = false,
                   var exThree: Boolean = false,
                   var exFour: Boolean = false,
                   var exFive: Boolean = false)