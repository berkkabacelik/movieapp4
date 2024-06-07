package com.example.movieapp4.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.movieapp4.databinding.FragmentSignUpBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString().trim()
            val pass = binding.signupPassword.text.toString().trim()
            if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (pass.isNotEmpty()) {
                    auth.createUserWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(requireActivity(), OnSuccessListener<AuthResult> {
                            Toast.makeText(requireContext(), "Sign up Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finish()
                        }).addOnFailureListener(requireActivity(), OnFailureListener { e ->
                            Toast.makeText(requireContext(), "Sign up Failed", Toast.LENGTH_SHORT).show()
                        })
                } else {
                    binding.signupPassword.error = "Password cannot be empty"
                }
            } else if (email.isEmpty()) {
                binding.signupEmail.error = "Email cannot be empty"
            } else {
                binding.signupEmail.error = "Please enter valid email"
            }
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        binding.loginRedirectText.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        Log.d("SignupFragment", "Signup Fragment created")


    }
}
