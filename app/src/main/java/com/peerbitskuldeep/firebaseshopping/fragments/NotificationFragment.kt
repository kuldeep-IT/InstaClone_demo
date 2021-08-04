package com.peerbitskuldeep.firebaseshopping.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.peerbitskuldeep.firebaseshopping.R


class NotificationFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_notification, container, false)


        return view
    }

    companion object
    {
        fun newInstance()= NotificationFragment().apply {  }
    }
}