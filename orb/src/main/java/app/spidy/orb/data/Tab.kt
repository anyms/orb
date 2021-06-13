package app.spidy.orb.data

import app.spidy.orb.fragments.OrbWebViewFragment

data class Tab(
    var title: String,
    val fragment: OrbWebViewFragment,
    var url: String = "",
    ) {
    val tabId: Long = kotlin.random.Random.nextLong()
}
