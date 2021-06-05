package app.spidy.orb.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import app.spidy.orb.R

class TabPageTransformer(private val pageTranslationX: Float): ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        page.translationX = -pageTranslationX * position
        page.scaleY = 0.8f

        // gives the animation
        // page.scaleY = 1 - (0.25f * abs(position))
        // If you want a fading effect uncomment the next line:
        // page.alpha = 0.25f + (1 - abs(position))
    }
}