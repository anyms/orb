package sh.fearless.orb.data

import sh.fearless.orb.fragments.OrbWebViewFragment

data class Tab(
    var title: String,
    val fragment: OrbWebViewFragment,
    var url: String = "",
    ) {
    val tabId: Long = kotlin.random.Random.nextLong()
}
