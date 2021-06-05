package app.spidy.orb.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class ResetPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.translationX = 1f
        page.scaleY = 1f
    }
}