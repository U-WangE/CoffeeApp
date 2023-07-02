package com.uwange.coffeeapp.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.Task
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.uwange.coffeeapp.R
import com.uwange.coffeeapp.adapter.ImageSliderAdapter
import com.uwange.coffeeapp.databinding.DialogCircleCloseBinding
import com.uwange.coffeeapp.databinding.FragmentHomeBinding
import com.uwange.coffeeapp.utils.CommonUtil.showToast
import com.uwange.coffeeapp.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private companion object {
        private const val TAG_BARCODE = "BARCODE"
        private const val TAG_QRCODE = "QRCODE"
    }

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
        setUserName()
        setupBarcodeDialog()

        // 현재 coupon point set
        setUserCouponPoint()
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

    // coupon stamp 이미지 생성
    private fun setupStampsImage() {
        val couponPointCount = viewModel.getCouponPointData()
        val couponMaxPoint = 5
        // Assuming you have a LinearLayout with id "linearContainer" defined in your XML layout file
        val linearContainer: LinearLayout = binding.llStamps

        // Array of bottom margins in dp
        val topMargins = arrayOf(3, 15, 20, 15, 3)

        // Coupon Image Setup
        val imageResources: Array<Int> = Array(couponMaxPoint) {
            if (couponPointCount >= couponMaxPoint - it)
                R.drawable.ic_coffee_bean_filled
            else
                R.drawable.ic_coffee_bean
        }

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
    private fun Resources.dpToPx(dp: Int): Int {
        val scale = displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    private fun setupBarcodeDialog() {
        // 이미지 뷰 및 이미지 설정
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap("123123214",
            BarcodeFormat.CODE_128, 100, 50)
        binding.ivBarcodeOrQr.setImageBitmap(bitmap)

        binding.ivBarcodeOrQr.setOnClickListener {
            createBarcode()
        }
    }

    fun createBarcode() {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_circle_close, null)
        val dialogBinding = DialogCircleCloseBinding.bind(dialogView)
        val bitmap = BarcodeEncoder().encodeBitmap(viewModel.getUserId(), BarcodeFormat.CODE_128, 200, 100)
        dialogBinding.ivCodeImage.setImageBitmap(bitmap)
        dialogBinding.btSwitchBarcodeOrQr.tag = TAG_QRCODE

        dialogBinding.btSwitchBarcodeOrQr.setOnClickListener {
            when (dialogBinding.btSwitchBarcodeOrQr.tag) {
                TAG_BARCODE -> {
                    dialogBinding.ivCodeImage.setImageBitmap(
                        BarcodeEncoder().encodeBitmap(viewModel.getUserId(), BarcodeFormat.CODE_128, 200, 100)
                    )
                    dialogBinding.btSwitchBarcodeOrQr.tag = TAG_QRCODE
                    dialogBinding.btSwitchBarcodeOrQr.text = TAG_BARCODE
                }
                TAG_QRCODE -> {
                    dialogBinding.ivCodeImage.setImageBitmap(
                        BarcodeEncoder().encodeBitmap(viewModel.getUserId(), BarcodeFormat.QR_CODE, 200, 200)
                    )
                    dialogBinding.btSwitchBarcodeOrQr.tag = TAG_BARCODE
                    dialogBinding.btSwitchBarcodeOrQr.text = TAG_QRCODE
                }
                else -> {
                    showToast(requireContext(), "Code Load Error")
                }
            }
        }

        builder.setView(dialogView)
        val alertDialog = builder.create()

        dialogBinding.btClose.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUserName() {
        var isLongPressed = false
        binding.tvUserName.text = viewModel.getUserName()
        binding.tvUserName.setOnLongClickListener {
            (it as TextView).maxLines = Int.MAX_VALUE
            it.ellipsize = null
            isLongPressed = true
            false
        }
        binding.tvUserName.setOnTouchListener { it, event ->
            if (event.action == MotionEvent.ACTION_UP && isLongPressed) {
                (it as TextView).maxLines = 1
                it.ellipsize = TextUtils.TruncateAt.END
                isLongPressed = false
            }
            false
        }
    }

    /// stamp image 변경  적용해야함
    private fun setUserCouponPoint() {
        binding.tvStampsCounter.text = getString(R.string.coupon_counter, viewModel.getCouponPointData())
    }
}