package com.example.pkl_v1.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pkl_v1.model.ModelActivity
import com.example.pkl_v1.model.ModelDataDiriPasien
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class DashboardRepository {

    var firestore = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid as String

    init {

    }

    fun setProfile(modelDataDiriPasien: ModelDataDiriPasien): Boolean {
        val bool = firestore.collection("PKL/Data/Pasien").document(uid).set(modelDataDiriPasien).isSuccessful
        return bool
    }

    fun getProfile(): LiveData<ModelDataDiriPasien> {
        val mutableData = MutableLiveData<ModelDataDiriPasien>()
        firestore.collection("PKL/Data/Pasien").document(uid).get().addOnSuccessListener {
            if (it.exists()) {
                val data = it.toObject(ModelDataDiriPasien::class.java)
                mutableData.value = data!!
            }else{
                mutableData.value = ModelDataDiriPasien("","","","")
            }
        }.addOnFailureListener {  }
        return mutableData
    }

    fun setActivityPasien(modelActivity: ModelActivity){
        val date = SimpleDateFormat("ddMMyyyy")
        val currentDateNow = date.format(Date())
        val ref = firestore.collection("PKL/Activity/$uid").document(currentDateNow)
            ref.set(ModelActivity(ref.id.toInt(),modelActivity.sit,modelActivity.stand,modelActivity.walk,modelActivity.run))
    }

    fun getActivityPasien(date:String):LiveData<ModelActivity>{
        val mutableData = MutableLiveData<ModelActivity>()
        firestore.collection("PKL/Activity/$uid").document(date).get().addOnSuccessListener {
            if (it.exists()) {
                val data = it.toObject(ModelActivity::class.java)
                mutableData.value = data!!
            }else{
                mutableData.value = ModelActivity(0,0,0,0,0)
            }
        }.addOnFailureListener {  }
        return mutableData

    }


}