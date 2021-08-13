package com.maden.million.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.*
import com.maden.million.databinding.FragmentBannerBinding
import com.google.android.gms.ads.MobileAds
import com.maden.million.util.BannerControl
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BannerFragment : Fragment() {

    private var _binding: FragmentBannerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        showBanner = false
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBannerBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    var showBanner: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MobileAds.initialize(requireContext()) {}
        //val adView = AdView(requireContext())
        //adView.adSize = AdSize.BANNER
        //adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"

        if(BannerControl.bannerShow != true){
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
            BannerControl.bannerShow = true

            MainScope().launch {
                destroyBanner()
            }

        }


    }

    private suspend fun destroyBanner(){
        delay(5000)
        if(showBanner == true){
            binding.adView.visibility = View.GONE
        }
    }
}
