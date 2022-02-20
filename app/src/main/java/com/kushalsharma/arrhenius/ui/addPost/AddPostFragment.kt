package com.kushalsharma.arrhenius.ui.addPost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.kushalsharma.arrhenius.R
import com.kushalsharma.arrhenius.databinding.FragmentAddpostBinding

class AddPostFragment : Fragment() {

    private var _binding: FragmentAddpostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddpostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Glide.with(this).load(R.drawable.ic_baseline_add_box_24)
            .apply(RequestOptions.bitmapTransform( RoundedCorners(32)))

            .into(binding.addPostPic)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}