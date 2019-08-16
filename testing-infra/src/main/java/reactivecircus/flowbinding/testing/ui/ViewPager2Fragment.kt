package reactivecircus.flowbinding.testing.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_viewpager2.*
import kotlinx.android.synthetic.main.item_viewpager.view.*
import reactivecircus.flowbinding.testing.R

class ViewPager2Fragment : Fragment(R.layout.fragment_viewpager2) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = ViewPagerAdapter()
    }
}

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        return ViewPagerHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_viewpager,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount(): Int {
        return pages.size
    }
}

private val pages = listOf("1", "2", "3")

class ViewPagerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(pageTitle: String) {
        itemView.pageTitleTextView.text = pageTitle
    }
}
