package com.uwange.coffeeapp.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.tasks.Task
import com.uwange.coffeeapp.adapter.ImageSliderAdapter
import com.uwange.coffeeapp.databinding.FragmentHomeBinding
import com.uwange.coffeeapp.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel : HomeFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.imageUrls.observe(viewLifecycleOwner) { urls ->
            setupImageSlider(urls)
        }

        viewModel.fetchImageUrls()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupImageSlider(imageUrls: List<Task<Uri>>) {
        val imageSliderAdapter = ImageSliderAdapter(requireContext(), imageUrls)
        binding.vpImageSlider.adapter = imageSliderAdapter
    }
}