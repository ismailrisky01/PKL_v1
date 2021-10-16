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
import com.example.pkl_v1.databinding.FragmentLoginBinding
import com.example.pkl_v1.databinding.FragmentRegistrasiBinding
import com.example.pkl_v1.model.ModelSchedule
import com.example.pkl_v1.util.LoadingHelper
import com.example.pkl_v1.util.SharedPref
import com.example.pkl_v1.viewmodel.AuthViewModel
import com.example.pkl_v1.viewmodel.DashboardViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import es.dmoral.toasty.Toasty


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val mAuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLogin()
        binding.IDLoginBtnLogin.setOnClickListener { login() }
        binding.IDLoginBtnRegis.setOnClickListener { regis() }

    }

    private fun checkLogin() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
            Toasty.info(
                requireContext(),
                "Welcome",
                Toast.LENGTH_SHORT,
                true
            ).show()
        }
    }

    private fun login() {
        val loading = LoadingHelper(requireContext())
        val email = binding.IDLoginEdtEmail.text.toString()
        val password = binding.IDLoginEdtPassword.text.toString()
        if (email.isEmpty()&&password.isEmpty()){
            Toasty.warning(
                requireContext(),
                "Please Fill All Field" ,
                Toast.LENGTH_SHORT,
                true
            ).show()
        }else{
            loading.show()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) {
                    if (it.isSuccessful) {

                        ModelSchedule().cancleAktivitas(requireContext())
                        val data = ModelSchedule()
                        data.setAlarm(requireContext())
                        data.setAktivitas(requireContext())
                        findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                        SharedPref(requireContext()).setAlarmSetStatus(false)
                        loading.dismiss()
                    } else {
                        loading.dismiss()
                        Toasty.error(
                            requireContext(),
                            "" + it.exception?.message,
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                    }
                }
        }
    }

    private fun regis() {
        findNavController().navigate(R.id.action_loginFragment_to_registrasiFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}