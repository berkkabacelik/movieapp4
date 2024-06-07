package com.example.movieapp4.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.movieapp4.MainActivity
import com.example.movieapp4.R
import com.example.movieapp4.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString().trim()
            val pass = binding.loginPassword.text.toString().trim()
            if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (pass.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(requireActivity()) { authResult ->
                            val user = authResult.user
                            if (user != null) {
                                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_loginFragment_to_mainFragment2)
                            } else {
                                Toast.makeText(requireContext(), "User not registered", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener(requireActivity()) { e ->
                            when (e) {
                                is FirebaseAuthInvalidUserException -> {
                                    Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
                                }
                                is FirebaseAuthInvalidCredentialsException -> {
                                    Toast.makeText(requireContext(), "Check your password", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                } else {
                    binding.loginPassword.error = "Password cannot be empty"
                }
            } else if (email.isEmpty()) {
                binding.loginEmail.error = "Email cannot be empty"
            } else {
                binding.loginEmail.error = "Please enter valid email"
            }
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        binding.signupRedirectText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment2)
        }
    }
}
