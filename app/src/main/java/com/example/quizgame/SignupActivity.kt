package com.example.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.View
import android.widget.Toast
import com.example.quizgame.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    lateinit var signupBinding: ActivitySignupBinding
  val auth :FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding =ActivitySignupBinding.inflate(layoutInflater)
        val view =signupBinding.root
        setContentView(view)
        signupBinding.signupbtn.setOnClickListener {
            val email =signupBinding.edittextemail.text.toString()
            val password=signupBinding.edittextpassword.text.toString()
            signupwithFirebase(email,password)
        }

    }
    fun signupwithFirebase(email: String,password:String){
        signupBinding.progressBar.visibility =View.VISIBLE
        signupBinding.signupbtn.isClickable =false
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            task->
            if(task.isSuccessful){
               Toast.makeText(applicationContext,"your account has been created",Toast.LENGTH_LONG).show()
                finish()
                signupBinding.progressBar.visibility =View.INVISIBLE
                signupBinding.signupbtn.isClickable =true
            }
            else{
                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_LONG).show()

            }
        }


    }
}