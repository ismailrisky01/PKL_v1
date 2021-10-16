package com.example.pkl_v1.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pkl_v1.model.ModelActivity
import com.example.pkl_v1.model.ModelChat
import com.example.pkl_v1.model.ModelDataDiriPasien
import com.example.pkl_v1.util.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class Repository {

    var firestore = FirebaseFirestore.getInstance()
    var uid = FirebaseAuth.getInstance().currentUser?.uid as String

    fun updateUid(uidNew: String) {
        uid = uidNew
    }

    fun setProfile(modelDataDiriPasien: ModelDataDiriPasien): Boolean {
        val bool = firestore.collection("PKL/Data/Pasien").document(uid)
            .set(modelDataDiriPasien).isSuccessful
        return bool
    }

    fun getProfile(): LiveData<ModelDataDiriPasien> {
        val mutableData = MutableLiveData<ModelDataDiriPasien>()
        firestore.collection("PKL/Data/Pasien").document(uid).get().addOnSuccessListener {
            if (it.exists()) {
                val data = it.toObject(ModelDataDiriPasien::class.java)
                mutableData.value = data!!
            } else {
                mutableData.value = ModelDataDiriPasien("", "", "", "", "", "", "", "", "")
            }
        }.addOnFailureListener { }
        return mutableData
    }

    fun setActivityPasien(context: Context,modelActivity: ModelActivity) {
        val date = SimpleDateFormat("ddMMyyyy")
        val currentDateNow = date.format(Date())
        val ref = firestore.collection("PKL/Activity/$uid").document(currentDateNow)
        val mypref = SharedPref(context)
        ref.set(
            ModelActivity(
                ref.id.toInt(),
                mypref.getSit()/7200,
                mypref.getStand()/7200,
                mypref.getWalk()/7200,
                mypref.getRun()/7200
            )
        )
    }

    fun getActivityPasien(date: String): LiveData<ModelActivity> {
        val mutableData = MutableLiveData<ModelActivity>()
        firestore.collection("PKL/Activity/$uid").document(date).get().addOnSuccessListener {
            if (it.exists()) {
                val data = it.toObject(ModelActivity::class.java)
                mutableData.value = data!!
            } else {
                mutableData.value = ModelActivity(0, 0, 0, 0, 0)
            }
        }.addOnFailureListener { }
        return mutableData

    }

    fun getMessage():LiveData<MutableList<ModelChat>> {
        val mutableList = MutableLiveData<MutableList<ModelChat>>()

        firestore.collection("PKL/Chat/$uid").get().addOnSuccessListener { result ->
            val data = mutableListOf<ModelChat>()
            result.forEach { document ->
                val it = document.toObject(ModelChat::class.java)
                data.add(it)
            }
            mutableList.value = data

        }
        return mutableList
    }



}