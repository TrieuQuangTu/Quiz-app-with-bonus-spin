package com.example.earningapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.earningapp.Adapter.categoryAdapter
import com.example.earningapp.Model.categoryModelClass
import com.example.earningapp.R
import com.example.earningapp.Withdrawal
import com.example.earningapp.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private  val binding:FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private var categoryList =ArrayList<categoryModelClass>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

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

        //display bottom Dialog when click
        binding.imgCoin.setOnClickListener {
            val bottomSheetDialogFragment =Withdrawal()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager,"TEST")
            bottomSheetDialogFragment.enterTransition
        }



        categoryList.clear()
        categoryList.add(categoryModelClass(R.drawable.scince1,"Scince"))
        categoryList.add(categoryModelClass(R.drawable.english1,"english"))
        categoryList.add(categoryModelClass(R.drawable.geography,"geography"))
        categoryList.add(categoryModelClass(R.drawable.math,"math"))

        binding.categoryRecyclerview.setHasFixedSize(true)
        binding.categoryRecyclerview.layoutManager =GridLayoutManager(requireContext(),2)
        var adapter =categoryAdapter(categoryList,requireActivity())
        binding.categoryRecyclerview.adapter =adapter
        return binding.root
    }

}