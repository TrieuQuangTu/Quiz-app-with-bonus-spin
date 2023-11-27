package com.example.earningapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.earningapp.Model.historyModelClass
import com.example.earningapp.databinding.FragmentHistoryBinding
import com.example.earningapp.databinding.ItemHistoryBinding
import java.sql.Timestamp
import java.util.Date

class historyAdapter(var ListHistory: ArrayList<historyModelClass>) :
    RecyclerView.Adapter<historyAdapter.historyViewHolder>() {


    inner class historyViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): historyViewHolder {
       return historyViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun getItemCount(): Int {
        return ListHistory.size
    }

    override fun onBindViewHolder(holder: historyViewHolder, position: Int) {
        var timeStamp =Timestamp(ListHistory.get(position).timeAndDate.toLong())
        holder.binding.itemHistoryTIme.text = Date(timeStamp.time).toString()
        holder.binding.itemHistoryCoin.text =ListHistory[position].coin
        holder.binding.status.text =if(ListHistory.get(position).isWithDrawal){"Money WithDrawal"}else{"+Money Added"}

    }
}