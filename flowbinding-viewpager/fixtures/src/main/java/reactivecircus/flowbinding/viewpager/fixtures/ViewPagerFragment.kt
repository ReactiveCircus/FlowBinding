@file:Suppress("DEPRECATION")

package reactivecircus.flowbinding.viewpager.fixtures

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import reactivecircus.flowbinding.viewpager.fixtures.databinding.FragmentPageItemBinding
import reactivecircus.flowbinding.viewpager.fixtures.databinding.FragmentViewpagerBinding

class ViewPagerFragment : Fragment(R.layout.fragment_viewpager) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentViewpagerBinding.bind(view)
        binding.viewPager.adapter = PagerAdapter(parentFragmentManager)
    }
}

private val pages = listOf("1", "2", "3")

class PagerAdapter(
    fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int = pages.size

    override fun getItem(position: Int): Fragment = PageItemFragment(
        pages[position]
    )
}

class PageItemFragment(private val pageTitle: String) : Fragment(R.layout.fragment_page_item) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPageItemBinding.bind(view)
        binding.pageTitleTextView.text = pageTitle
    }
}
