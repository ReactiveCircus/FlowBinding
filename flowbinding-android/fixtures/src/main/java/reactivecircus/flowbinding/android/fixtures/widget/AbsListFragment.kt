package reactivecircus.flowbinding.android.fixtures.widget

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import reactivecircus.flowbinding.android.fixtures.R
import reactivecircus.flowbinding.android.fixtures.databinding.FragmentAbsListBinding

class AbsListFragment : Fragment(R.layout.fragment_abs_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAbsListBinding.bind(view)

        @Suppress("MagicNumber")
        val values = IntArray(50) { it + 1 }.toTypedArray()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, values)
        binding.absListView.adapter = adapter
    }
}
