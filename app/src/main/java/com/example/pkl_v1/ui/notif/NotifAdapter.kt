package com.example.pkl_v1.ui.notif

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pkl_v1.databinding.MessageItemBinding
import com.example.pkl_v1.model.ModelChat

class NotifAdapter : RecyclerView.Adapter<NotifAdapter.ViewHolder>() {
    var listData = mutableListOf<ModelChat>()
    fun setMessage(data: MutableList<ModelChat>) {
        listData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MessageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = listData[position]
        if (currentData.from=="admin"){
            holder.initData(currentData)
        }
    }

    override fun getItemCount(): Int {
        return listData.size

    }

    class ViewHolder(val binding: MessageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun initData(modelChat: ModelChat){
            if (modelChat.from=="admin"){
                binding.tvMessage.visibility = View.GONE
                binding.tvBotMessage.text = modelChat.message
            }
        }
    }
}