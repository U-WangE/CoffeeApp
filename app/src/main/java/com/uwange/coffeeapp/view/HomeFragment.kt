package com.uwange.coffeeapp.view

import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.Task
import com.uwange.coffeeapp.R
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
        setupStampsImage()
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

    //TODO::차후 infinite pager adapter, auto slider 제작
    private fun setupImageSlider(imageUrls: List<Task<Uri>>) {
        val imageSliderAdapter = ImageSliderAdapter(requireContext(), imageUrls, binding.pbProgressBar)
        binding.vpImageSlider.adapter = imageSliderAdapter
        binding.tvViewPagerCounter.text = getString(R.string.viewpager2_banner, 1, imageUrls.size)
        binding.vpImageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tvViewPagerCounter.text = getString(R.string.viewpager2_banner, position + 1, imageUrls.size)
            }
        })
    }

    fun setupStampsImage() {

        // Assuming you have a LinearLayout with id "linearContainer" defined in your XML layout file
        val linearContainer: LinearLayout = binding.llStamps

        // Array of bottom margins in dp
        val topMargins = arrayOf(3, 15, 20, 15, 3)

        // Array of image resource IDs
        val imageResources = arrayOf(
            R.drawable.icon_coffee_bean,
            R.drawable.icon_coffee_bean,
            R.drawable.icon_coffee_bean,
            R.drawable.icon_coffee_bean,
            R.drawable.icon_coffee_bean
        )
        for (i in topMargins.indices) {
            val imageView = ImageView(requireContext())
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(
                0,
                resources.dpToPx(topMargins[i]),
                0,
                0
            ) // Convert dp to pixels
            layoutParams.width = resources.dpToPx(50) // Set a fixed width of 50dp
            layoutParams.height = resources.dpToPx(50) // Set a fixed height of 50dp
            imageView.layoutParams = layoutParams
            imageView.setImageResource(imageResources[i])

            linearContainer.addView(imageView)
        }
    }

    // Extension function to convert dp to pixels
    fun Resources.dpToPx(dp: Int): Int {
        val scale = displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}