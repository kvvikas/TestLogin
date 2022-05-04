package com.example.testlogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_phone_no.*


class PhoneNoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_no)
        showToolbar()
        buttonContinue.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val mobile: String = editTextMobile.text.toString().trim()
                if (mobile.isEmpty() || mobile.length < 10) {
                    editTextMobile.error = "Enter a valid mobile"
                    editTextMobile.requestFocus()
                    return
                }
                val intent = Intent(this@PhoneNoActivity, VerifyPhoneActivity::class.java)
                intent.putExtra("mobile", mobile)
                startActivity(intent)
            }
        })
    }
    private fun showToolbar() {
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Login with Phone No"
            toolBar.setNavigationOnClickListener { onBackPressed() }
        }
    }
}