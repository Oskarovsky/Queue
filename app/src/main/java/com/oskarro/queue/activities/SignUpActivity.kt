package com.oskarro.queue.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.oskarro.queue.R
import com.oskarro.queue.firebase.FirebaseUtils
import com.oskarro.queue.model.User
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setupActionBar()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    fun userRegisteredSuccess() {
        Toast.makeText(this, "You have successfully registered", Toast.LENGTH_SHORT).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()

    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_sign_up_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
        }
        toolbar_sign_up_activity.setNavigationOnClickListener { onBackPressed() }

        btn_page_sign_up.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val name: String = et_name_sign_up.text.toString().trim { it <= ' ' }
        val email: String = et_email_sign_up.text.toString().trim { it <= ' ' }
        val password: String = et_password_sign_up.text.toString().trim { it <= ' ' }

        if (validateForm(name, email, password)) {
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    val registeredEmail = firebaseUser.email!!
                    val user = User(firebaseUser.uid, name, registeredEmail)
                    FirebaseUtils().registerUser(this, user)
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter a name")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter a email")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a password")
                false
            } else -> {
                true
            }
        }
    }

}