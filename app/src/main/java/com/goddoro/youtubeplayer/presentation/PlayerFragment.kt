package com.goddoro.youtubeplayer.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.goddoro.youtubeplayer.MainActivity
import com.goddoro.youtubeplayer.R
import com.goddoro.youtubeplayer.databinding.FragmentPlayerBinding
import com.goddoro.youtubeplayer.utils.debugE
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

class PlayerFragment : Fragment(){

    private val TAG = PlayerFragment::class.java.simpleName
    private lateinit var mBinding: FragmentPlayerBinding

    private lateinit var player : Player


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPlayerBinding.inflate(inflater, container,false).also { mBinding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.lifecycleOwner = viewLifecycleOwner
        setupTransition()
        initPlayer()

    }

    private fun initPlayer () {

        player = SimpleExoPlayer.Builder(requireContext()).build()

        mBinding.playerView.player = player

        player.repeatMode = Player.REPEAT_MODE_ALL

        val mediaItem =
            MediaItem.fromUri("https://cdn.onesongtwoshows.com/video/okt7ne01ywn_1602269794509.mp4")
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        mBinding.imgDownArrow.setOnClickListener {
            mBinding.playerMotionLayout.setTransition(R.id.to_expanded)
            mBinding.playerMotionLayout.transitionToStart()
        }

//        mBinding.videoContainer.setOnClickListener {
//            if (mBinding.playerMotionLayout.currentState == R.id.collapsed) {
//                mBinding.playerMotionLayout.setTransition(R.id.to_expanded)
//                mBinding.playerMotionLayout.transitionToEnd()
//            }
//        }
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

    override fun onDestroy() {
        super.onDestroy()

        player.release()
    }

    companion object {

        fun newInstance() = PlayerFragment()
    }
}