package sh.fearless.orb.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import sh.fearless.orb.R
import kotlin.math.abs

class TabPageTransformer(private val pageTranslationX: Float): ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        page.translationX = -pageTranslationX * position

        // gives the animation
         page.scaleY = 1 - (0.25f * abs(position))
        // If you want a fading effect uncomment the next line:
        // page.alpha = 0.25f + (1 - abs(position))
    }
}