package com.goddoro.youtubeplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.goddoro.youtubeplayer.databinding.ActivityMainBinding
import com.goddoro.youtubeplayer.presentation.PlayerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_player.*

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityMainBinding

    private val fragment1 : MainFragment = MainFragment.newInstance()
    private val fragment2: PlayerFragment = PlayerFragment.newInstance()

    var curMainMenu = R.id.gnb_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        mBinding.lifecycleOwner = this


        setContentView(mBinding.root)

        setupBottomNavigationView()
        initFragment()
    }


    private fun loadPlayerFragment() {

        if ( supportFragmentManager.findFragmentById(R.id.playerContainer) == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.playerContainer, fragment2, "3")
                .show(fragment2).commit()
        }
        else {
            (supportFragmentManager.findFragmentById(R.id.playerContainer)).also {
                it as PlayerFragment

                if (it.playerMotionLayout.currentState == R.id.collapsed)
                    it.playerMotionLayout.transitionToEnd()
                else {

                }
            }

        }
    }

    private fun setupBottomNavigationView() {


        val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->

                if ( menuItem.itemId != R.id.gnb_video) curMainMenu = menuItem.itemId

                when (menuItem.itemId) {
                    R.id.gnb_home -> {
                        // 홈

                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.gnb_video -> {
                        //온에어
                        loadPlayerFragment()
                        return@OnNavigationItemSelectedListener false
                    }
                    R.id.gnb_profile -> {
                        // 다시보기
                        return@OnNavigationItemSelectedListener true
                    }
                    else -> {
                        // MY
                        return@OnNavigationItemSelectedListener false
                    }
                }
                false
            }

        mBinding.bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


    }

    private fun initFragment() {

        supportFragmentManager.beginTransaction().add(R.id.container, fragment1,"1").show(fragment1).commit()

    }

    override fun onBackPressed() {

        (supportFragmentManager.findFragmentById(R.id.playerContainer)).also {
            /**
             * PlayerFragment가 없다면 BottomNaviagtion Index 변경
             */
            if (it == null) {
                if ( curMainMenu == R.id.gnb_home) super.onBackPressed()
                else mBinding.bottomNavigationView.selectedItemId = R.id.gnb_home
                return
            }

            it as PlayerFragment
            when (it.playerMotionLayout.currentState) {
                R.id.expanded -> it.playerMotionLayout.transitionToStart()
                R.id.collapsed -> {
                    supportFragmentManager.beginTransaction()
                        .remove(fragment2)
                        .commit()
                }
                else -> super.onBackPressed()
            }
        }
    }


}