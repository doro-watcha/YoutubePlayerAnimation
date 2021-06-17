package com.goddoro.youtubeplayer.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.goddoro.youtubeplayer.databinding.FragmentPlayerBinding

class PlayerFragment : Fragment(){

    private lateinit var mBinding : FragmentPlayerBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPlayerBinding.inflate(inflater, container,false).also { mBinding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.lifecycleOwner = viewLifecycleOwner

    }
}