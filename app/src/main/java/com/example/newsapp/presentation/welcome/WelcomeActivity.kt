package com.example.newsapp.presentation.welcome

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.databinding.ActivityWelcomeBinding
import com.example.newsapp.presentation.auth.AuthActivity
import com.example.newsapp.presentation.main.home.HomeActivity
import com.example.newsapp.utils.viewmodelfactory.ViewModelFactory

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var viewModel: WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()

        Handler(Looper.getMainLooper()).postDelayed({
            getSession()
        }, 2000)
    }

    private fun getSession() {
        viewModel.loginData.observe(this) { loginData ->
            if (loginData != null) {
                if (loginData["isLogin"] as Boolean? == true)
                    startActivity(Intent(this, HomeActivity::class.java))
                else
                    startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }
        viewModel.getLogin()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[WelcomeViewModel::class.java]
    }
}