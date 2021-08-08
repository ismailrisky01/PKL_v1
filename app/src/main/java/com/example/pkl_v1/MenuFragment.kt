package com.example.pkl_v1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pkl_v1.databinding.FragmentDashboardBinding
import com.example.pkl_v1.databinding.FragmentMenuBinding


class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.IDMenuBerdiri.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key", "stand")
            findNavController().navigate(R.id.action_menuFragment2_to_recordFragment, bundle)
        }
        binding.IDMenuDuduk.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key", "sit")
            findNavController().navigate(R.id.action_menuFragment2_to_recordFragment, bundle)
        }
        binding.IDMenuBerbaring.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key", "sleep")
            findNavController().navigate(R.id.action_menuFragment2_to_recordFragment, bundle)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}