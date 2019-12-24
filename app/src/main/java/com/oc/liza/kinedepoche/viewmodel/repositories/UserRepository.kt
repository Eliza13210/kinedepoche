package com.oc.liza.kinedepoche.viewmodel.repositories

import androidx.lifecycle.LiveData
import com.oc.liza.kinedepoche.database.dao.UserDao
import com.oc.liza.kinedepoche.models.User

class UserRepository(val userDao: UserDao) {

    // --- GET ---
    fun getUser(userId: Long): LiveData<User> {
        return this.userDao.getUser(userId)
    }

    fun getUserByName(userName: String): LiveData<User> {
        return this.userDao.getUserByName(userName)
    }

    fun getAllUsers(): LiveData<MutableList<User>>? {
        return this.userDao.getAllUsers()
    }

    // --- CREATE ---

    fun createUser(user: User) {
        userDao.createUser(user)
    }

    // --- DELETE ---
    fun deleteUser(userId: Long) {
        userDao.deleteUser(userId)
    }

    // --- UPDATE ---
    fun updateUser(user: User) {
        userDao.updateUser(user)
    }
}