package ua.antibyte.animationtext

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper

class ConveyorTextView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defAttrStyle: Int = 0
) : FrameLayout(context, attrs, defStyle, defAttrStyle) {
    companion object {
        const val DELAY_DURATION: Long = 500
    }

    private lateinit var textContainer: ViewFlipper
    private lateinit var imageContainer: ViewFlipper

    private lateinit var textView: TextView
    private lateinit var imageView: ImageView

    private val words = ArrayList<String>()

    private var text: String = ""
    private var imageResId: Int = -1
    private var currentWordIndex = 0

    init {
        inflate(context, R.layout.view_conveyor_text, this)
        initViews()
        initAttrs()
    }

    fun setText(text: String) {
        currentWordIndex = 0

        words.clear()
        words.addAll(text.split(" "))

        textView.text = words[currentWordIndex]
    }

    fun setImage(resId: Int) {
        this.imageResId = resId
    }

    fun start() {
        clearAllAnimation()
        textContainer.visibility = View.VISIBLE
        imageContainer.visibility = View.GONE

        startTextAnimation()
    }

    private fun startTextAnimation() {
        getObjectAnimatorForView(textView, R.animator.pop_up_from_below_animation).apply {
            addListener(createTextPopUpFromBelowAnimationListener())
            start()
        }
    }

    private fun startImageAnimation() {
        getObjectAnimatorForView(imageView, R.animator.pop_up_from_below_animation).apply {
            addListener(createImagePopUpFromBelowAnimationListener())
            start()
        }
    }

    private fun onImageAnimationEnd() {
        clearAllAnimation()
    }

    private fun clearAllAnimation() {
        textContainer.clearAnimation()
        imageContainer.clearAnimation()

        textView.clearAnimation()
        imageView.clearAnimation()
    }

    private fun onTextAnimationEnd() {
        textView.clearAnimation()
        currentWordIndex++

        if (currentWordIndex < words.size) {
            textView.text = words[currentWordIndex]
            startTextAnimation()
        } else if (imageResId != -1) {
            textContainer.visibility = View.GONE
            imageContainer.visibility = View.VISIBLE
            imageView.setImageResource(imageResId)
            startImageAnimation()
        }
    }

    private fun getObjectAnimatorForView(view: View, resId: Int) =
        (AnimatorInflater.loadAnimator(context, resId) as ObjectAnimator).apply {
            target = view
        }

    private fun initViews() {
        textContainer = this.findViewById(R.id.view_conveyor_text_container)
        imageContainer = this.findViewById(R.id.view_conveyor_image_container)

        textView = this.findViewById(R.id.view_conveyor_text_view)
        imageView = this.findViewById(R.id.view_conveyor_image_view)
    }

    private fun initAttrs() {
        val typedArray =
            context.theme?.obtainStyledAttributes(attrs, R.styleable.ConveyorTextView, 0, 0)
        try {
            text = typedArray?.getString(R.styleable.ConveyorTextView_text) ?: ""
            imageResId = typedArray?.getResourceId(R.styleable.ConveyorTextView_src, -1) ?: -1
            setText(text)
        } finally {
            typedArray?.recycle()
        }
    }

    // listeners text

    private fun createTextPopUpFromBelowAnimationListener() = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            getObjectAnimatorForView(textView, R.animator.slide_up_animation).apply {
                addListener(createTextSlideUpAnimation())
                startDelay = DELAY_DURATION
                start()
            }
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }
    }

    private fun createTextSlideUpAnimation() = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            onTextAnimationEnd()
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }
    }

    // listeners image

    private fun createImagePopUpFromBelowAnimationListener() = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            getObjectAnimatorForView(imageContainer, R.animator.inc_scale_animation).apply {
                addListener(createImageIncScaleAnimationListener())
                startDelay = DELAY_DURATION
                start()
            }
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }
    }

    private fun createImageIncScaleAnimationListener() = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            getObjectAnimatorForView(imageContainer, R.animator.decrease_scale_animation).apply {
                addListener(createImageDecreaseScaleAnimationListener())
                start()
            }
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }
    }

    private fun createImageDecreaseScaleAnimationListener() = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            getObjectAnimatorForView(imageView, R.animator.slide_up_animation).apply {
                addListener(createImageSlideUpAnimation())
                startDelay = DELAY_DURATION
                start()
            }
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }
    }

    private fun createImageSlideUpAnimation() = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            onImageAnimationEnd()
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }
    }
}