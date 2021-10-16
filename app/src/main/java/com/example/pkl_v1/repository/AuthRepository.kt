package com.example.pkl_v1.repository

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.example.pkl_v1.R
import com.example.pkl_v1.model.ModelDataDiriPasien
import com.example.pkl_v1.util.LoadingHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.dmoral.toasty.Toasty

class AuthRepository {
    private var firestore = FirebaseFirestore.getInstance()

    fun loginUser(
        email: String,
        password: String,
        context: Context,
        task:Unit
    ) {
        val loading = LoadingHelper(context)

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser?.uid as String
                    task
                    loading.dismiss()
                } else {
                    loading.dismiss()
                    Toasty.error(
                        context,
                        "" + it.exception?.message,
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                }
            }

    }

    fun setData(modelDataDiriPasien: ModelDataDiriPasien, context: Context) {
        val user = FirebaseAuth.getInstance().currentUser?.uid as String
        firestore.collection("PKL/Data/Pasien").document(user).set(modelDataDiriPasien)
    }

}