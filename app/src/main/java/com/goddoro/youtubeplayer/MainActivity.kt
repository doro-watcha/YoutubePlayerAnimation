package com.goddoro.youtubeplayer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.goddoro.youtubeplayer.databinding.ActivityMainBinding
import com.goddoro.youtubeplayer.presentation.PlayerFragment
import com.goddoro.youtubeplayer.presentation.ProfileFragment
import com.goddoro.youtubeplayer.utils.debugE
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_player.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var mBinding : ActivityMainBinding

    private val fragment1 : MainFragment = MainFragment.newInstance()
    private val fragment2: PlayerFragment = PlayerFragment.newInstance()
    private val fragment3 : ProfileFragment = ProfileFragment.newInstance()
    private var willShow : Fragment = fragment1
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
            Log.d(TAG, "New Load")

            supportFragmentManager.beginTransaction()
                .add(R.id.playerContainer, fragment2, "2")
                .show(fragment2).commit()


        }
        else {
            debugE(TAG, "FUCK")
            (supportFragmentManager.findFragmentById(R.id.playerContainer)).also {
                it as PlayerFragment

                if (it.playerMotionLayout.currentState == R.id.collapsed) {
                    it.playerMotionLayout.setTransition(R.id.to_expanded)
                    it.playerMotionLayout.transitionToEnd()
                } else {

                }
            }

        }
    }

    private fun setupBottomNavigationView() {


        val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->

                if ( menuItem.itemId != R.id.gnb_video) changeFragment(menuItem.itemId)

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

    private fun changeFragment( menuId : Int ) {

        curMainMenu = menuId

        willShow = if (menuId == R.id.gnb_profile) {
            fragment3
        }
        else {
            fragment1
        }

        supportFragmentManager.beginTransaction()
            .hide(fragment1)
            .hide(fragment3)
            .show(willShow)
            .commit()
    }

    private fun initFragment() {

        supportFragmentManager.beginTransaction().add(R.id.container, fragment1,"1").show(fragment1).commit()
        supportFragmentManager.beginTransaction().add(R.id.container, fragment3,"3").hide(fragment3).commit()

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
                    it.playerMotionLayout.setTransition(R.id.to_removed)
                    it.playerMotionLayout.transitionToEnd()
                }
                else -> super.onBackPressed()
            }
        }
    }

    private fun removePlayerFragment() {
        supportFragmentManager.beginTransaction()
            .remove(fragment2)
            .commit()

    }


}