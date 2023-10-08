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
import com.example.newsapp.di.AppModule
import com.example.newsapp.di.DaggerAppComponent
import com.example.newsapp.presentation.main.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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
                binding.btnLogin.isEnabled = isLoginButtonEnabled(text.toString(), null)
            }
            etPassword.doOnTextChanged { text, _, _, _ ->
                binding.btnLogin.isEnabled = isLoginButtonEnabled(null, text.toString())
            }
            btnLogin.setOnClickListener {
                makeLogin()
            }
            btnRegister.setOnClickListener {
                moveToRegister()
            }

            binding.btnLogin.isEnabled =
                isLoginButtonEnabled(null, null)
        }
    }

    private fun isLoginButtonEnabled(email: String?, password: String?): Boolean {
        val emailText = email ?: binding.etEmail.text.toString()
        val passwordText = password ?: binding.etPassword.text.toString()
        val isEmailValid = checkEmailValidation(emailText)
        val isPasswordValid = checkPasswordValidation(passwordText)
        return isPasswordValid && isEmailValid
    }

    private fun initViewModel() {
        DaggerAppComponent.builder()
            .appModule(AppModule(requireContext()))
            .build().inject(this@LoginFragment)

        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
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

    private fun checkEmailValidation(text: String): Boolean {
        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
        binding.layoutEmail.isErrorEnabled = !isEmailValid
        binding.layoutEmail.error =
            if (!isEmailValid) getString(R.string.incorrect_email_format_message) else null

        return isEmailValid
    }

    private fun checkPasswordValidation(text: String): Boolean {
        val isPasswordValid = text.length >= 8
        binding.layoutPassword.isErrorEnabled = !isPasswordValid
        binding.layoutPassword.error =
            if (!isPasswordValid) getString(R.string.password_contain_8char) else null

        return isPasswordValid
    }
}