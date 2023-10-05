package com.example.newsapp.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentLoginBinding
import com.example.newsapp.presentation.main.home.HomeActivity
import com.example.newsapp.utils.viewmodelfactory.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    private val dummyUserEmail = "ramatriagung91@gmail.com"
    private val dummyUserPassword = "12345678"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        binding.apply {
            etEmail.text = Editable.Factory.getInstance().newEditable(dummyUserEmail)
            etPassword.text = Editable.Factory.getInstance().newEditable(dummyUserPassword)

            etEmail.doOnTextChanged { text, _, _, _ ->
                checkEmailValidation(text.toString())
            }
            btnLogin.setOnClickListener {
                makeLogin()
            }
            btnRegister.setOnClickListener {
                moveToRegister()
            }

            checkEmailValidation(etEmail.text.toString())
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory.getInstance(requireActivity())
        )[LoginViewModel::class.java]
    }

    private fun moveToRegister() {
        showSnackbar(getString(R.string.move_to_register_message))
    }

    private fun loginDataProcess(email: String, password: String): Boolean {
        return email == dummyUserEmail && password == dummyUserPassword
    }

    private fun makeLogin() {
        showLoading(true)
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        Handler(Looper.getMainLooper()).postDelayed({
            if (loginDataProcess(email, password)) {
                showLoading(false)
                showSnackbar(getString(R.string.login_successfully))
                viewModel.saveLogin(email)
                moveToHome(email)
            } else {
                showLoading(false)
                showSnackbar(getString(R.string.login_failed))
            }
        }, 3000)
    }

    private fun moveToHome(email: String) {
        val homeIntent = Intent(activity, HomeActivity::class.java)
        homeIntent.putExtra(HomeActivity.EXTRA_EMAIL, email)
        homeIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        activity?.startActivity(homeIntent)
        activity?.finish()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingLayout.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun checkEmailValidation(text: String) {
        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
        binding.etEmail.error =
            if (!isEmailValid) getString(R.string.incorrect_email_format_message) else null
        binding.btnLogin.isEnabled = isEmailValid
    }
}