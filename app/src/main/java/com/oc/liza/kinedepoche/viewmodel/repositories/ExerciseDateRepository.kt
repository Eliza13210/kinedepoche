package com.oc.liza.kinedepoche.viewmodel.repositories

import androidx.lifecycle.LiveData
import com.oc.liza.kinedepoche.database.dao.DateDao
import com.oc.liza.kinedepoche.models.ExerciseDate

class ExerciseDateRepository(val dateDao: DateDao) {
    // --- GET ---
    fun getDates(userId: Long): LiveData<List<ExerciseDate>> {
        return this.dateDao.getDates(userId)
    }

    fun getDate(date: String): LiveData<ExerciseDate> {
        return this.dateDao.getDate(date)
    }

    // --- CREATE ---
    fun createDate(date: ExerciseDate) {
        dateDao.insertDate(date)
    }

    // --- DELETE ---
    fun deleteDate(dateId: Long) {
        dateDao.deleteDate(dateId)
    }

    // --- UPDATE ---
    fun updateDate(date: ExerciseDate) {
        dateDao.updateDate(date)
    }
}