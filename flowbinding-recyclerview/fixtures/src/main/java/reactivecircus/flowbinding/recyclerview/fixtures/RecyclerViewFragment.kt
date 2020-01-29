package reactivecircus.flowbinding.recyclerview.fixtures

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import reactivecircus.flowbinding.recyclerview.fixtures.databinding.FragmentRecyclerViewBinding

class RecyclerViewFragment : Fragment(R.layout.fragment_recycler_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRecyclerViewBinding.bind(view)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
