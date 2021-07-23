package com.example.pkl_v1.ui.alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pkl_v1.databinding.ItemAlarmBinding
import com.example.pkl_v1.model.AlarmModel

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    private var alarmList = emptyList<AlarmModel>()
    private var listener: OnToggleAlarmListener? = null
    fun setAlarm(alarmModel: List<AlarmModel>, listener: OnToggleAlarmListener) {
        this.alarmList = alarmModel
        this.listener = listener
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, listener!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataAlarm = alarmList[position]
        holder.bind(dataAlarm)


    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    class ViewHolder(val binding: ItemAlarmBinding, listener: OnToggleAlarmListener) :
        RecyclerView.ViewHolder(binding.root) {
        private var listener: OnToggleAlarmListener? = null

        fun bind(alarmModel: AlarmModel) {
            val alarmText = String.format("%02d:%02d", alarmModel.hour, alarmModel.minute)
            binding.IDAlarmText.text = alarmText
            binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
                listener?.onToggle(alarmModel)
            }
        }

        init {
            this.listener = listener
        }
    }

}