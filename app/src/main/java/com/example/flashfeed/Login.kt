package com.example.flashfeed

import android.content.Intent
import android.view.animation.AnimationUtils
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.flashfeed.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        setupAnimations()

        binding.newUser.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        binding.loginButton.setOnClickListener {

            val email = binding.TextInputEditTextEmail.text.toString()
            val password = binding.TextInputEditTextPassword.text.toString()

            if (email.isBlank() || password.isBlank())
                Toast.makeText(this, "MissingFields", Toast.LENGTH_SHORT).show()
            else {
                binding.ProgressBar.isVisible = true
                signIn(email, password)
            }
        }
        binding.forgetPassword.setOnClickListener {
            val email = binding.TextInputEditTextEmail.text.toString()

            if (email.isBlank()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.ProgressBar.isVisible = true
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    binding.ProgressBar.isVisible = false
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password reset email sent!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }


    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    binding.ProgressBar.isVisible = false
                    if (auth.currentUser!!.isEmailVerified) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Check Your Email!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }

            }
    }

    // one time login
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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
        binding.textView.startAnimation(animSlideInTop)
        binding.TextInputLayoutEmail.startAnimation(animSlideInRight)
        binding.TextInputLayoutPassword.startAnimation(animSlideInRight)
        binding.forgetPassword.startAnimation(animSlideInLeft)
        binding.loginButton.startAnimation(animButton)
        binding.newUser.startAnimation(animSlideInBottom)
    }

    /*
    sign out

     Firebase.auth.signOut()
    startActivity(Intent(this, Login::class.java))
    finish()
    */


}
