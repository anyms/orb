package app.spidy.orb.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.spidy.orb.data.Tab

class ViewPageAdapter(private val tabs: ArrayList<Tab>, activity: FragmentActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment {
        return tabs[position].fragment
    }

    override fun containsItem(itemId: Long): Boolean {
        return tabs.map { it.tabId }.contains(itemId)
    }

    override fun getItemId(position: Int): Long {
        return tabs[position].tabId
    }

    fun removeItem(index: Int) {
        tabs.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, tabs.size)
    }
}