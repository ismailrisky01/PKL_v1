package com.example.pkl_v1.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.app.ActivityManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pkl_v1.R
import com.example.pkl_v1.databinding.FragmentProfileBinding
import com.example.pkl_v1.model.ModelSchedule
import com.example.pkl_v1.viewmodel.DashboardViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val mDashboardViewModel by lazy {
        ViewModelProvider(this).get(DashboardViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDashboardViewModel.getProfile().observe(viewLifecycleOwner, {
            Picasso.get().load(it.fotoPasien).into(binding.IDProfileImage)
            binding.apply {
                IDProfilNamaUser.text = it.namaPaien
                IDProfilJenisKelamin.text = it.jenisKelamin
                IDProfilUmur.text = it.umurPasien
                IDProfilEmail.text = FirebaseAuth.getInstance().currentUser?.email
                IDProfilPekerjaan.text = it.pekerjaan
                IDProfilTanggalLahir.text = it.tanggalPasien
            }
        })

        binding.apply {
            cardView7.visibility = View.GONE
            IDProfileTxtUpdate.setOnClickListener {
                binding.cardView7.visibility = View.VISIBLE

                binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }
            IDProfileBtnLogOut.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext()).setCancelable(false)
                    .setMessage("Apakah anda yakin ingin keluar?")
                    .setNegativeButton("Tidak") { _, _ ->
                    }
                    .setPositiveButton("Ya") { dialog, which ->
                        ModelSchedule().cancleAlarm(requireContext())
                        ModelSchedule().cancleAktivitas(requireContext())
                        FirebaseAuth.getInstance().signOut()
                        findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                    }.show()
            }
            IDProdileBtnResetPassword.setOnClickListener {
                cardView7.visibility = View.VISIBLE
                val password = IDProfileEdtPassword.text.toString()
                val confirm = IDProfileEdtPasswordConfirm.text.toString()
                if (password == confirm) {
                    FirebaseAuth.getInstance().currentUser!!.updatePassword(password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toasty.success(
                                    requireContext(),
                                    "Password Updated",
                                    Toasty.LENGTH_SHORT
                                ).show()
                            }
                        }.addOnFailureListener {
                        Toasty.success(requireContext(), "Password Updated", Toasty.LENGTH_SHORT)
                            .show()

                    }

                }else{
                    Toasty.warning(requireContext(),"Password tidak sama",Toasty.LENGTH_LONG).show()
                }
            }
        }


//        binding.IDProdileBtnResetPassword.setOnClickListener {
//            Toasty.success(requireContext(), "Password  ", Toasty.LENGTH_SHORT).show()
//
//            val email = FirebaseAuth.getInstance().currentUser!!.email as String
//            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
//                Toasty.success(
//                    requireContext(),
//                    "Password Has Been Reset, Pelase check email: $email ",
//                    Toasty.LENGTH_SHORT
//                ).show()
//            }.addOnFailureListener {
//                Toasty.warning(requireContext(), it.message.toString(), Toasty.LENGTH_SHORT).show()
//            }.addOnCanceledListener {
//                Toasty.warning(requireContext(), "Cancled", Toasty.LENGTH_SHORT).show()
//
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}