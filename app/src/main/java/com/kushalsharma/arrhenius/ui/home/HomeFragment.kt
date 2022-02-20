package com.kushalsharma.arrhenius.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kushalsharma.arrhenius.R
import com.kushalsharma.arrhenius.dao.PostDao
import com.kushalsharma.arrhenius.databinding.FragmentHomeBinding
import com.kushalsharma.arrhenius.modals.Post
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), IPostAdapter {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var postDao: PostDao
    private lateinit var adapter: PostAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.myProfilePic.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_navigation_account)
        }

        auth = Firebase.auth

        val currentUser = auth.currentUser!!


        Glide.with(binding.myProfilePic)
            .load(currentUser.photoUrl).circleCrop().into(binding.myProfilePic)

        setupRecyclerView()


        return root
    }

    private fun setupRecyclerView() {
        postDao = PostDao()
        val postsCollections = postDao.postCollections
        val query = postsCollections.orderBy("createdAt").limit(100)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions, this)

       binding.postRv.adapter = adapter
        binding.postRv.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}