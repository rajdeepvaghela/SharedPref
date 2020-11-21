package com.rdapps.sharedpref.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rdapps.sharedpref.R
import com.rdapps.sharedpref.pref.Pref
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        if (Pref.IS_LOGGED_IN.getValue()) {
            btnLogin.visibility = View.GONE
            edtUserName.visibility = View.GONE
            edtAge.visibility = View.GONE
            tvDetails.visibility = View.VISIBLE
            btnLogout.visibility = View.VISIBLE

            val loggedInTime: Long = Pref.LOGGED_IN_TIME.getValue()

            val userDetails = "User Name: ${Pref.USER_NAME.getValue<String>()}\n"
                    .plus("User Age: ${Pref.USER_AGE.getValue<Int>()}\n")
                    .plus("Logged In At: $loggedInTime")

            tvDetails.text = userDetails

            btnLogout.setOnClickListener {
                Pref.IS_LOGGED_IN.setValue(false)
                Pref.USER_NAME.setValue("")
                Pref.USER_AGE.setValue(0)
                Pref.LOGGED_IN_TIME.setValue(0L)

                initView()
            }

        } else {
            btnLogin.visibility = View.VISIBLE
            edtUserName.visibility = View.VISIBLE
            edtAge.visibility = View.VISIBLE
            tvDetails.visibility = View.GONE
            btnLogout.visibility = View.GONE

            btnLogin.setOnClickListener {
                val userName = edtUserName.text.toString()
                val age = edtAge.text.toString()

                if (!TextUtils.isEmpty(userName) && TextUtils.isDigitsOnly(age)) {

                    Pref.USER_NAME.setValue(userName)
                    Pref.USER_AGE.setValue(age.toInt())
                    Pref.LOGGED_IN_TIME.setValue(System.currentTimeMillis())
                    Pref.IS_LOGGED_IN.setValue(true)

                    initView()
                }
            }
        }
    }
}