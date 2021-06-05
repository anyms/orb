package app.spidy.orbexample.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.spidy.orb.fragments.BrowserFragment
import app.spidy.orbexample.R
import app.spidy.orbexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val browserFragment = BrowserFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.browserContainer, browserFragment)
            .commit()
    }
}