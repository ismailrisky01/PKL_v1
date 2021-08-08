package com.example.pkl_v1.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pkl_v1.R
import com.example.pkl_v1.databinding.FragmentRegistrasiBinding
import com.example.pkl_v1.model.ModelPasien
import com.example.pkl_v1.viewmodel.AuthViewModel
import com.example.pkl_v1.viewmodel.DashboardViewModel
import com.google.firebase.auth.FirebaseAuth
import es.dmoral.toasty.Toasty


class RegistrasiFragment : Fragment() {
    private var _binding: FragmentRegistrasiBinding? = null
    private val binding get() = _binding!!
    private val mAuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegistrasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.IDRegisBtnRegis.setOnClickListener { regis() }
    }

    private fun regis() {

        val nama = binding.IDRegisEdtNama.text.toString()
        val email = binding.IDRegisEdtEmail.text.toString()
        val password = binding.IDRegisEdtPassword.text.toString()
        val confirm = binding.IDRegisEdtPasswordConfirm.text.toString()
        val umur = binding.IDRegisEdtUmur.text.toString()
        val jenisKelamin = binding.IDRegisEdtJenisKelamin.toString()
        val tinggi = binding.IDRegisEdtTinggiBadan.text.toString()
        val beratBadan = binding.IDRegisEdtBerat.text.toString()

        if (nama != "" && email != "" && password != "" && confirm != "" && umur != "" && jenisKelamin != "" && tinggi != "" && beratBadan != "") {
            if (password != confirm) {
                Toasty.error(requireContext(), "Pastikan Password sama", Toast.LENGTH_SHORT, true)
                    .show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = FirebaseAuth.getInstance().currentUser?.uid as String
                            mAuthViewModel.setDataRegis(
                                ModelPasien(
                                    user,
                                    nama,
                                    umur,
                                    jenisKelamin,
                                    tinggi,
                                    beratBadan
                                ), requireContext()
                            )
                            findNavController().navigate(R.id.action_registrasiFragment_to_dashboardFragment)
                        } else {
                            Toasty.error(
                                requireContext(),
                                "Regis Failed " + it.exception?.message,
                                Toast.LENGTH_SHORT,
                                true
                            ).show()

                        }
                    }
            }

        } else {
            Toasty.warning(
                requireContext(),
                "Please fill all field" ,
                Toast.LENGTH_SHORT,
                true
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}