package com.example.newsapp.presentation.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.newsapp.databinding.ActivityMainBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginFragment = LoginFragment()
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(binding.fragmentContainer.id, loginFragment).commit()
    }
}