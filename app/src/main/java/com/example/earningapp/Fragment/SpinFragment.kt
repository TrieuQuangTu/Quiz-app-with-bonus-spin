package com.example.earningapp.Fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.earningapp.Model.User
import com.example.earningapp.Model.historyModelClass
import com.example.earningapp.Withdrawal
import com.example.earningapp.databinding.FragmentSpinBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Random


class SpinFragment : Fragment() {
    private lateinit var  binding:FragmentSpinBinding

    private lateinit var timer:CountDownTimer
    private val itemTitles = arrayOf("100","Try Again","500","Try Again","200","Try Again")
    var currentChance =0L
    var currentCoin =0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpinBinding.inflate(inflater,container,false)


        //display bottom Dialog when click
        binding.imgCoin.setOnClickListener {
            val bottomSheetDialogFragment = Withdrawal()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager,"TEST")
            bottomSheetDialogFragment.enterTransition
        }

        //get data tu Firebase Realtime va set len TextView
        Firebase.database.reference.child("user")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var user =snapshot.getValue<User>()
                    binding.name.text =user?.name
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        currentChance =snapshot.getValue() as Long
                        binding.spinChance.text= currentChance.toString()
                    }else{
                        var temp =0
                        binding.spinChance.text= temp.toString()
                        binding.Spin.isEnabled =false
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        //get du lieu playerCoin hien thi len textview Coin
        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   if (snapshot.exists()){
                       currentCoin =snapshot.getValue() as Long
                       binding.coin.text =currentCoin.toString()
                   }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        return binding.root
    }
    private fun showResult(itemTitles:String,spin:Int){
        if (spin%2 ==0){
            var wincoin =itemTitles.toInt()
            //set value len nhanh playerCoin
            Firebase.database.reference.child("playerCoin")
                .child(Firebase.auth.currentUser!!.uid).setValue(wincoin+currentCoin)

            //set value len nhanh playerCoinHistory
            var history =historyModelClass(System.currentTimeMillis().toString(),wincoin.toString(),false)
            Firebase.database.reference.child("playerCoinHistory")
                .push()
                .child(Firebase.auth.currentUser!!.uid).setValue(history)



            binding.coin.text = (wincoin+currentCoin).toString()

        }
        Toast.makeText(requireContext(),itemTitles,Toast.LENGTH_SHORT).show()
        //khi quay vong quay , so lan quay se giam di 1
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
            .setValue(currentChance-1)
        binding.Spin.isEnabled=true //Enable the button again
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Khi nút spin được nhấp vào, nó bị vô hiệu hóa
        binding.Spin.setOnClickListener {
            binding.Spin.isEnabled=false //disable the button while the wheels spinning
            if (currentChance>0){
                val spin = Random().nextInt(6) //Generate a radom value between 0 and 5
                val degrees =60f * spin //Bạn tính toán độ quay cho bánh xe dựa vào giá trị ngẫu nhiên.

                timer =object :CountDownTimer(5000,50){
                    var rotation =0f //khai bao goc quay
                    override fun onTick(p0: Long) {
                        rotation+= 5f // goc quay cua wheel tang them 5 do
                        if (rotation >= degrees){
                            rotation = degrees
                            timer.cancel()
                            showResult(itemTitles[spin],spin)
                        }
                        binding.wheel.rotation =rotation
                    }

                    override fun onFinish() {}
                }.start()
            }else{
                Toast.makeText(requireContext(),"Out of Spin Chance.Please let chance in pratice question",Toast.LENGTH_SHORT).show()

            }
        }
    }

}
