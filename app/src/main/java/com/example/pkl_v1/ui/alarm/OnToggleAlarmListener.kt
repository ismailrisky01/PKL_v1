package com.example.pkl_v1.ui.alarm

import com.example.pkl_v1.model.AlarmModel
import com.example.pkl_v1.model.ModelQuestion

interface OnToggleAlarmListener {
    fun onToggle(alarm: AlarmModel?)
}
interface PilihanListener{
    fun pilihan(modelQuestion: ModelQuestion)
}
interface DeleteListener{
    fun delete(alarm: AlarmModel?)
}
