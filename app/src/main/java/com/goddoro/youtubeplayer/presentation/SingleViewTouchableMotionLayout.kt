package com.goddoro.youtubeplayer.presentation


import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.goddoro.youtubeplayer.R

class SingleViewTouchableMotionLayout(context: Context, attributeSet: AttributeSet? = null) : MotionLayout(context, attributeSet) {

    private val viewToDetectTouch by lazy {
        findViewById<View>(R.id.video_container) //TODO move to Attributes
    }
    private val viewRect = Rect()
    private var touchStarted = false

    private var xCoordinate = 0f
    private var yCoordinate = 0f
    private var isTransitionProcessing = false
    private val transitionListenerList = mutableListOf<TransitionListener?>()

    init {
        addTransitionListener(object : TransitionListener {
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {

                touchStarted = false
                isTransitionProcessing = false
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                isTransitionProcessing = true
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }
        })

        super.setTransitionListener(object : TransitionListener {
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                transitionListenerList.filterNotNull()
                    .forEach { it.onTransitionChange(p0, p1, p2, p3) }
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                transitionListenerList.filterNotNull()
                    .forEach { it.onTransitionCompleted(p0, p1) }
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }
        })

    }

    override fun setTransitionListener(listener: TransitionListener?) {
        addTransitionListener(listener)
    }

    override fun addTransitionListener(listener: TransitionListener?) {
        transitionListenerList += listener
    }

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            transitionToEnd()
            return false
        }
    })

//    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
//
//        if ( event?.action == MotionEvent.ACTION_MOVE){
//            return super.onInterceptTouchEvent(event)
//        }
//        return false
//    }

    override fun onTouchEvent(event: MotionEvent): Boolean {


        when (event.actionMasked) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                Log.d("MainPlayerFragment","ACTION_UP $touchStarted")
                val xDistance = Math.abs(xCoordinate - event.x)
                val yDistance = Math.abs(yCoordinate - event.y)
                if (touchStarted && !isTransitionProcessing && xDistance < 5 && yDistance < 5) {
              
                    this.setTransition(R.id.to_expanded)
                    this.transitionToEnd()
                }
                touchStarted = false
                isTransitionProcessing = false
                return super.onTouchEvent(event)
            }
            MotionEvent.ACTION_DOWN -> {
                xCoordinate = event.x
                yCoordinate = event.y
            }
        }
        if (!touchStarted) {
            viewToDetectTouch.getHitRect(viewRect)
            touchStarted = viewRect.contains(event.x.toInt(), event.y.toInt())

        }

        return touchStarted && super.onTouchEvent(event)
    }

}