package reactivecircus.flowbinding.android.fixtures.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import reactivecircus.flowbinding.android.fixtures.R
import reactivecircus.flowbinding.android.fixtures.databinding.FragmentAndroidWidgetBinding

class AndroidWidgetFragment : Fragment(R.layout.fragment_android_widget) {

    private lateinit var binding: FragmentAndroidWidgetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentAndroidWidgetBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val values = arrayOf("One", "Two", "Three")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, values)
        binding.listView.adapter = adapter
    }
}
