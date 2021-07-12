package com.maden.million.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.maden.million.R
import com.maden.million.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.million.viewmodel.GetNewUserViewModel


class GetNewUserFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_new_user, container, false)
    }


    private lateinit var getNewUserViewModel: GetNewUserViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GLOBAL_CURRENT_FRAGMENT = "new_user"

        getNewUserViewModel = ViewModelProvider(this)
            .get(GetNewUserViewModel::class.java)

        getNewUserViewModel.getNewUser()
    }
}