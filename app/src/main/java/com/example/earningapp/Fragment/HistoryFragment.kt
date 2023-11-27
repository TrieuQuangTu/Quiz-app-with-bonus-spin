package com.example.earningapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.earningapp.Adapter.historyAdapter
import com.example.earningapp.Model.historyModelClass
import com.example.earningapp.R
import com.example.earningapp.Withdrawal
import com.example.earningapp.databinding.FragmentHistoryBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Collections


class HistoryFragment : Fragment() {
    private val binding:FragmentHistoryBinding by lazy {
        FragmentHistoryBinding.inflate(layoutInflater)
    }
    private var ListHistory=ArrayList<historyModelClass>()
    private lateinit var adapter:historyAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        //display bottom Dialog when click
        binding.imgCoin.setOnClickListener {
            val bottomSheetDialogFragment = Withdrawal()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager,"TEST")
            bottomSheetDialogFragment.enterTransition
        }




        //get du lieu playerCoin hien thi len textview Coin
        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
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

        adapter =historyAdapter(ListHistory)

        //get du lieu playerCoinHistory hien thi len Recyclerview
        Firebase.database.reference.child("playerCoinHistory").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object:ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    ListHistory.clear()
                    var ListHistory1 =ArrayList<historyModelClass>()
                    for (datasnapshot in snapshot.children){
                        var data =datasnapshot.getValue(historyModelClass::class.java)
                        ListHistory1.add(data!!)
                    }
                    //lon nguoc lai
                    Collections.reverse(ListHistory1)
                    ListHistory.addAll(ListHistory1)
                    adapter.notifyDataSetChanged()
                    setAdapter()
                }

                private fun setAdapter() {
                    binding.HistoryRecyclerview.layoutManager =LinearLayoutManager(requireContext())
                    binding.HistoryRecyclerview.adapter =adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        return binding.root
    }


}