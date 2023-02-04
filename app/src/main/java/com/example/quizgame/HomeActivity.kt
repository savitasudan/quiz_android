package com.example.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quizgame.databinding.ActivityHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    lateinit var homeBinding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding=ActivityHomeBinding.inflate(layoutInflater)
        val view =homeBinding.root
        setContentView(view)

        homeBinding.startquiz.setOnClickListener {
            val intent= Intent(this,QuizActivity::class.java)
            startActivity(intent)
        }
        homeBinding.signout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()


            val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
            val googleSignInClient = GoogleSignIn.getClient(this,gso)
            googleSignInClient.signOut().addOnCompleteListener{
                task->
                if(task.isSuccessful){
                    Toast.makeText(applicationContext,"signout is suceesful",Toast.LENGTH_SHORT).show()
                }
            }
            val intent =Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}