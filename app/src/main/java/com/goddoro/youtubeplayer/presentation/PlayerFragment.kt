package com.goddoro.youtubeplayer.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.goddoro.youtubeplayer.MainActivity
import com.goddoro.youtubeplayer.databinding.FragmentPlayerBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

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
        setupTransition()

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
                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                }

                override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                }
            })
            transitionToEnd()
        }
    }

    companion object {

        fun newInstance() = PlayerFragment()
    }
}