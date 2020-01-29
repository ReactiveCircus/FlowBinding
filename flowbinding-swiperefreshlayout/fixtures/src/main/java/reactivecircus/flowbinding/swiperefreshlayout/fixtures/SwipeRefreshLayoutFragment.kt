package reactivecircus.flowbinding.swiperefreshlayout.fixtures

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import reactivecircus.flowbinding.swiperefreshlayout.fixtures.databinding.FragmentSwipeRefreshLayoutBinding

class SwipeRefreshLayoutFragment : Fragment(R.layout.fragment_swipe_refresh_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSwipeRefreshLayoutBinding.bind(view)

        @Suppress("MagicNumber")
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SimpleAdapter(20)
        }
    }
}
