package com.example.quizgame

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.example.quizgame.databinding.ActivityQuizBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuizActivity : AppCompatActivity() {
    lateinit var quizBinding: ActivityQuizBinding
    val database =FirebaseDatabase.getInstance()
    //retrieve data from database reference object
    val databaseReference =database.reference.child("questions")
    var question=""
    var answerA=""
    var answerB=""
    var answerC=""
    var correctAnswer=""
    var questionCount=0
    var questionNumber= 1
    var useranswer =" "
    var userWrong= 0
    var userCorrect= 0
    lateinit var timer :CountDownTimer
    private val totalTime= 25000L
    var timerCounter = false
    var lefttime=totalTime
    val auth =FirebaseAuth.getInstance()
    val user =auth.currentUser
    var scoreRef= database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        quizBinding=ActivityQuizBinding.inflate(layoutInflater)
        val view =quizBinding.root

        setContentView(view)
        gameLogic()
        quizBinding.buttonNext.setOnClickListener {

            resetTimer()
            gameLogic()
        }

        quizBinding.buttonfinish.setOnClickListener {
        sendscore()
        }

        quizBinding.textViewA.setOnClickListener {
            pauseTimer()
            useranswer ="a"
            if(correctAnswer == useranswer){
                 quizBinding.textViewA.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.textViewCorrect.text =userCorrect.toString()

            }
            else{
                quizBinding.textViewA.setBackgroundColor(Color.RED)
                userWrong++

                quizBinding.textViewWrong.text =userWrong.toString()
                findAnswers()
            }
            disableClickableofOptions()
        }

        quizBinding.textViewB.setOnClickListener {
            pauseTimer()
            useranswer ="b"
            if(correctAnswer == useranswer){
                quizBinding.textViewB.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.textViewCorrect.text =userCorrect.toString()

            }
            else{
                quizBinding.textViewB.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text =userWrong.toString()

             findAnswers()
            }
            disableClickableofOptions()

        }

        quizBinding.textViewC.setOnClickListener {
            pauseTimer()
            useranswer ="c"
            if(correctAnswer == useranswer){
                quizBinding.textViewC.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.textViewCorrect.text =userCorrect.toString()

            }
            else{
                quizBinding.textViewC.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text =userWrong.toString()

                 findAnswers()
            }
            disableClickableofOptions()
        }


    }


    //retrieve data

    private fun gameLogic(){

        restoreoptions()
        databaseReference.addValueEventListener(object :ValueEventListener{

            //snapshot reaches the question section on database
            override fun onDataChange(snapshot: DataSnapshot) {

                questionCount = snapshot.childrenCount.toInt()

                if(questionNumber<=questionCount){
                    question=snapshot.child(questionNumber.toString()).child("q").value.toString()
                    answerA=snapshot.child(questionNumber.toString()).child("a").value.toString()
                    answerB=snapshot.child(questionNumber.toString()).child("b").value.toString()
                    answerC=snapshot.child(questionNumber.toString()).child("c").value.toString()
                    correctAnswer=snapshot.child(questionNumber.toString()).child("answer").value.toString()
                    quizBinding.textViewQuestion.text =question
                    quizBinding.textViewA.text=answerA
                    quizBinding.textViewB.text=answerB
                    quizBinding.textViewC.text=answerC
                    quizBinding.progressBarQuiz.visibility=View.INVISIBLE
                    quizBinding.linearLayoutInfo.visibility=View.VISIBLE
                    quizBinding.linearLayoutQuestion.visibility=View.VISIBLE
                    quizBinding.linearLayoutbuttons.visibility= View.VISIBLE

                    startTimer()
                }
                else{
                    val Dialogmessage=AlertDialog.Builder(this@QuizActivity)
                    Dialogmessage.setTitle("QuizGame")
                    Dialogmessage.setMessage("Congratulations !! \n you have answered all the question Do you want to see the result?")
                    Dialogmessage.setCancelable(false)
                    Dialogmessage.setPositiveButton("OK") {dialogWindow,position->
                        sendscore()
                    }

                    Dialogmessage.setNegativeButton("Play Again"){dialogWindow,position->
                        val intent =Intent(applicationContext,HomeActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                    Dialogmessage.create().show()
                        // Perform action when "OK" button is clicked

                    Dialogmessage.show()

                    Toast.makeText(applicationContext,"you have answered all the question",Toast.LENGTH_SHORT).show()
                }
                questionNumber++


            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun findAnswers(){
        when (correctAnswer){
            "a"->quizBinding.textViewA.setBackgroundColor(Color.GREEN)
            "b"->quizBinding.textViewB.setBackgroundColor(Color.GREEN)
            "c"->quizBinding.textViewC.setBackgroundColor(Color.GREEN)

        }
    }

    fun disableClickableofOptions(){
        quizBinding.textViewA.isClickable=false
        quizBinding.textViewB.isClickable=false
        quizBinding.textViewC.isClickable=false

    }

    fun restoreoptions(){
        quizBinding.textViewA.setBackgroundColor(Color.WHITE)
        quizBinding.textViewB.setBackgroundColor(Color.WHITE)
        quizBinding.textViewC.setBackgroundColor(Color.WHITE)
        quizBinding.textViewA.isClickable=true
        quizBinding.textViewB.isClickable=true
        quizBinding.textViewC.isClickable=true


    }

   private fun startTimer(){

       timer= object: CountDownTimer(lefttime,1000){
           override fun onTick(millisUntilFinished: Long) {
               lefttime= millisUntilFinished
               updateCountDownText()
           }

           override fun onFinish() {
               disableClickableofOptions()
               resetTimer()
               updateCountDownText()
               quizBinding.textViewQuestion.text="sorry time is up!Continue with next question"
               timerCounter = false
           }

       }.start()
 timerCounter=true
    }

    private fun updateCountDownText() {
      val remainingtime:Int = (lefttime/1000).toInt()
        quizBinding.textviewTime.text=remainingtime.toString()
    }

    fun pauseTimer(){
         timer.cancel()
        timerCounter=false
    }

    fun resetTimer(){
        pauseTimer()
        lefttime= totalTime
        updateCountDownText()

    }


    fun sendscore(){

        user?.let{
            var userID= it.uid
            scoreRef.child("scores").child(userID).child("correct").setValue(userCorrect)
            scoreRef.child("scores").child(userID).child("wrong").setValue(userWrong).addOnSuccessListener {
                Toast.makeText(applicationContext,"Scores sent to database sucessfully",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@QuizActivity,ResultActivity::class.java)
                startActivity(intent)
            }
        }


    }
}