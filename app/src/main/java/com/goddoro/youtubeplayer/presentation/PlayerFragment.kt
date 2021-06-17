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
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

class PlayerFragment : Fragment(){

    private lateinit var mBinding : FragmentPlayerBinding

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

        val mediaItem =
            MediaItem.fromUri("https://cdn.onesongtwoshows.com/video/okt7ne01ywn_1602269794509.mp4")
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    private fun setupTransition() {

        mBinding.playerMotionLayout.apply {

            setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                    (activity as MainActivity).also {
                        it.mainMotionLayout.progress = abs(progress)
                    }
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    if ( mBinding.playerMotionLayout.currentState == R.id.expanded ) motionLayout?.setTransition(R.id.clickArrow)
                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                }

                override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                }
            })
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