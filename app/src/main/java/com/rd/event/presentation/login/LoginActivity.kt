package com.rd.event.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rd.event.R
import com.rd.event.presentation.event_list.EventListActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.btn_login)
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val userName = sharedPref.getString(getString(R.string.user_name), "")
        val userEmail = sharedPref.getString(getString(R.string.user_email), "")
        etName.setText(userName)
        etEmail.setText(userEmail)

        btnLogin.setOnClickListener {
            if (validateFields()) {
                with(sharedPref.edit()) {
                    putString(getString(R.string.user_name), etName.text.toString())
                    putString(getString(R.string.user_email), etEmail.text.toString())
                    apply()
                }

                val intent = Intent(this, EventListActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true

        if  (etName.text.isNullOrBlank()) {
            isValid  = false
            etName.error = getString(R.string.login_invalid_name)
        }

        if  (etEmail.text.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(etEmail.text).matches()) {
            isValid  = false
            etEmail.error = getString(R.string.login_invalid_email)
        }

        return isValid
    }
}