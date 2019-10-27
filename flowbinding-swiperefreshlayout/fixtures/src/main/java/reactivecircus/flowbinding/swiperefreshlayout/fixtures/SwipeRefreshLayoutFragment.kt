package reactivecircus.flowbinding.swiperefreshlayout.fixtures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import reactivecircus.flowbinding.swiperefreshlayout.fixtures.databinding.FragmentSwipeRefreshLayoutBinding

class SwipeRefreshLayoutFragment : Fragment() {

    private lateinit var binding: FragmentSwipeRefreshLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSwipeRefreshLayoutBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("MagicNumber")
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SimpleAdapter(20)
        }
    }
}
