package com.example.taller3

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.taller3.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.LoginButton.setOnClickListener {
            signInUser(binding.emailText.text.toString(), binding.passwordText.text.toString())
        }
    }

    private fun signInUser(email: String, password: String) {
        if (validateForm(email, password)) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener() {
                if (it.isSuccessful) {
                    updateUI(auth.currentUser)
                } else {
                    val message = it.exception!!.message
                    Toast.makeText(this, message, Toast.LENGTH_LONG ).show()
                    Log.w(ContentValues.TAG, "signInWithEmailAndPassword:failure", it.exception)
                    binding.emailText.text.clear()
                    binding.passwordText.text.clear()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI(auth.currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("email", auth.currentUser?.email.toString())
            startActivity(intent)
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        var valid = false
        if (email.isEmpty()) {
            binding.emailText.error = "Required!"
        } else if (!validEmailAddress(email)) {
            binding.emailText.error = "Invalid email address"
        } else if (password.isEmpty()) {
            binding.passwordText.error = "Required!"
        } else if (password.length < 6){
            binding.passwordText.error = "Password should be at least 6 characters long!"
        }else {
            valid = true
        }
        return valid
    }

    private fun validEmailAddress(email:String):Boolean{
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return email.matches(regex.toRegex())
    }
}