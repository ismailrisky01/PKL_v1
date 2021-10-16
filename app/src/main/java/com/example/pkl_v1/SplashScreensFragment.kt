package com.example.pkl_v1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.pkl_v1.databinding.FragmentProfileBinding
import com.example.pkl_v1.databinding.FragmentSplashScreensBinding
import com.example.pkl_v1.model.ModelSchedule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreensFragment : Fragment() {
    private var _binding: FragmentSplashScreensBinding? = null
    private val binding get() = _binding!!




    override fun onResume() {
        super.onResume()
        init()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreensBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    fun init(){
        YoYo.with(Techniques.Bounce).duration(3000).playOn(binding.IDSplashTxt)
        GlobalScope.launch {
            val user = FirebaseAuth.getInstance().currentUser
            delay(3000)
            requireActivity().runOnUiThread {
                if (user!=null){
                    findNavController().navigate(R.id.action_splashScreensFragment_to_dashboardFragment)
                }else{
                    findNavController().navigate(R.id.action_splashScreensFragment_to_loginFragment)
                }
            }
        }
    }


}