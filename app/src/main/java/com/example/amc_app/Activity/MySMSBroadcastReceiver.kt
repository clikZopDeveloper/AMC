package com.example.amc_app.Activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class MySMSBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    // Get the SMS message
                    val message = extras?.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    // Extract the OTP from the message using regex or any other method
                    val otp = extractOtp(message)
                    Log.d("werwe", "Received OTP: $otp")
                }
                CommonStatusCodes.TIMEOUT -> {
                    // Handle timeout
                }
            }
        }
    }

    private fun extractOtp(message: String): String {
        // Implement your logic to extract OTP from the SMS message
        // You might use regex or any other method based on the OTP format
        // Example using regex for a 6-digit OTP: (\d{6})
        return ""
    }

    companion object {
        private const val TAG = "MySMSBroadcastReceiver"
    }
}