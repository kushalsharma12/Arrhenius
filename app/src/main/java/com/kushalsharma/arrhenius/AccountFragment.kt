package com.kushalsharma.arrhenius

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kushalsharma.arrhenius.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = Firebase.auth
        val currentUser = auth.currentUser!!

        binding.userName.text = currentUser.displayName!!.toString()
        binding.userEmail.text = currentUser.email!!.toString()

        Glide.with(binding.userImg)
            .load(currentUser.photoUrl).circleCrop().into(binding.userImg)


        binding.btnChangewalletAddress.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_navigation_account_to_navigation_wallet)
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}