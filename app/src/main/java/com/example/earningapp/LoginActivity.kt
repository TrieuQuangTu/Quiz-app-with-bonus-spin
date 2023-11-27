package com.example.earningapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.earningapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()
        binding.btnLogin.setOnClickListener {
            Login()
        }

    }

    private fun Login() {
        if (binding.edtEmail.text.isEmpty() || binding.edtPassword.text.isEmpty()){
            Toast.makeText(this,"Please fill information",Toast.LENGTH_SHORT).show()
        }else {
            val email =binding.edtEmail.text.toString().trim()
            val password =binding.edtPassword.text.toString().trim()

            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                Toast.makeText(this,"Sign in Successfully",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                finish()
            }.addOnFailureListener{
                Toast.makeText(this,"Sign In Failed : ${it.message}",Toast.LENGTH_SHORT).show()
            }
        }
    }
}