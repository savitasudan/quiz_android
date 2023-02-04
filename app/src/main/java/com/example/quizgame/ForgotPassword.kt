package com.example.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quizgame.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {
    lateinit var forgotBinding :ActivityForgotPasswordBinding
    val auth =FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotBinding=ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view =forgotBinding.root
        setContentView(view)
        forgotBinding.btnreset.setOnClickListener {
            val userEmail= forgotBinding.btnreset.text.toString()
            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener{
                    task->
                if(task.isSuccessful){
                  Toast.makeText(applicationContext,"we sent a password reset email to your email address",Toast.LENGTH_LONG).show()
                    finish()
                }
                else{
                    Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }

        }
    }
}