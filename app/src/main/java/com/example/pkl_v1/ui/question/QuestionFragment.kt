package com.example.pkl_v1.ui.question

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pkl_v1.databinding.FragmentQuestionnaireBinding
import com.example.pkl_v1.model.ModelQuestion
import com.example.pkl_v1.ui.alarm.PilihanListener
import com.example.pkl_v1.util.LoadingHelper
import com.example.pkl_v1.util.SharedPref
import com.example.pkl_v1.viewmodel.QuestionViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class QuestionFragment : Fragment(), PilihanListener {
    private var _binding: FragmentQuestionnaireBinding? = null
    private val binding get() = _binding!!
    private val mQuestionViewModel by lazy {
        ViewModelProvider(this).get(QuestionViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentQuestionnaireBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkData()
        binding.IDQuestionBtnSubmit.setOnClickListener {
            mQuestionViewModel.uploadNilai(requireActivity(),it)
            SharedPref(requireContext()).setAlarmSetStatus(false)
        }

        binding.IDDaftarPolisiBtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun checkData() {
        val loading = LoadingHelper(requireContext())
        loading.show()
        val adapter = QuestionAdapter()
        binding.IDQuestionRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.IDQuestionRecyclerview.adapter = adapter
        mQuestionViewModel.readAllQuestion.observe(viewLifecycleOwner, Observer { it ->
            if (it.isEmpty()) {

                mQuestionViewModel.downloadData().observe(viewLifecycleOwner, Observer { data ->
                    data.forEach {
                        mQuestionViewModel.addData(it)
                    }
                   val datas = it.sortedBy {
                        it.noSoal
                    }
                    adapter.setData(datas,this)
//                    mQuestionViewModel.readAllQuestion.removeObservers(viewLifecycleOwner)
                    loading.dismiss()
                })
            } else {
//                Toast.makeText(requireContext(), "Data Available", Toast.LENGTH_SHORT).show()

                val datas = it.sortedBy {
                    it.noSoal
                }
                adapter.setData(datas,this)
                adapter.notifyDataSetChanged()
//                mQuestionViewModel.readAllQuestion.removeObservers(viewLifecycleOwner)
                loading.dismiss()
            }
        })

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun pilihan(modelQuestion: ModelQuestion) {
            mQuestionViewModel.upadteQuestion(modelQuestion)
    }
}