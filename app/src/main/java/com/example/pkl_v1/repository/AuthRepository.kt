package com.example.pkl_v1.repository

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.pkl_v1.R
import com.example.pkl_v1.model.ModelPasien
import com.example.pkl_v1.util.LoadingHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.dmoral.toasty.Toasty

class AuthRepository {
    private var firestore = FirebaseFirestore.getInstance()

    fun loginUser(
        email: String,
        password: String,
        modelPasien: ModelPasien,
        activity: FragmentActivity,
        view: View
    ) {
        val loading = LoadingHelper(activity)

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) {
                if (it.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser?.uid as String
                    firestore.collection("PKL/Data/Pasien").document(user).set(modelPasien)
                        .addOnSuccessListener {
                            loading.dismiss()
                            activity.findNavController(view.id)
                                .navigate(R.id.action_loginFragment_to_dashboardFragment)

                        }
                    loading.dismiss()
                } else {
                    loading.dismiss()
                    Toasty.error(
                        activity,
                        "" + it.exception?.message,
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                }
            }

    }

    fun setData(modelPasien: ModelPasien, context: Context) {
        val user = FirebaseAuth.getInstance().currentUser?.uid as String
        firestore.collection("PKL/Data/Pasien").document(user).set(modelPasien)
    }

}