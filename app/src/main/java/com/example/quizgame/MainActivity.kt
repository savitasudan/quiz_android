package com.example.quizgame

import android.content.ContentProviderClient
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.quizgame.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    val auth =FirebaseAuth.getInstance()
    lateinit var googleSigninClient: GoogleSignInClient
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding=ActivityMainBinding.inflate(layoutInflater)
        val view =mainBinding.root
        setContentView(view)
val textofgooglebutton= mainBinding.signinwithgoogle.getChildAt(0)as TextView
        textofgooglebutton.text ="Continue with google "
        textofgooglebutton.setTextColor(Color.BLACK)
        textofgooglebutton.textSize = 18F
//register
        registerActivityForGoogleSignin()
        mainBinding.login.setOnClickListener{
            val useremail=mainBinding.edittextuseremail.text.toString()
            val userpassword=mainBinding.edittextuserpassword.text.toString()
         Toast.makeText(applicationContext,"signin",Toast.LENGTH_LONG).show()
            signinuser(useremail,userpassword)
        }
        mainBinding.signinwithgoogle.setOnClickListener {
            signingoogle()
            Toast.makeText(applicationContext,"signin with google",Toast.LENGTH_LONG).show()
        }
        mainBinding.forgotpassword.setOnClickListener{
         val intent =Intent(this@MainActivity,ForgotPassword::class.java)
            startActivity(intent)
        }
          mainBinding.signup.setOnClickListener{
            val intent =Intent(this@MainActivity,SignupActivity::class.java)
              startActivity(intent)
        }
    }

    private fun signingoogle() {
       val gso=GoogleSignInOptions.Builder(DEFAULT_SIGN_IN).requestIdToken("736544084583-vqm37vgl8jusjptdi3hrqhcu6jji7p14.apps.googleusercontent.com").
       requestEmail().build()
        googleSigninClient = GoogleSignIn.getClient(this,gso)
        signin()
    }

    private fun signin() {
       val signInIntent :Intent =googleSigninClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }


    private fun registerActivityForGoogleSignin(){
        activityResultLauncher =registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                result ->
                val resultCode =result.resultCode
                val data =result.data
                if(resultCode== RESULT_OK  && data!=null){
                    val task :Task<GoogleSignInAccount> =GoogleSignIn.getSignedInAccountFromIntent(data)
                     firebaseSigninwithgoogle(task)

                }
            })

    }

    private fun firebaseSigninwithgoogle( task :Task<GoogleSignInAccount>) {


        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext, "Welcome to quiz game", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            firebaseGoogleAccount(account)
        }
        catch (e:ApiException){
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }


    private fun firebaseGoogleAccount(account: GoogleSignInAccount) {
        val authCredential =GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(authCredential).addOnCompleteListener{
            task->
            if(task.isSuccessful){

            }
            else{

            }
        }

    }

    fun signinuser(userEmail : String,userPassword :String){
        auth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener{
            task->
            if(task.isSuccessful){
                Toast.makeText(applicationContext,"welcome to quiz game",Toast.LENGTH_SHORT).show()
                val intent =Intent(this@MainActivity,HomeActivity::class.java)
                startActivity(intent)
                finish()

            }
            else{
                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if(user!=null){
            Toast.makeText(applicationContext,"welcome to quiz game",Toast.LENGTH_SHORT).show()
            val intent =Intent(this@MainActivity,HomeActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
}

