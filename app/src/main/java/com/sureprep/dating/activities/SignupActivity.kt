package com.sureprep.dating.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sureprep.dating.R
import com.sureprep.dating.User
import com.sureprep.dating.util.DATA_USERS
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private val firebaseDatabase = FirebaseDatabase.getInstance().reference
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser
        if(user != null) {
            startActivity(DatingActivity.newIntent(this))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener { firebaseAuthListener }
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener { firebaseAuthListener }
    }

    // Signup function. Gets the email from email activity, password from email activity, If task fails, shows error message
    fun onSignup(v: View) {
        if(!emailET.text.toString().isNullOrEmpty() && !passwordET.text.toString().isNullOrEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(emailET.text.toString(), passwordET.text.toString())
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val email = emailET.text.toString()
                        val userId = firebaseAuth.currentUser?.uid ?: ""
                        val user = User(userId, "","", email,"","", "","", "")
                        firebaseDatabase.child(DATA_USERS).child(userId).setValue(user)
                        startActivity(DatingActivity.newIntent(this))
                    }
                    else {
                        Toast.makeText(this, "Signup error ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    companion object {
        fun newIntent(context: Context?) = Intent(context, SignupActivity::class.java)
    }
}
