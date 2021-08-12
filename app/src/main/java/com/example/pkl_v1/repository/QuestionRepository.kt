package com.example.pkl_v1.repository

import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.pkl_v1.data.QuestionDAO
import com.example.pkl_v1.model.ModelNilai
import com.example.pkl_v1.model.ModelNilaiPasien
import com.example.pkl_v1.model.ModelQuestion
import com.example.pkl_v1.util.LoadingHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

    suspend fun totalQuestion(): Int {
        return questionDAO.countQUestioner()
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
                val uid = FirebaseAuth.getInstance().currentUser?.uid as String
                val date = SimpleDateFormat("ddMyyyy")
                val currentDateNow = date.format(Date())

                val total = nilaiQuestion()-totalQuestion()
                val ref = FirebaseFirestore.getInstance().collection("PKL/Question/$uid").document(currentDateNow)
                val data = ModelNilaiPasien(uid,currentDateNow,total)
                ref.set(data)
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
        data.add(ModelQuestion("",16,"Kehilangan berat badan, Bila hanya riwayatnya","tidak ada kehilangan bent badan"," kemungkinan bent badan berkurang berhubungan dengan sakit sekarang"," jelas (menurut pasien )berkurang berat badannya","tidak jelas lagi penurunan bent badan","",0))
         data.add(ModelQuestion("",17,"Kehilangan berat badan, Di bawah pengawasan dokter bangsal secara mingguan bila jelas berat badan berkurang" +
                 "menurut ukuran","kurang dari 0,5kg seminggu","lebih dari 0,5kg seminggu","lebih dari 1kg seminggu"," tidak dinyatakan lagi kehilangan berat badan","",0))
        data.add(ModelQuestion("",16,"Variasi harian, Catat mana yang lebih berat pagi atau malam, kalau tidak ada gangguan beri tanda nol","tidak ada perubahan","  lebih berat waktu malam"," lebih buruk waktu pagi","","",0))
        data.add(ModelQuestion("",16,"Variasi harian, Kalau ada perubahan tandai derajat perubahan tersebut, tandai nol bila tidak ada perubahan","tidak ada"," ringan"," berat","","",0))

        data.forEach {
            val ref = FirebaseFirestore.getInstance().collection("PKL/Soal/Kondisi").document()
            ref.set(ModelQuestion(ref.id,it.noSoal,it.soal,it.pilihan1,it.pilihan2,it.pilihan3,it.pilihan4,it.pilihan5,it.dipilih)).addOnSuccessListener {

            }
            }
    }

}