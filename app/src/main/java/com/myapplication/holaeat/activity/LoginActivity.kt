package com.myapplication.holaeat.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.myapplication.holaeat.R
import com.myapplication.holaeat.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {


    lateinit var etMobileNumber : EditText
    lateinit var etPassword : EditText
    lateinit var btnLogin : Button

    lateinit var txtForgotPassword : TextView
    lateinit var txtRegisterYourself: TextView


    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etMobileNumber = findViewById(R.id.etMobileNumber)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtRegisterYourself = findViewById(R.id.txtRegisterYourself)



        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        btnLogin.setOnClickListener {


            /*Create the queue for the request*/
            val queue = Volley.newRequestQueue(this@LoginActivity)
            val url = "http://13.235.250.119/v2/login/fetch_result"

            /*Create the JSON parameters to be sent during the login process*/
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", etMobileNumber.text.toString())
            jsonParams.put("password", etPassword.text.toString())


            if (ConnectionManager().checkConnectivity(this@LoginActivity)) {
                /*Finally send the json object request*/
                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.POST, url, jsonParams,
                    Response.Listener {

                        try {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {
                                val response = data.getJSONObject("data")
                                sharedPreferences.edit()
                                    .putString("user_id", response.getString("user_id")).apply()
                                sharedPreferences.edit()
                                    .putString("user_name", response.getString("name")).apply()
                                sharedPreferences.edit()
                                    .putString(
                                        "user_mobile_number",
                                        response.getString("mobile_number")
                                    )
                                    .apply()
                                sharedPreferences.edit()
                                    .putString("user_address", response.getString("address"))
                                    .apply()
                                sharedPreferences.edit()
                                    .putString("user_email", response.getString("email")).apply()

                                //sessionManager.setLogin(true)
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        MainActivity::class.java
                                    )
                                )
                                finish()
                            } else {
                                btnLogin.visibility = View.VISIBLE
                                txtForgotPassword.visibility = View.VISIBLE
                                btnLogin.visibility = View.VISIBLE
                                val errorMessage = data.getString("errorMessage")
                                Toast.makeText(
                                    this@LoginActivity,
                                    errorMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: JSONException) {
                            btnLogin.visibility = View.VISIBLE
                            txtForgotPassword.visibility = View.VISIBLE
                            txtRegisterYourself.visibility = View.VISIBLE
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener {
                        btnLogin.visibility = View.VISIBLE
                        txtForgotPassword.visibility = View.VISIBLE
                        txtRegisterYourself.visibility = View.VISIBLE
                        Log.e("Error::::", "/post request fail! Error: ${it.message}")
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"

                        /*The below used token will not work, kindly use the token provided to you in the training*/
                        headers["token"] = "d8fe3166127045"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)

            } else {
                val dialog = AlertDialog.Builder(this@LoginActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection is not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }

                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this@LoginActivity)
                }
                dialog.create()
                dialog.show()
            }

        }
        txtRegisterYourself.setOnClickListener {
            val intent2 = Intent(
                this@LoginActivity,
                RegisterYourselfActivity::class.java
            )
            startActivity(intent2)
        }

        txtForgotPassword.setOnClickListener {
            val intent3 = Intent(
                this@LoginActivity,
                ForgotPasswordActivity::class.java
            )
            startActivity(intent3)
        }
    }


}