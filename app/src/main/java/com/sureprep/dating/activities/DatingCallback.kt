package com.sureprep.dating.activities

import com.google.firebase.database.DatabaseReference

interface DatingCallback {
    fun onSignout()

    fun onGetUserId(): String

    fun getUserDatabase(): DatabaseReference

    fun profileComplete()

    fun startActivityForPhoto()
}