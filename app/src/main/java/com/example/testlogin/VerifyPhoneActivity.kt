package com.example.testlogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import kotlinx.android.synthetic.main.activity_verify_phone.*
import java.util.concurrent.TimeUnit

class VerifyPhoneActivity : AppCompatActivity() {
    //These are the objects needed
    //It is the verification id that will be sent to the user
    private var mVerificationId: String? = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone)
        showToolbar()
        //initializing objects
        mAuth = FirebaseAuth.getInstance()


        //getting mobile number from the previous activity
        //and sending the verification code to the number


        //getting mobile number from the previous activity
        //and sending the verification code to the number
        val intent = intent
        val mobile = intent.getStringExtra("mobile")
        if (mobile != null) {
            sendVerificationCode(mobile)
        }


        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button


        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById<View>(R.id.buttonSignIn).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val code = editTextCode.text.toString().trim { it <= ' ' }
                if (code.isEmpty() || code.length < 6) {
                    editTextCode.error = "Enter valid code"
                    editTextCode.requestFocus()
                    return
                }

                //verifying the code entered manually
                verifyVerificationCode(code)
            }
        })

    }
    private fun showToolbar() {
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Verify OTP"
            toolBar.setNavigationOnClickListener { onBackPressed() }
        }
    }
    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private fun sendVerificationCode(mobile: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91$mobile",
            60,
            TimeUnit.SECONDS,
            this,
            mCallbacks
        )
    }


    //the callback to detect the verification status
    private val mCallbacks: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                //Getting the code sent by SMS
                val code = phoneAuthCredential.smsCode

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    editTextCode!!.setText(code)
                    //verifying the code
                    verifyVerificationCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@VerifyPhoneActivity, e.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)

                //storing the verification id that is sent to the user
                mVerificationId = s
            }
        }


    private fun verifyVerificationCode(code: String) {
        //creating the credential
        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)

        //signing the user
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this@VerifyPhoneActivity
            ) { task ->
                if (task.isSuccessful) {
                    //verification successful we will start the profile activity
                    val intent = Intent(this@VerifyPhoneActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {

                    //verification unsuccessful.. display an error message
                    var message = "Somthing is wrong, we will fix it soon..."
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        message = "Invalid code entered..."
                    }
                    val snackbar = Snackbar.make(
                        findViewById(R.id.container),
                        message, Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction("Dismiss") { }
                    snackbar.show()
                }
            }
    }
}