package com.example.flashfeed

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.flashfeed.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        setupAnimations()

        binding.oldUser.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        binding.SignupButton.setOnClickListener {
            val email = binding.TextInputEditTextEmail.text.toString()
            val password = binding.TextInputEditTextPassword.text.toString()
            val confarmpassword = binding.TextInputEditTextConfirmPassword.text.toString()

            if (email.isBlank() || password.isBlank() || confarmpassword.isBlank())

                Toast.makeText(this, "MissingFields", Toast.LENGTH_SHORT).show()
            else if (password.length < 6)

                Toast.makeText(this, "Short Password", Toast.LENGTH_SHORT).show()
            else if (password != confarmpassword)

                Toast.makeText(this, "Password don't Match", Toast.LENGTH_SHORT).show()
            else {
                binding.ProgressBar.isVisible = true
                createAccount(email, password)
            }

        }

    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    verifyEmail()
                } else {
                    binding.ProgressBar.isVisible = false
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun verifyEmail() {
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Check Your Email", Toast.LENGTH_SHORT).show()
                    binding.ProgressBar.isVisible = false
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }
            }

    }

    private fun setupAnimations() {
        val animSlideInRight =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        val animSlideInLeft =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        val animSlideInTop =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_top)
        val animSlideInBottom =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom)
        val animButton =
            AnimationUtils.loadAnimation(this, R.anim.button_click)

        binding.logo.startAnimation(animSlideInTop)
        binding.createAccount.startAnimation(animSlideInTop)
        binding.TextInputLayoutEmail.startAnimation(animSlideInRight)
        binding.TextInputLayoutPassword.startAnimation(animSlideInRight)
        binding.TextInputLayoutConfirmPassword.startAnimation(animSlideInLeft)
        binding.SignupButton.startAnimation(animButton)
        binding.oldUser.startAnimation(animSlideInBottom)
    }
}