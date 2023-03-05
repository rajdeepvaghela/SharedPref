package com.rdapps.sharedpref.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rdapps.sharedpref.databinding.ActivityMainBinding
import com.rdapps.sharedpref.pref.Store
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DataStoreMainActivity : ComponentActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initListeners()
        initCollectors()
    }

    private fun initListeners() = with(binding) {
        btnLogout.setOnClickListener {
            lifecycleScope.launch {
                Store.IsLoggedIn.set(false)
                Store.UserName.set("")
                Store.UserAge.set(0)
                Store.LoggedInTime.set(0L)
            }
        }

        btnLogin.setOnClickListener {
            val userName = edtUserName.text.toString()
            val age = edtAge.text.toString()
            edtUserName.text = null
            edtAge.text = null

            if (userName.isNotEmpty() && age.isDigitsOnly()) {

                lifecycleScope.launch {
                    Store.IsLoggedIn.set(true)
                    Store.UserName.set(userName)
                    Store.UserAge.set(age.toInt())
                    Store.LoggedInTime.set(System.currentTimeMillis())
                }
            }
        }
    }

    private fun initCollectors() = lifecycleScope.launch {
        launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                Store.IsLoggedIn.getFlow<Boolean>().collect {
                    binding.groupLoggedIn.isVisible = it
                    binding.groupLoggedOut.isVisible = !it
                }
            }
        }

        launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                combine(
                    Store.UserName.getFlow<String>(),
                    Store.UserAge.getFlow<Int>(),
                    Store.LoggedInTime.getFlow<Long>()
                ) { userName, age, loggedInTime ->
                    "User Name: $userName\n"
                        .plus("User Age: $age\n")
                        .plus("Logged In At: $loggedInTime")
                }.collect {
                    binding.tvDetails.text = it
                }
            }
        }
    }
}