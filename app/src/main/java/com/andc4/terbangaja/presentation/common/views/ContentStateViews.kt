package com.andc4.terbangaja.presentation.common.views
//
// import android.content.Context
// import android.util.AttributeSet
// import androidx.annotation.RawRes
// import androidx.constraintlayout.widget.ConstraintLayout
// import androidx.core.view.isVisible
// import com.airbnb.lottie.LottieAnimationView
// import com.airbnb.lottie.LottieCompositionFactory
// import com.andc4.terbangaja.R
// import com.andc4.terbangaja.databinding.LayoutContentStateBinding
// import java.io.InputStream
//
// class ContentStateView
//    @JvmOverloads
//    constructor(
//        context: Context,
//        attrs: AttributeSet? = null,
//        defStyleAttr: Int = 0,
//    ) : ConstraintLayout(context, attrs, defStyleAttr) {
//        private var binding: LayoutContentStateBinding
//
//        private var listener: ContentStateListener? = null
//
//        init {
//            inflate(context, R.layout.layout_content_state, this)
//            binding = LayoutContentStateBinding.bind(this)
//        }
//
//        fun setListener(listener: ContentStateListener) {
//            this.listener = listener
//        }
//
//        fun setState(
//            state: ContentState,
//            message: String? = null,
//            @RawRes rawRes: Int? = null,
//        ) {
//            when (state) {
//                ContentState.SUCCESS -> {
//                    binding.root.isVisible = false
//                    binding.ivLoading.isVisible = false
//                    binding.ivContentState.isVisible = false
//                    listener?.onContentShow(true)
//                }
//                ContentState.LOADING -> {
//                    binding.root.isVisible = true
//                    binding.ivLoading.isVisible = true
//                    binding.ivContentState.isVisible = false
//                    loadAnimation(binding.ivLoading, R.raw.loading)
//                    listener?.onContentShow(false)
//                }
//                ContentState.EMPTY -> {
//                    binding.root.isVisible = true
//                    binding.ivLoading.isVisible = false
//                    binding.ivContentState.isVisible = true
//                    loadAnimation(binding.ivContentState, rawRes ?: R.raw.empty)
//                    listener?.onContentShow(false)
//                }
//                ContentState.ERROR_NETWORK -> {
//                    binding.root.isVisible = true
//                    binding.ivLoading.isVisible = false
//                    binding.ivContentState.isVisible = true
//                    loadAnimation(binding.ivContentState, rawRes ?: R.raw.network_error)
//                    listener?.onContentShow(false)
//                }
//                ContentState.ERROR_GENERAL -> {
//                    binding.root.isVisible = true
//                    binding.ivLoading.isVisible = false
//                    binding.ivContentState.isVisible = true
//                    loadAnimation(binding.ivContentState, rawRes ?: R.raw.error)
//                    listener?.onContentShow(false)
//                }
//            }
//        }
//
//        private fun loadAnimation(
//            view: LottieAnimationView,
//            @RawRes animationRes: Int,
//        ) {
//            try {
//                val inputStream: InputStream = resources.openRawResource(animationRes)
//                LottieCompositionFactory.fromJsonInputStream(inputStream, null).addListener { composition ->
//                    view.setComposition(composition)
//                    view.playAnimation()
//                }.addFailureListener { exception ->
//                    exception.printStackTrace()
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
// interface ContentStateListener {
//    fun onContentShow(isContentShow: Boolean)
// }
//
// enum class ContentState {
//    SUCCESS,
//    LOADING,
//    EMPTY,
//    ERROR_NETWORK,
//    ERROR_GENERAL,
// }
