package app.spidy.orbexample.activities

import android.os.Bundle
import sh.fearless.orb.OrbActivity
import app.spidy.orbexample.R
import app.spidy.orbexample.databinding.ActivityMainBinding

class MainActivity : OrbActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.browserContainer, getOrbFragment(), "OrbBrowserFragment")
                .commit()
        } else {
            restoreOrbFragment()
        }
    }
}