package com.rdapps.sharedpref.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.ComponentActivity
import com.rdapps.sharedpref.databinding.ActivityMainBinding
import com.rdapps.sharedpref.pref.Pref

class MainActivity : ComponentActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initListeners()
    }

    private fun initView() = with(binding) {
        if (Pref.IsLoggedIn.get()) {
            btnLogin.visibility = View.GONE
            edtUserName.visibility = View.GONE
            edtAge.visibility = View.GONE
            tvDetails.visibility = View.VISIBLE
            btnLogout.visibility = View.VISIBLE

            val loggedInTime = Pref.LoggedInTime.get(0L)

            val userDetails = "User Name: ${Pref.UserName.get<String>()}\n"
                .plus("User Age: ${Pref.UserAge.get<Int>()}\n")
                .plus("Logged In At: $loggedInTime")

            tvDetails.text = userDetails

        } else {
            btnLogin.visibility = View.VISIBLE
            edtUserName.visibility = View.VISIBLE
            edtAge.visibility = View.VISIBLE
            tvDetails.visibility = View.GONE
            btnLogout.visibility = View.GONE
        }
    }

    private fun initListeners() = with(binding) {
        btnLogout.setOnClickListener {
            Pref.IsLoggedIn.set(false)
            Pref.UserName.set("")
            Pref.UserAge.set(0)
            Pref.LoggedInTime.set(0L)

            initView()
        }

        btnLogin.setOnClickListener {
            val userName = edtUserName.text.toString()
            val age = edtAge.text.toString()

            if (!TextUtils.isEmpty(userName) && TextUtils.isDigitsOnly(age)) {

                Pref.UserName.set(userName)
                Pref.UserAge.set(age.toInt())
                Pref.LoggedInTime.set(System.currentTimeMillis())
                Pref.IsLoggedIn.set(true)

                initView()
            }
        }
    }
}