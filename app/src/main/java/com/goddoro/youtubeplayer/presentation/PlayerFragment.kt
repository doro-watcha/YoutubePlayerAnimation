package com.goddoro.youtubeplayer.presentation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.goddoro.youtubeplayer.MainActivity
import com.goddoro.youtubeplayer.R
import com.goddoro.youtubeplayer.databinding.FragmentPlayerBinding
import com.goddoro.youtubeplayer.service.PlayerService
import com.goddoro.youtubeplayer.utils.debugE
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

class PlayerFragment : Fragment(){

    private val TAG = PlayerFragment::class.java.simpleName
    private lateinit var mBinding: FragmentPlayerBinding

    private lateinit var playerService: PlayerService
    private var mBound : Boolean = false

    var usePlayerBackground = false



    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as PlayerService.PlayerBinder

            debugE(TAG, "onServiceConnected")
            playerService = binder.getService()
            mBound = true
            initPlayer()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            debugE(TAG, "onServiceDisconnected")
            mBound = false
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPlayerBinding.inflate(inflater, container,false).also { mBinding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.lifecycleOwner = viewLifecycleOwner
        setupTransition()

    }

    private fun initPlayer () {

        debugE(TAG, "init Player")

        mBinding.playerView.player = playerService.player

        mBinding.imgDownArrow.setOnClickListener {
            mBinding.playerMotionLayout.setTransition(R.id.to_expanded)
            mBinding.playerMotionLayout.transitionToStart()
        }

        playerService.play()

    }

    private fun setupTransition() {

        mBinding.playerMotionLayout.apply {

            setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                    if (endId != R.id.removed) {
                        (activity as MainActivity).also {
                            it.mainMotionLayout.progress = abs(progress)
                        }
                    }
                    debugE(TAG, progress)
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

                    val progress: Float = (activity as MainActivity).mainMotionLayout.progress
                    if (progress != 0f && currentId == R.id.collapsed) {
                        (activity as MainActivity).mainMotionLayout.progress = 0f
                    }

                    if (currentId == R.id.removed) {
                        debugE(TAG, "REMOVE")
                        requireActivity().supportFragmentManager.beginTransaction()
                            .remove(this@PlayerFragment).commit()
                    }
                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                }

                override fun onTransitionTrigger(
                    p0: MotionLayout?,
                    p1: Int,
                    p2: Boolean,
                    p3: Float
                ) {

                    debugE(TAG, "Trigerred!")
                }
            })
            setTransition(R.id.to_expanded)
            transitionToEnd()
        }
    }

    override fun onStart() {
        super.onStart()

        debugE(TAG, "onStart")

        Intent(requireActivity(), PlayerService::class.java).also { intent ->
            requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()

        if (usePlayerBackground) {
            playerService.pause()
        }

    }



    override fun onDestroy() {
        super.onDestroy()

    }

    companion object {

        fun newInstance() = PlayerFragment()
    }
}