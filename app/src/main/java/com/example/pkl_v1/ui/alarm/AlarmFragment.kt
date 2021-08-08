package com.example.pkl_v1.ui.alarm

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pkl_v1.databinding.FragmentAlarmBinding
import com.example.pkl_v1.model.AlarmModel
import com.example.pkl_v1.viewmodel.AlarmViewModel
import java.util.*


class AlarmFragment : Fragment(),OnToggleAlarmListener,DeleteListener {
    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAlarmViewModel: AlarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val alarmAdapter = AlarmAdapter()
        binding.IDAlarmRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.IDAlarmRecyclerview.adapter = alarmAdapter
        mAlarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        mAlarmViewModel.readAllAlarm.observe(viewLifecycleOwner, Observer {
//            Toast.makeText(requireContext(), ""+it.size, Toast.LENGTH_SHORT).show()
//            if (it.size>4){
//                mAlarmViewModel.deleteALLAlarm()
//            }
            alarmAdapter.setAlarm(it,this,this)
        })

        binding.floatingActionButton.setOnClickListener {
//            val data = TimePickerMe().getDatePicker(requireContext())
//            mAlarmViewModel.addAlarm(data)
////            data.schedule(requireContext())

            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                val alarmId = Random().nextInt(Int.MAX_VALUE)
                val data =AlarmModel(alarmId,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true)
                mAlarmViewModel.addAlarm(data)
                data.schedule(requireContext())
            }
            TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
        binding.textView3.setOnClickListener {
            mAlarmViewModel.deleteALLAlarm()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onToggle(alarm: AlarmModel?) {
        if (alarm!!.started) {
            alarm.cancelAlarm(requireContext())
            mAlarmViewModel.upadteAlarm(alarm)


        } else {

            alarm!!.schedule(requireContext())
            mAlarmViewModel.upadteAlarm(alarm)
        }
    }


    override fun delete(alarm: AlarmModel?) {
        Toast.makeText(requireContext(), "Delete", Toast.LENGTH_SHORT).show()
        Log.d("PKL_Ismail",alarm?.started.toString())
        if (alarm!!.started) {
            alarm.cancelAlarm(requireContext())
            mAlarmViewModel.deleteAlarm(alarm!!)
        }else{
            mAlarmViewModel.deleteAlarm(alarm!!)

        }
    }

}