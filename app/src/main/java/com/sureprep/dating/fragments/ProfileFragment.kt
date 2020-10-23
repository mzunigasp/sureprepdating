package com.sureprep.dating.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.sureprep.dating.R
import com.sureprep.dating.User
import com.sureprep.dating.activities.DatingCallback
import com.sureprep.dating.util.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var userDatabase: DatabaseReference
    private var callback: DatingCallback? = null

    fun setCallback(callback: DatingCallback) {
        this.callback = callback
        userId = callback.onGetUserId()
        userDatabase = callback.getUserDatabase().child(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progress_layout.setOnTouchListener { v, event -> true }

        populateInfo()

        photoIV.setOnClickListener { callback?.startActivityForPhoto() }

        applyButton.setOnClickListener { onApply() }
        signoutButton.setOnClickListener { callback?.onSignout() }
    }

    fun populateInfo() {
        progress_layout.visibility = View.VISIBLE
        userDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                professional_status.visibility = View.GONE
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (isAdded) {
                    val user = p0.getValue(User::class.java)
                    nameET.setText(user?.name, TextView.BufferType.EDITABLE)
                    emailET.setText(user?.email, TextView.BufferType.EDITABLE)
                    ageET.setText(user?.age, TextView.BufferType.EDITABLE)
                    descriptionET.setText(user?.description, TextView.BufferType.EDITABLE)
                    if (user?.gender == GENDER_MALE) {
                        radioMale1.isChecked = true
                    }
                    if (user?.gender == GENDER_FEMALE) {
                        radioFemale1.isChecked = true
                    }
                    if (user?.gender == GENDER_NB) {
                        radioNonbinary1.isChecked = true
                    }
                    if (user?.preferredGender == GENDER_MALE) {
                        radioMale2.isChecked = true
                    }
                    if (user?.preferredGender == GENDER_FEMALE) {
                        radioFemale2.isChecked = true
                    }
                    if (user?.preferredGender == GENDER_NB) {
                        radioNonbinary2.isChecked = true
                    }
                    if (user?.professionalStatus == DATA_CPA) {
                        cpa.isChecked = true
                    }
                    if (user?.professionalStatus == DATA_TAXPAYER) {
                        taxpayer.isChecked = true
                    }
                    if(!user?.imageUrl.isNullOrEmpty()) {
                        populateImage(user?.imageUrl!!)
                    }
                    progress_layout.visibility = View.GONE
                }
            }

        })
    }

    fun onApply() {
        if (nameET.text.toString().isNullOrEmpty() ||
            emailET.text.toString().isNullOrEmpty() ||
            radioGroup1.checkedRadioButtonId == -1 ||
            radioGroup2.checkedRadioButtonId == -1
        ) {
            Toast.makeText(context, getString(R.string.error_profile_incomplete), Toast.LENGTH_SHORT).show()
        } else {
            val name = nameET.text.toString()
            val age = ageET.text.toString()
            val email = emailET.text.toString()
            val gender =
                if (radioMale1.isChecked) GENDER_MALE
                else if (radioFemale1.isChecked) GENDER_FEMALE
                else GENDER_NB
            val preferredGender =
                if (radioMale2.isChecked) GENDER_MALE
                else if (radioFemale2.isChecked) GENDER_FEMALE
                else  GENDER_NB
            val professionalStatus =
                if (cpa.isChecked) DATA_CPA
                else DATA_TAXPAYER
            val description = descriptionET.text.toString()

            userDatabase.child(DATA_NAME).setValue(name)
            userDatabase.child(DATA_AGE).setValue(age)
            userDatabase.child(DATA_EMAIL).setValue(email)
            userDatabase.child(DATA_GENDER).setValue(gender)
            userDatabase.child(DATA_GENDER_PREFERENCE).setValue(preferredGender)
            userDatabase.child(DATA_PROFESSIONAL_STATUS).setValue(professionalStatus)
            userDatabase.child(DATA_DESCRIPTION).setValue(description)

            callback?.profileComplete()
        }
    }

    fun updateImageUri(uri: String) {
        userDatabase.child(DATA_IMAGE_URL).setValue(uri)
        populateImage(uri)
    }

    fun populateImage(uri: String) {
        Glide.with(this)
            .load(uri)
            .into(photoIV)
    }

}