package com.myapplication.holaeat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.myapplication.holaeat.R
import com.myapplication.holaeat.util.ConnectionManager
import org.json.JSONObject

class RegisterYourselfActivity : AppCompatActivity() {

    lateinit var etRegisterName: EditText
    lateinit var etRegisterEmail: EditText
    lateinit var etRegisterMobile: EditText
    lateinit var RegisterAddress: EditText
    lateinit var RegisterPassword: EditText
    lateinit var RegisterConfirmPass: EditText
    lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_yourself)


            etRegisterName = findViewById(R.id.etRegisterName)
            etRegisterEmail = findViewById(R.id.etRegisterEmail)
            etRegisterMobile = findViewById(R.id.etRegisterMobile)
            RegisterAddress = findViewById(R.id.RegisterAddress)
            RegisterPassword = findViewById(R.id.RegisterPassword)
            RegisterConfirmPass = findViewById(R.id.RegisterConfirmPass)
            btnRegister = findViewById(R.id.btnRegister)




        btnRegister.setOnClickListener {

            val name=etRegisterName.text.toString()
            val email=etRegisterEmail.text.toString()
            val mobile =etRegisterMobile.text.toString()
            val address= RegisterAddress.text.toString()
            val password=RegisterPassword.text.toString()



            val queue = Volley.newRequestQueue(this@RegisterYourselfActivity)
            val url = "http://13.235.250.119/v2/register/fetch_result"

            val jsonParams = JSONObject()
            jsonParams.put("name", name)
            jsonParams.put("mobile_number", mobile)
            jsonParams.put("password", password)
            jsonParams.put("address", address)
            jsonParams.put("email", email)


            if (ConnectionManager().checkConnectivity(this@RegisterYourselfActivity)) {
                val jsonRequest = object :
                    JsonObjectRequest(Request.Method.POST, url, jsonParams , Response.Listener {

                        try {
                            val data = it.getJSONObject("data")

                            val success = data.getBoolean("success")
                            if (success) {

                                val intent = Intent(this@RegisterYourselfActivity, LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@RegisterYourselfActivity,
                                    "Some error occurred! $it",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } catch (e: Exception) {
                            Toast.makeText(
                                this@RegisterYourselfActivity,
                                "Some unexpected error occurred!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }, Response.ErrorListener {

                        Toast.makeText(
                            this@RegisterYourselfActivity,
                            "Volley Error $it",
                            Toast.LENGTH_SHORT
                        ).show()

                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "d8fe3166127045"
                        return headers
                    }
                }
                queue.add(jsonRequest)
            } else {
                val dialog = AlertDialog.Builder(this@RegisterYourselfActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection is not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }

                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this@RegisterYourselfActivity)
                }
                dialog.create()
                dialog.show()
            }
        }
        }

    }
