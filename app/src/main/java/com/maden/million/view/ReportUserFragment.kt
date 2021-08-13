package com.maden.million.view

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.maden.million.R
import com.maden.million.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.million.databinding.FragmentReportUserBinding
import com.maden.million.viewmodel.ProfileViewModel
import com.maden.million.viewmodel.ReportUserViewModel


class ReportUserFragment : Fragment() {

    private var _binding: FragmentReportUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var reportUserViewModel: ReportUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportUserBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        reportUserViewModel = ViewModelProvider(this)
            .get(ReportUserViewModel::class.java)

        observeData()


        binding.reportUserFinishButton.setOnClickListener{

            val nameSurname = binding.reportNameSurname.text.toString().trim()
            val username = binding.reportUserUsername.text.toString().trim()
            val des = binding.reportUserReportText.text.toString().trim()


            if(nameSurname != "" && username != "" && des != ""){
                reportUserViewModel.reportUser(username, nameSurname, des)
                binding.reportUserFinishButton.visibility = View.GONE
            } else {
                Toast.makeText(requireContext(),
                    "Lütfen eksiksiz doldurunuz.",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun observeData(){
        reportUserViewModel.reportUser.observe(viewLifecycleOwner, Observer {
            if(it){
                Toast.makeText(requireContext(),
                    "Şikayetin başarıyla oluşturuldu.",
                    Toast.LENGTH_LONG).show()

                val action = ReportUserFragmentDirections
                    .actionReportUserFragmentToChatListFragment()
                Navigation.findNavController(requireActivity(),
                    R.id.main_fragment_layout)
                    .navigate(action)
                GLOBAL_CURRENT_FRAGMENT = "chat_list"

            } else {
                Toast.makeText(requireContext(),
                    "Sorun yaşandı. Tekrar dener misin ?",
                    Toast.LENGTH_LONG).show()
                binding.reportUserFinishButton.visibility = View.VISIBLE
            }
        })
    }

}