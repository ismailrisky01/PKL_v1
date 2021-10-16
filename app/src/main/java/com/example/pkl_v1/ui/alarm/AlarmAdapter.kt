package com.example.pkl_v1.ui.alarm

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pkl_v1.databinding.ItemAlarmBinding
import com.example.pkl_v1.model.AlarmModel

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    private var alarmList = emptyList<AlarmModel>()
    private var listener: OnToggleAlarmListener? = null
    private var deleteListener: DeleteListener? = null
    fun setAlarm(alarmModel: List<AlarmModel>, listener: OnToggleAlarmListener,deleteListener: DeleteListener) {
        this.alarmList = alarmModel
        this.listener = listener
        this.deleteListener = deleteListener
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener!!,deleteListener!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataAlarm = alarmList[position]
        holder.bind(dataAlarm)
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    class ViewHolder(val binding: ItemAlarmBinding, listener: OnToggleAlarmListener,deleteListener: DeleteListener) :
        RecyclerView.ViewHolder(binding.root) {
        private var listener: OnToggleAlarmListener? = null
        private var deleteListener: DeleteListener? = null

        fun bind(alarmModel: AlarmModel) {
            val alarmText = String.format("%02d:%02d", alarmModel.hour, alarmModel.minute)
            binding.IDAlarmText.text = alarmText
            Log.d("PKL_Ismail",alarmModel.started.toString())
            binding.switch1.isChecked = alarmModel.started
            binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
                listener?.onToggle(alarmModel)
            }

            binding.IDItemAlarmBtnDelete.setOnClickListener {
                deleteListener?.delete(alarmModel)
            }
        }

        init {
            this.listener = listener
            this.deleteListener= deleteListener
        }
    }
}