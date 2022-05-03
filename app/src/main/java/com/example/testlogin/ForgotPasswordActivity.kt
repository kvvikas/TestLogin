package com.example.testlogin

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        auth = FirebaseAuth.getInstance()
        showToolbar()
        btnBack.setOnClickListener {
            finish()
        }

        btnResetPassword.setOnClickListener {
            var email: String = etUserEmail.text.toString()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter email id", Toast.LENGTH_LONG).show()
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            Toast.makeText(this, "Unable to send reset mail", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            }
        }
    }

    private fun showToolbar() {
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Forgot Password"
            toolBar.setNavigationOnClickListener { onBackPressed() }
        }
    }
}