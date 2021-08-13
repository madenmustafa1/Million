package com.maden.million.view

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.maden.million.R
import com.maden.million.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.million.databinding.FragmentProfileBinding
import com.maden.million.util.downloadPhoto
import com.maden.million.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var aboutMe: String? = null
    private var instagram: String? = null
    private var facebook: String? = null
    private var twitter: String? = null

    private var selectedBitmap: Bitmap? = null


    private lateinit var profileViewModel: ProfileViewModel
    val intent = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GLOBAL_CURRENT_FRAGMENT = "profile"

        profileViewModel = ViewModelProvider(this)
            .get(ProfileViewModel::class.java)
        profileViewModel.getMyProfile()

        observeMyProfileData()


        registerLauncher()


        binding.userProfilePhoto.setOnClickListener {
            //askForPermissions()
            askForPermissions2(it)

        }

        binding.editProfile.setOnClickListener { goToEditProfile() }

        binding.profileInstagramIcon.setOnClickListener { goToMyInstagram() }
        binding.profileTwitterIcon.setOnClickListener { goToMyTwitter() }
        binding.profileFacebookIcon.setOnClickListener { goToMyFacebook() }


    }


    private fun observeMyProfileData() {
        profileViewModel.profileDataClass.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.userNameSurname.text = it[0].userNameSurname
                binding.username.text = "#" + it[0].username
                binding.userAboutMe.setText(it[0].aboutMe)
                aboutMe = it[0].aboutMe

                binding.userLikeText.text = it[0].like
                binding.userDisLikeText.text = it[0].dislike

                instagram = it[0].instagram
                facebook = it[0].facebook
                twitter = it[0].twitter
            }
        })
        profileViewModel.uProfilePhoto.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.userProfilePhoto.downloadPhoto(it)
            }
        })
    }



    //--------------------

    fun askForPermissions2(view: View) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {

                Snackbar.make(
                    view,
                    "Galeriye gitmek için izin vermelisin",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("İzin veriyorum", View.OnClickListener {
                    //Request Permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                }).show()

            } else {
                //Request Permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        } else {
            val intentToGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)


        }
    }



    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(
                ActivityResultContracts
                    .StartActivityForResult()
            ) { result ->

                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data

                    if (intentFromResult != null) {
                        val imageData = intentFromResult.data
                        //binding.userProfilePhoto.setImageURI(imageData)
                        if (imageData != null) {
                            try {
                                if (Build.VERSION.SDK_INT >= 28) {
                                    val source = ImageDecoder.createSource(requireActivity().contentResolver, imageData)
                                    selectedBitmap = ImageDecoder.decodeBitmap(source)
                                    binding.userProfilePhoto.setImageBitmap(selectedBitmap)
                                    saveProfilePhoto(selectedBitmap!!)

                                } else {
                                    selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageData)

                                    binding.userProfilePhoto.setImageBitmap(selectedBitmap)
                                    saveProfilePhoto(selectedBitmap!!)
                                }


                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                    }
                }

            }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->

            if(result){
                //Permission granted
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                Toast.makeText(requireContext(), "Galeri için izin gerekli!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun makeSmallerBitmap(image: Bitmap, maximumSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio:  Double = width.toDouble() / height.toDouble()

        //Yatay görsel
        if(bitmapRatio > 1) {
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()



        } else {
            //Portrait
            height = maximumSize
            val scaledHeight = height / bitmapRatio
            width = scaledHeight.toInt()

        }

        return Bitmap.createScaledBitmap(image,width, height, true)
    }



    private fun saveProfilePhoto(bitmap: Bitmap){

        val ref = storage.reference
        val bit = bitmap
        val baos = ByteArrayOutputStream()
        bit.compress(Bitmap.CompressFormat.JPEG, 65, baos)
        val data = baos.toByteArray()
        ref.child(auth.currentUser!!.email!!).child("profilePhoto").putBytes(data)
            .addOnSuccessListener {
                var photoUrl: String = ""


                ref.child(auth.currentUser.email)
                    .child("profilePhoto")
                    .downloadUrl.addOnSuccessListener {
                        if (it != null) {
                            photoUrl = it.toString()

                        }
                    }.addOnCompleteListener {
                        photoUrl?.let {
                            var dataU = hashMapOf("photoUrl" to photoUrl)

                            db.collection("Profile")
                                .document(auth.currentUser.email)
                                .set(dataU, SetOptions.merge())
                                .addOnSuccessListener {


                                }
                                .addOnFailureListener {

                                }
                        }
                    }


            }.addOnFailureListener {
                println(it.localizedMessage)
            }

    }












    private fun goToEditProfile() {

        var instagram1: String = ""
        var facebook1: String = ""
        var twitter1: String = ""
        var aboutMe1: String = ""

        if (instagram != null) {
            instagram1 = instagram.toString()
        }
        if (facebook != null) {
            facebook1 = facebook.toString()
        }
        if (twitter != null) {
            twitter1 = twitter.toString()
        }
        if (aboutMe != null) {
            aboutMe1 = aboutMe.toString()
        }

        val action = ProfileFragmentDirections
            .actionProfileFragmentToEditProfileFragment(
                twitter1, facebook1,
                aboutMe1, instagram1
            )

        Navigation.findNavController(
            requireActivity(),
            R.id.main_fragment_layout
        )
            .navigate(action)

        GLOBAL_CURRENT_FRAGMENT = "edit_profile"

    }

    private fun goToMyInstagram() {
        if (instagram != null && instagram != "") {
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse("https://www.instagram.com/$instagram")
            startActivity(intent)
        } else {
            Toast.makeText(
                context,
                "Instagram adresi bulunamadı",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun goToMyTwitter() {
        if (twitter != null && twitter != "") {
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse("https://www.twitter.com/$twitter")
            startActivity(intent)
        } else {
            Toast.makeText(
                context,
                "Twitter adresi bulunamadı",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun goToMyFacebook() {
        if (facebook != null && facebook != "") {
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse("https://www.facebook.com/$facebook")
            startActivity(intent)
        } else {
            Toast.makeText(
                context,
                "Facebook adresi bulunamadı",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    //####### ####### ####### ####### #######
    //#######  HEPSİ ViewModel'e geçecek #######
    //####### ####### ####### ####### #######
    private fun askForPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireView().context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1
            )
        } else {
            val intentGallery = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(intentGallery, 2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                val intentGallery = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(intentGallery, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    var selectedPicture: Uri? = null
    private val storage = FirebaseStorage.getInstance()

    private var db = Firebase.firestore
    private var auth = Firebase.auth
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPicture = data.data
            try {
                if (selectedPicture != null) {
                    if (Build.VERSION.SDK_INT >= 28) {
                        val source = ImageDecoder.createSource(
                            requireActivity().contentResolver, selectedPicture!!
                        )
                        var bitmap = ImageDecoder.decodeBitmap(source)
                        userProfilePhoto.setImageBitmap(bitmap)


                        val ref = storage.reference
                        val bit = bitmap
                        val baos = ByteArrayOutputStream()
                        bit.compress(Bitmap.CompressFormat.JPEG, 80, baos)
                        val data = baos.toByteArray()
                        ref.child(auth.currentUser!!.email!!).child("profilePhoto").putBytes(data)
                            .addOnSuccessListener {
                                var photoUrl: String = ""


                                ref.child(auth.currentUser.email)
                                    .child("profilePhoto")
                                    .downloadUrl.addOnSuccessListener {
                                        if (it != null) {
                                            photoUrl = it.toString()

                                        }
                                    }.addOnCompleteListener {
                                        photoUrl?.let {
                                            var dataU = hashMapOf("photoUrl" to photoUrl)

                                            db.collection("Profile")
                                                .document(auth.currentUser.email)
                                                .set(dataU, SetOptions.merge())
                                                .addOnSuccessListener {


                                                }
                                                .addOnFailureListener {

                                                }
                                        }
                                    }


                            }.addOnFailureListener {
                                println(it.localizedMessage)
                            }

                    } else {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver, selectedPicture
                        )
                        userProfilePhoto.setImageBitmap(bitmap)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error!", Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}