package reactivecircus.flowbinding.android.fixtures.widget

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import reactivecircus.flowbinding.android.fixtures.R
import reactivecircus.flowbinding.android.fixtures.databinding.FragmentListBinding

class ListFragment : Fragment(R.layout.fragment_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentListBinding.bind(view)

        val values = arrayOf("One", "Two", "Three")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, values)
        binding.listView.adapter = adapter
    }
}
