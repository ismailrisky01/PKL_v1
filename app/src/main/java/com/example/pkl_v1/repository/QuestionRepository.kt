package com.example.pkl_v1.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.pkl_v1.data.question.QuestionDAO
import com.example.pkl_v1.model.AlarmModel
import com.example.pkl_v1.model.ModelQuestion
import com.example.pkl_v1.util.LoadingHelper
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuestionRepository(private val questionDAO: QuestionDAO) {
    val readAllData: LiveData<List<ModelQuestion>> = questionDAO.readAllQuestion()

    suspend fun addQuestion(modelQuestion: ModelQuestion) {
        questionDAO.addQuestion(modelQuestion)
    }

    suspend fun updateQuestion(modelQuestion: ModelQuestion) {
        questionDAO.update(modelQuestion)
    }

    suspend fun deleteAllQuestion() {
        questionDAO.deleteAllQuestion()
    }

    suspend fun nilaiQuestion(): Int {
        return questionDAO.nilaiQUestioner()
    }

    fun saveNilai(context: FragmentActivity, view: View) {
        val loading = LoadingHelper(context)
        loading.show()
        GlobalScope.launch {
            if (questionDAO.readNullQuestion().isNotEmpty()) {
                context.runOnUiThread {
                    Toast.makeText(context, "Isi Yang kosong", Toast.LENGTH_SHORT).show()
                    loading.dismiss()
                }
            } else {
                FirebaseDatabase.getInstance().reference.child("Nilai").setValue(nilaiQuestion())
                    .addOnSuccessListener {
                        GlobalScope.launch {
                            loading.dismiss()
                            questionDAO.deleteAllQuestion()
                            context.runOnUiThread {
                                context.findNavController(view.id).popBackStack()
                            }
                        }
                    }
            }


        }

    }


    fun downloadData(): LiveData<MutableList<ModelQuestion>> {
        val mutableList = MutableLiveData<MutableList<ModelQuestion>>()

        FirebaseFirestore.getInstance().collection("PKL/Soal/Kondisi").get()
            .addOnSuccessListener { result ->
                val data = mutableListOf<ModelQuestion>()
                result.forEach { document ->
                    val it = document.toObject(ModelQuestion::class.java)
                    data.add(it)
                    mutableList.value = data
                }
            }
        return mutableList
    }
    fun addData() {
        val data = ArrayList<ModelQuestion>()
        data.add(ModelQuestion("","Anxietas psikis","tidak ada kesukaran mempertahankan tidur","Tidak ada","Sedang","Berat","",0))
        data.add(ModelQuestion("","Gejala somatic GI","Tidak ada","Nafsu makan berkurang tetapi dapat makan tanpa dorongan teman, merasa perutnya penuh","Sukar makan tanpa dorongan teman, membutuhkan pencahar untuk buang air besar atau obat –obatan untuk seluruh pencernaan ","","",0))
        data.add(ModelQuestion("","Gejala somatik umum ","tidak ada","anggota geraknya, punggung atau kepala terasa berat, sakit punggung kepala dan otot-otot, hilangnnya kekuatan dan kemampuan","Gejala-gejala diatas jelas","","",0))
        data.add(ModelQuestion("","Genital(gejala pada genital dan linido)","tidak ada","Ringan","Berat","","",0))
        data.add(ModelQuestion("","Hypochondriasis","tidak ada ","Dihayati sendiri"," preokupasi mengenai kesehatan diri sendiri"," sering mengeluh, membutuhkan pertolongan dan lain-lain","Delusi hypochondriasis ",0))
        data.add(ModelQuestion("","Insight","mengetahui sedang depresi dan sakit","mengetahui sakit tetapi berhubungan dengan penyebab: iklim, makanan, bekerja berlebihan, virus, perlu istirahat dan lain-lain","","","",0))
        data.add(ModelQuestion("","Depersonalisasi dan derealisasi","tidak ada. Misalnya merasa tidak ada realitas ide-ide nihilitas","ringan","sedang","berat","berat sekali (tidak dapat bekerja karena gangguan)",0))
        data.add(ModelQuestion("","Gejala-gejala paranoia","tidak ada","ringan","berat","","",0))
        data.forEach {
            val ref = FirebaseFirestore.getInstance().collection("PKL/Soal/Kondisi").document()
            ref.set(ModelQuestion(ref.id,it.soal,it.pilihan1,it.pilihan2,it.pilihan3,it.pilihan4,it.pilihan5,it.dipilih)).addOnSuccessListener {

            }
            }
    }

}