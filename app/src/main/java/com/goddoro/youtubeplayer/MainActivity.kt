package com.goddoro.youtubeplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.goddoro.youtubeplayer.databinding.ActivityMainBinding
import com.goddoro.youtubeplayer.presentation.PlayerFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityMainBinding

    private val fragment1 : MainFragment = MainFragment.newInstance()
    private val fragment2: PlayerFragment = PlayerFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        mBinding.lifecycleOwner = this


        setContentView(mBinding.root)
    }

    private fun setupBottomNavigationView() {



    }

    private fun initFragments() {


    }

}