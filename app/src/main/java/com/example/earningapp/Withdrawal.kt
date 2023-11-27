package com.example.earningapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.earningapp.Model.historyModelClass
import com.example.earningapp.databinding.FragmentWithdrawalBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Withdrawal : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentWithdrawalBinding
    var currentCoin =0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWithdrawalBinding.inflate(inflater,container,false)


        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        currentCoin =snapshot.getValue() as Long

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        binding.TransferAmount.setOnClickListener {
            if (binding.edtAmout.text.toString().toDouble() <= currentCoin){
                Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid)
                    .setValue(currentCoin-binding.edtAmout.text.toString().toDouble())

                var history =
                    historyModelClass(System.currentTimeMillis().toString(),binding.edtAmout.text.toString(),false)
                Firebase.database.reference.child("playerCoinHistory")
                    .push()
                    .child(Firebase.auth.currentUser!!.uid).setValue(history)

            }else{
                Toast.makeText(activity,"Out of Money",Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }


}