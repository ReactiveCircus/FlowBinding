package reactivecircus.flowbinding.viewpager2.fixtures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import reactivecircus.flowbinding.viewpager2.fixtures.databinding.FragmentViewpager2Binding
import reactivecircus.flowbinding.viewpager2.fixtures.databinding.ItemViewpagerBinding

class ViewPager2Fragment : Fragment() {

    private lateinit var binding: FragmentViewpager2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentViewpager2Binding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = ViewPagerAdapter()
    }
}

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        val binding = ItemViewpagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewPagerHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount(): Int {
        return pages.size
    }
}

private val pages = listOf("1", "2", "3")

class ViewPagerHolder(private val binding: ItemViewpagerBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(pageTitle: String) {
        binding.pageTitleTextView.text = pageTitle
    }
}
