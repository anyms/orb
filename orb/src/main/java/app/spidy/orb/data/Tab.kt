package app.spidy.orb.data

import androidx.fragment.app.Fragment

data class Tab(
    var title: String,
    val fragment: Fragment
) {
    val tabId: Long = kotlin.random.Random.nextLong()
}
