package com.example.pkl_v1.ui.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pkl_v1.R
import com.example.pkl_v1.databinding.FragmentRegistrasiBinding
import com.example.pkl_v1.model.ModelDataDiriPasien
import com.example.pkl_v1.util.SharedPref
import com.example.pkl_v1.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import java.util.*


class RegistrasiFragment : Fragment() {
    private var _binding: FragmentRegistrasiBinding? = null
    private val binding get() = _binding!!

    private val mAuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }
    var selectedPhotoUri: Uri? = null

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
        binding.IDRegisEdtImage.setOnClickListener { getImage() }
    }
    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100)
    }

    private fun regis() {

        val nama = binding.IDRegisEdtNama.text.toString()
        val email = binding.IDRegisEdtEmail.text.toString()
        val password = binding.IDRegisEdtPassword.text.toString()
        val confirm = binding.IDRegisEdtPasswordConfirm.text.toString()
        val tanggallahir = binding.IDRegisEdtTanggalLahir.text.toString()
        val image = binding.IDRegisEdtImage.text.toString()

        if (nama != "" && email != "" && password != "" && confirm != ""&&selectedPhotoUri!=null) {
            if (password != confirm) {
                Toasty.error(requireContext(), "Pastikan Password sama", Toast.LENGTH_SHORT, true)
                    .show()
            } else {
                uploadImage(selectedPhotoUri).observe(viewLifecycleOwner, {lokasi->
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val user = FirebaseAuth.getInstance().currentUser?.uid as String
//                                mAuthViewModel.setDataRegis(
//                                    ModelDataDiriPasien(
//                                        user,
//                                        nama,
//                                        tanggallahir,
//                                        lokasi
//                                    ), requireContext()
//                                )
                                SharedPref(requireContext()).setAlarmSetStatus(false)
                                findNavController().navigate(R.id.action_registrasiFragment_to_loginFragment)
                            } else {
                                Toasty.error(
                                    requireContext(),
                                    "Regis Failed " + it.exception?.message,
                                    Toast.LENGTH_SHORT,
                                    true
                                ).show()

                            }
                        }
                })

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
    private fun uploadImage(imageUri: Uri?): MutableLiveData<String> {
        val filename = UUID.randomUUID().toString()
        val storage = FirebaseStorage.getInstance().getReference("/PKL/$filename")
        var uri = MutableLiveData<String>()
        storage.putFile(imageUri!!).addOnSuccessListener {
            storage.downloadUrl.addOnSuccessListener {
                Log.d("A","Disimpan ke ${it.toString()}")
                uri.value = it.toString()
            }

        }
        return uri

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK&&requestCode==100&&data!=null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedPhotoUri)
            binding.IDRegisImage.setImageBitmap(bitmap)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}