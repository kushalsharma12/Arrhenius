package com.kushalsharma.arrhenius.ui.addPost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kushalsharma.arrhenius.R
import com.kushalsharma.arrhenius.dao.PostDao
import com.kushalsharma.arrhenius.databinding.FragmentAddpostBinding


class AddPostFragment : Fragment() {

    private var _binding: FragmentAddpostBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var postDao: PostDao


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddpostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = Firebase.auth
        postDao = PostDao()


        Glide.with(this).load(R.drawable.ic_baseline_add_box_24)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(32)))

            .into(binding.addPostPic)

        binding.postButton.setOnClickListener {
            val input = binding.addpostDescp.text.toString().trim()
            if (input.isNotEmpty()) {
                postDao.addPost(input)
                Navigation.findNavController(it)
                    .navigate(R.id.action_navigation_post_to_navigation_home)

            } else {
                Toast.makeText(this.context, "Please enter something in it!", Toast.LENGTH_SHORT)
                    .show()
            }


        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}