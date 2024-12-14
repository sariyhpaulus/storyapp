package com.bangkit.storyapp.view.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.storyapp.R
import com.bangkit.storyapp.databinding.FragmentHomeBinding
import com.bangkit.storyapp.view.ViewModelFactory
import com.bangkit.storyapp.view.StoryAdapter
import com.bangkit.storyapp.view.detail.DetailStoryActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val adapter = StoryAdapter(::navigateToDetail)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)
        _binding = binding

        _binding?.rvStory?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = this@HomeFragment.adapter
        }

        with(viewModel) {
            getStoriesWithToken()

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }

            listStory.observe(viewLifecycleOwner) { stories ->
                adapter.submitList(stories)
                binding.rvStory.adapter = adapter
                binding.rvStory.visibility = View.VISIBLE
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        _binding?.progressBar?.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun navigateToDetail(id: String){
        DetailStoryActivity.start(requireContext(), id)
    }
}