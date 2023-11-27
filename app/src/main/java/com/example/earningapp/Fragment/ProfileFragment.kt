package com.example.earningapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.earningapp.Model.User
import com.example.earningapp.R
import com.example.earningapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {
    val binding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }
    private var isExpand =true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding.imageButton.setOnClickListener {
            if (isExpand){
                binding.expandableConstraint.visibility =View.VISIBLE //visible:thay
                binding.imageButton.setImageResource(R.drawable.arrowup)
            }else{
               binding.expandableConstraint.visibility =View.GONE //bien mat
                binding.imageButton.setImageResource(R.drawable.downarrow)
            }
            isExpand = !isExpand
        }
        Firebase.database.reference.child("user").child(Firebase.auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val userProfile =snapshot.getValue(User::class.java)
                        if (userProfile !=null){
                            binding.profileName.setText(userProfile.name)
                            binding.profileAge.setText(userProfile.age)
                            binding.profileEmail.setText(userProfile.email)
                            binding.profilePassword.setText(userProfile.password)
                            binding.nameUp.setText(userProfile.name)
                            //binding.profileCountry.setText(userProfile.country)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        return binding.root
    }


}