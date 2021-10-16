package com.example.pkl_v1.ui.notif

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pkl_v1.R
import com.example.pkl_v1.databinding.FragmentNotifBinding
import com.example.pkl_v1.databinding.FragmentProfileBinding
import com.example.pkl_v1.viewmodel.DashboardViewModel
import com.example.pkl_v1.viewmodel.NotifViewModel


class NotifFragment : Fragment() {
    private var _binding: FragmentNotifBinding? = null
    private val binding get() = _binding!!
    private val mNotifViewModel by lazy {
        ViewModelProvider(this).get(NotifViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNotifBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNotifViewModel.readAllQuestion.observe(viewLifecycleOwner,{
            val adapter = NotifAdapter()
            binding.rvMessages.layoutManager = LinearLayoutManager(requireContext())
            binding.rvMessages.adapter = adapter
            adapter.setMessage(it)
        })
    }
    override fun onDestroy() {
        super.onDestroy()
    }

}