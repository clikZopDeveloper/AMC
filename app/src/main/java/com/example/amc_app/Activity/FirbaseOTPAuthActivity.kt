package com.example.amc_app.Activity

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.amc_app.R
import com.example.amc_app.databinding.ActivityFirebaseAuthBinding
import com.google.android.gms.auth.api.phone.SmsRetriever

import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ResolvableApiException
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*

import java.util.concurrent.TimeUnit

class FirbaseOTPAuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirebaseAuthBinding

    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var activity: Activity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_firebase_auth)
        // window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    //    auth = Firebase.auth
       // auth=FirebaseAuth.getInstance();

        startSmsRetriever()
      //  callFirebaseOTP()

        // [END phone_auth_callbacks]
    }

    fun  callFirebaseOTP(){
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)

                binding.btn.setOnClickListener {
                    startPhoneNumberVerification(binding.editMobNo.text.toString())
                }

                binding.btnVerify.setOnClickListener {
                    verifyPhoneNumberWithCode(storedVerificationId, binding.editOTP.text.toString())
                }

                binding.btnResendVerify.setOnClickListener {
                    resendVerificationCode(binding.editMobNo.text.toString(), resendToken)
                }

            }


            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token

                /*  startActivity(Intent(this@FirbaseOTPAuthActivity,DashboardActivity::class.java)
                      .putExtra("storedVerificationId",storedVerificationId)*/

                Log.d("vxcv", storedVerificationId + "")

            }
        }
    }

    // [START on_start_check_user]
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
    //    val currentUser = auth.currentUser
   //     updateUI(currentUser)
    }

    // [END on_start_check_user]

    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91" + phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)
    }

    // [START resend_verification]
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?,
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // (optional) Activity for callback binding
            // If no activity is passed, reCAPTCHA verification can not be used.
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user

                    val intent = Intent(this@FirbaseOTPAuthActivity, DashboardActivity::class.java)
                    startActivity(intent)
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
    // [END sign_in_with_phone]

    private fun updateUI(user: FirebaseUser? = auth.currentUser) {

    }

    companion object {
        private const val TAG = "PhoneAuthActivity"
    }

    private fun startSmsRetriever() {
        val client = SmsRetriever.getClient(this)

        val task = client.startSmsRetriever()

        task.addOnSuccessListener {
            // SMS Retriever has been started successfully
        }

        task.addOnFailureListener { e ->
            // Failed to start SMS Retriever
            if (e is ApiException) {
                val status = e.statusCode
                if (status == CommonStatusCodes.RESOLUTION_REQUIRED) {
                    // Show the user a dialog to resolve the issue
                    try {
                        val resolvable = e as ResolvableApiException
                        resolvable.startResolutionForResult(this, 123)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Handle the exception
                    }
                }
            }
        }
    }

}
