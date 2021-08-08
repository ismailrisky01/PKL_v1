package com.example.pkl_v1.ui.dashboard


import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pkl_v1.R
import com.example.pkl_v1.databinding.FragmentDashboardBinding
import com.example.pkl_v1.model.ModelPasien
import com.example.pkl_v1.repository.DashboardRepository
import com.example.pkl_v1.viewmodel.DashboardViewModel
import com.google.firebase.auth.FirebaseAuth


class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val mDashboardViewModel by lazy {
        ViewModelProvider(this).get(DashboardViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkProfile()
        binding.IDDashboardBtnSettingProfile.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_dashboardFragment_to_loginFragment)
        }

        binding.textView9.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_alarmFragment)
        }
    }

    fun toast(string: String) {
        Toast.makeText(requireContext(), "$string", Toast.LENGTH_SHORT).show()
    }

//    fun addProfile() {
//        val setData = mDashboardViewModel.setProfile(
//            ModelPasien(
//                FirebaseAuth.getInstance().currentUser?.uid.toString(),
//                "Alfin",
//                "20",
//                "70",
//                "170"
//            )
//        )
//        if (setData) {
//            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
//        }
//    }

    fun checkProfile() {
        mDashboardViewModel.getProfile().observe(viewLifecycleOwner, Observer {
            if (it.idPasien.isEmpty()) {
//                binding.IDDashboardContainerData.visibility = View.GONE
                Toast.makeText(requireContext(), "Data Pasien Belum ada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Data Available", Toast.LENGTH_SHORT).show()
//                binding.IDDashboardContainerData.visibility = View.VISIBLE
                setUpData(it)
            }
        })
    }

    private fun setUpData(modelPasien: ModelPasien) {
        binding.IDDashboardNamaPasien.text = "Nama : ${modelPasien.namaPaien}"
        binding.IDDashboardUmurPasien.text = "Umur :${modelPasien.umurPasien}"
        binding.IDDashboardJenisKelaminPasien.text = "Jenis Kelamin : ${modelPasien.umurPasien}"
        binding.IDDashboardTxtBeratBadan.text = "Berat badan : ${modelPasien.beratPasien}"
        binding.IDDashboardTxtTinggiBadan.text = "Tinggi badan : ${modelPasien.tinggiPasien}"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> findNavController().navigate(R.id.action_dashboardFragment_to_alarmFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
