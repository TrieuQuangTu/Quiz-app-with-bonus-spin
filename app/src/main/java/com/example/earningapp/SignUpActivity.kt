package com.example.earningapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.earningapp.Model.User
import com.example.earningapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignupBinding

    private lateinit var username:String
    private lateinit var age:String
    private lateinit var email:String
    private lateinit var password:String

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize firebaseAuth
        auth= FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().reference

        binding.btnSignup.setOnClickListener {
            signUp()
        }
        binding.txtAlready.setOnClickListener {
            startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
        }
    }

    private fun signUp() {
        username=binding.edtName.text.toString().trim()
        age=binding.edtAge.text.toString().trim()
        email =binding.edtEmail.text.toString().trim()
        password =binding.edtPassword.text.toString().trim()

        if (username.isEmpty() || age.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please fill information",Toast.LENGTH_SHORT).show()
        }else{
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this,"Sign Up Successfully",Toast.LENGTH_SHORT).show()
                    saveUserData()
                    startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this,"${it.exception}",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveUserData() {

        username=binding.edtName.text.toString().trim()
        age=binding.edtAge.text.toString().trim()
        email =binding.edtEmail.text.toString().trim()
        password =binding.edtPassword.text.toString().trim()

        var userId =auth.currentUser!!.uid
        var user =User(username,age,email,password)
        databaseReference.child("user").child(userId).setValue(user).addOnCompleteListener{
            Toast.makeText(this,"Data storage successfully",Toast.LENGTH_SHORT).show()
        }



    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser !=null){
            startActivity(Intent(this@SignUpActivity,HomeActivity::class.java))
        }
    }
}