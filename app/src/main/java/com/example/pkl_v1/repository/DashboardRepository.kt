package com.example.pkl_v1.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pkl_v1.model.ModelActivity
import com.example.pkl_v1.model.ModelPasien
import com.example.pkl_v1.model.ModelPasienActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class DashboardRepository {

    var firestore = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid as String

    init {

    }

    fun setProfile(modelPasien: ModelPasien): Boolean {
        val bool = firestore.collection("PKL/Data/Pasien").document(uid).set(modelPasien).isSuccessful
        return bool
    }

    fun getProfile(): LiveData<ModelPasien> {
        val mutableData = MutableLiveData<ModelPasien>()
        firestore.collection("PKL/Data/Pasien").document(uid).get().addOnSuccessListener {
            if (it.exists()) {
                val data = it.toObject(ModelPasien::class.java)
                mutableData.value = data!!
            }else{
                mutableData.value = ModelPasien("","","","","","")
            }
        }.addOnFailureListener {  }
        return mutableData
    }

    fun setActivityPasien(modelActivity: ModelActivity){
        val uid = FirebaseAuth.getInstance().currentUser?.uid as String
        val date = SimpleDateFormat("ddMyyyy")
        val currentDateNow = date.format(Date())
        val ref = firestore.collection("PKL/Activity/$uid").document(currentDateNow)
            ref.set(ModelPasienActivity(ref.id,modelActivity.duduk,modelActivity.berdiri,modelActivity.berbaring))
    }
}