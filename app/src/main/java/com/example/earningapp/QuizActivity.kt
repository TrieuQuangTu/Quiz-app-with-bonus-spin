package com.example.earningapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.earningapp.Model.Question
import com.example.earningapp.databinding.ActivityQuizBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QuizActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityQuizBinding.inflate(layoutInflater)
    }
    var currentQuestion = 0
    var score = 0
    var currentChance = 0L
    var userId = Firebase.auth.currentUser!!.uid


    private lateinit var questionList: ArrayList<Question>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //get du lieu playerCoin hien thi len textview Coin
        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        var currentCoin =snapshot.getValue() as Long
                        binding.coin.text =currentCoin.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        currentChance = snapshot.value as Long
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        //display bottom Dialog when click
        binding.imgCoin.setOnClickListener {
            val bottomSheetDialogFragment = Withdrawal()
            bottomSheetDialogFragment.show(this@QuizActivity.supportFragmentManager, "TEST")
            bottomSheetDialogFragment.enterTransition
        }

        questionList = ArrayList<Question>()
        // nhan du lieu : image tu HomeFragment
        var image = intent.getIntExtra("categoryimg", 0)
        var catText = intent.getStringExtra("questionType")

        //get data from Firebase firestore
        Firebase.firestore.collection("Questions")
            .document(catText.toString())
            .collection("question1").get().addOnSuccessListener { questionData ->
                questionList.clear()
                for (data in questionData.documents) {
                    var question: Question? = data.toObject(Question::class.java)
                    questionList.add(question!!)
                }
                Log.d("MyTag", "onCreate: ${questionList}")
                if (questionList.size > 0) {
                    binding.txtQuestion.setText(questionList.get(currentQuestion).question)
                    binding.option1.text = questionList.get(currentQuestion).option1
                    binding.option2.text = questionList.get(currentQuestion).option2
                    binding.option3.text = questionList.get(currentQuestion).option3
                    binding.option4.text = questionList.get(currentQuestion).option4
                }
            }
        binding.categoryImg.setImageResource(image)

        //
        binding.option1.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option1.text.toString())
        }
        binding.option2.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option2.text.toString())
        }
        binding.option3.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option3.text.toString())
        }
        binding.option4.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option4.text.toString())
        }

    }

    private fun nextQuestionAndScoreUpdate(s: String) {
        //so sanh : chua hieu
        if (s.equals(questionList.get(currentQuestion).ans)) {
            score += 10
        }
        currentQuestion++


        if (currentQuestion >= questionList.size) {
            if (score >= (score / (questionList.size * 10)) * 100) {
                binding.Winner.visibility = View.VISIBLE
                Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
                    .setValue(currentChance+1)
                var isUpdated = false
                if (isUpdated) {

                } else {

                }

            } else {
                binding.Sorry.visibility = View.VISIBLE
            }
        } else {
            binding.txtQuestion.setText(questionList.get(currentQuestion).question)
            binding.option1.text = questionList.get(currentQuestion).option1
            binding.option2.text = questionList.get(currentQuestion).option2
            binding.option3.text = questionList.get(currentQuestion).option3
            binding.option4.text = questionList.get(currentQuestion).option4
        }
    }
}