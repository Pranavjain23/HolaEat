package com.myapplication.holaeat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.myapplication.holaeat.R
import com.myapplication.holaeat.util.ConnectionManager
import com.myapplication.holaeat.util.Validations
import org.json.JSONObject

class OtpActivity : AppCompatActivity() {

    private lateinit var etOTP: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmNewPassword: EditText
    private lateinit var btnSubmitOTP: Button
    private lateinit var rlOTP: RelativeLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var mobileNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        etOTP = findViewById(R.id.etOTP)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword)
        btnSubmitOTP = findViewById(R.id.btnSubmitOTP)
        rlOTP = findViewById(R.id.rlOTP)
        progressBar = findViewById(R.id.progressBar)

        rlOTP.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        if (intent != null) {
            mobileNumber = intent.getStringExtra("user_mobile") as String
        }

        btnSubmitOTP.setOnClickListener {
            rlOTP.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            if (ConnectionManager().checkConnectivity(this@OtpActivity)) {
                if (etOTP.text.length == 4) {
                    if (Validations.validatePasswordLength(etNewPassword.text.toString())) {
                        if (Validations.matchPassword(
                                etNewPassword.text.toString(),
                                etConfirmNewPassword.text.toString()
                            )
                        ) {
                            resetPassword(
                                mobileNumber,
                                etOTP.text.toString(),
                                etNewPassword.text.toString()
                            )
                        } else {
                            rlOTP.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@OtpActivity,
                                "Passwords do not match",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } else {
                        rlOTP.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@OtpActivity,
                            "Invalid Password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    rlOTP.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@OtpActivity, "Incorrect OTP", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                rlOTP.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this@OtpActivity,
                    "No Internet Connection!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun resetPassword(mobileNumber: String, otp: String, password: String) {
        val queue = Volley.newRequestQueue(this)

        val RESET_PASSWORD = "http://13.235.250.119/v2/reset_password/fetch_result"

        val jsonParams = JSONObject()
        jsonParams.put("mobile_number", mobileNumber)
        jsonParams.put("password", password)
        jsonParams.put("otp", otp)

        val jsonObjectRequest =
            object : JsonObjectRequest(Method.POST, RESET_PASSWORD, jsonParams, Response.Listener {
                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        progressBar.visibility = View.INVISIBLE
                        val builder = AlertDialog.Builder(this@OtpActivity)
                        builder.setTitle("Confirmation")
                        builder.setMessage("Your password has been successfully changed")
                        builder.setIcon(R.drawable.ic_action_success)
                        builder.setCancelable(false)
                        builder.setPositiveButton("Ok") { _, _ ->
                            startActivity(
                                Intent(
                                    this@OtpActivity,
                                    LoginActivity::class.java
                                )
                            )
                            ActivityCompat.finishAffinity(this@OtpActivity)
                        }
                        builder.create().show()
                    } else {
                        rlOTP.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        val error = data.getString("errorMessage")
                        Toast.makeText(
                            this@OtpActivity,
                            error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    rlOTP.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@OtpActivity,
                        "Incorrect Response!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, Response.ErrorListener {
                rlOTP.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                VolleyLog.e("Error::::", "/post request fail! Error: ${it.message}")
                Toast.makeText(this@OtpActivity, it.message, Toast.LENGTH_SHORT).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"


                    headers["token"] = "d8fe3166127045"
                    return headers
                }
            }
        queue.add(jsonObjectRequest)
    }
}