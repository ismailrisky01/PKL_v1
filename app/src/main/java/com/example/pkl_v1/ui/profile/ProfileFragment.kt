package com.example.pkl_v1.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pkl_v1.R
import com.example.pkl_v1.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = FirebaseAuth.getInstance().currentUser?.displayName

        val user = FirebaseAuth.getInstance().currentUser!!
        binding.IDProfilNamaUser.text = user.displayName
        binding.IDProfileBtnLogOut.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_questionnaireFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}