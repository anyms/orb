package sh.fearless.orb.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import sh.fearless.orb.data.Tab

class ViewPageAdapter(
    private val tabs: ArrayList<Tab>, childFragmentManager: FragmentManager, lifecycle: Lifecycle
): FragmentStateAdapter(childFragmentManager, lifecycle) {
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
        notifyDataSetChanged()
    }
}