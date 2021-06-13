package app.spidy.orb.data

import app.spidy.orb.fragments.OrbWebViewFragment

data class Tab(
    var title: String,
    val fragmentOrb: OrbWebViewFragment,
    var url: String = "",
    ) {
    val tabId: Long = kotlin.random.Random.nextLong()
}
