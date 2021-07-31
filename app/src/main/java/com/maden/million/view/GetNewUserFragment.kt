package com.maden.million.view

import android.animation.ObjectAnimator
import android.graphics.Path
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.maden.million.R
import com.maden.million.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.million.databinding.FragmentChatBinding
import com.maden.million.databinding.FragmentGetNewUserBinding
import com.maden.million.util.BannerControl
import com.maden.million.util.UserChatList
import com.maden.million.util.downloadPhoto
import com.maden.million.viewmodel.GetNewUserViewModel
import kotlinx.android.synthetic.main.fragment_get_new_user.*


class GetNewUserFragment : Fragment() {

    private var _binding: FragmentGetNewUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGetNewUserBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private var newUserEmail: String? = null
    private var newUserFullName: String? = null
    private var newUserDownloadPhoto: String = ""
    private var newUserRoomUUID: String? = null

    private lateinit var getNewUserViewModel: GetNewUserViewModel

    private var mRewardedAd: RewardedAd? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GLOBAL_CURRENT_FRAGMENT = "new_user"

        getNewUserViewModel = ViewModelProvider(this)
            .get(GetNewUserViewModel::class.java)

        binding.getNewUserButton.setOnClickListener {
            getNewUserViewModel.getNewUser()
            moveToRocket()
        }


        binding.startChatButton.setOnClickListener {
            startChat(it)
        }


        observeData()

        /////
        //Sonrasında farklı fragment'a alınacak.
        /////
        MobileAds.initialize(requireContext()) {}
        //ca-app-pub-3940256099942544/5224354917
        var adRequest = AdRequest.Builder().build()

        RewardedAd.load(requireContext(),BannerControl.rewardTestID, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                println("Fail LoadError")
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                mRewardedAd = rewardedAd
            }
        })

        binding.rewardButtonGetNewUser.setOnClickListener {
            getReward()
        }
    }


    private fun getReward(){
        mRewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                //println("Reklam gösteriliyor")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                // Called when ad fails to show.
                //println("Reklam gösterilmedi")
            }

            override fun onAdDismissedFullScreenContent() {

                //println("Reklam gösterildi ve kapatıldı")

                getNewUserViewModel.rewardNewUserCount()
                binding.newUserProfileLayout.visibility = View.GONE
                binding.newUserProfileErrorLayout.visibility = View.GONE
                binding.getNewUserButton.visibility = View.VISIBLE
                mRewardedAd = null
            }
        }

        if (mRewardedAd != null) {
            mRewardedAd?.show(requireActivity(), OnUserEarnedRewardListener() {
                fun onUserEarnedReward(rewardItem: RewardItem) {
                    var rewardAmount = rewardItem.amount
                    var rewardType = rewardItem.type
                    //println("Kullanıcı hak kazandı")
                }
            })
        } else {
            //println("Ödülü gösterirken hata oluştu")
        }
    }


    private fun observeData() {
        getNewUserViewModel.newUserDataClass.observe(viewLifecycleOwner, Observer {
            it?.let {

                binding.newUserProfileLayout.visibility = View.VISIBLE
                binding.getNewUserButton.visibility = View.GONE
                binding.newUserNameSurname.text = it.userNameSurname
                binding.newUserUsername.text = it.username
                binding.newUserUserAboutMe.setText(it.aboutMe)

                newUserEmail = it.email
                newUserFullName = it.userNameSurname

                if (it.photoUrl != "") {
                    binding.newUserProfilePhoto.downloadPhoto(it.photoUrl)
                    newUserDownloadPhoto = it.photoUrl
                }
            }
        })

        getNewUserViewModel.infoDataClass.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.newUserProfileErrorLayout.visibility = View.VISIBLE
                binding.getNewUserButton.visibility = View.GONE
                binding.waitUserLayout.visibility = View.GONE

                binding.infoErrorText.text = it.info


            }
        })

        getNewUserViewModel.newChatRoomUUID.observe(viewLifecycleOwner, Observer {
            it?.let {
                newUserRoomUUID = it
            }
        })

        getNewUserViewModel.newUser.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(!it){
                    getNewUserViewModel.searchUSer()
                }
            }
        })

        getNewUserViewModel.waitUser.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    binding.waitUserLayout.visibility = View.VISIBLE
                    binding.getNewUserButton.visibility = View.GONE
                } else if(!it) {
                    binding.waitUserLayout.visibility = View.GONE
                }
            }
        })
    }

    private fun startChat(view: View){
        if (newUserEmail != "" && newUserEmail != null &&
            newUserFullName != "" && newUserFullName != null) {

            getNewUserViewModel.startChat(newUserEmail!!, newUserFullName!!)

            if(newUserRoomUUID != "" && newUserRoomUUID != null){


                navToChat(newUserRoomUUID!!, newUserEmail!!,
                    newUserFullName!!, newUserDownloadPhoto!!, view)
            }
        }
    }
    private fun navToChat(uuid: String, otherEmail: String,
                          otherUsername: String,
                          downloadPhotoUrl: String, view: View){

        val action = GetNewUserFragmentDirections
            .actionGetNewUserFragmentToChatFragment(uuid, otherEmail,
                otherUsername, downloadPhotoUrl)

        view.findNavController().navigate(action)
        GLOBAL_CURRENT_FRAGMENT = "chat"
    }

    private fun moveToRocket() {
        /*
        val path = Path().apply {
            arcTo(0f, 0f, 1000f, 1000f, testAnim.pivotX, -360f, true)
        }
        val animator = ObjectAnimator.ofFloat(testAnim, View.X, View.Y, path).apply {
            duration = 2000
            start()
        }
         */


        ObjectAnimator.ofFloat(binding.planeAnim, "translationX", 3000f).apply {
            duration = 2000
            start()
        }
        ObjectAnimator.ofFloat(binding.planeAnim, "translationY", -3000f).apply {
            duration = 2000
            start()
        }
    }
}