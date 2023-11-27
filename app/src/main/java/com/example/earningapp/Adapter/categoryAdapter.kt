package com.example.earningapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.earningapp.Model.categoryModelClass
import com.example.earningapp.QuizActivity
import com.example.earningapp.databinding.ItemCategoryBinding

class categoryAdapter(
    var categoryList:ArrayList<categoryModelClass>,
    var requiredActivity:FragmentActivity
):RecyclerView.Adapter<categoryAdapter.MycategoryViewHolder>() {

    inner class MycategoryViewHolder(val binding:ItemCategoryBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MycategoryViewHolder {
        return MycategoryViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MycategoryViewHolder, position: Int) {
        var dataList =categoryList[position]
        holder.binding.categoryImage.setImageResource(dataList.catImage)
        holder.binding.category.text =dataList.catText
        holder.binding.categoryBtn.setOnClickListener {
            val intent =Intent(requiredActivity,QuizActivity::class.java)
            intent.putExtra("categoryimg",dataList.catImage)
            intent.putExtra("questionType",dataList.catText)
            requiredActivity.startActivity(intent)
        }
    }
}