package reactivecircus.flowbinding.material.fixtures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import reactivecircus.flowbinding.material.fixtures.databinding.FragmentMaterial1Binding

class MaterialFragment1 : Fragment() {

    private lateinit var binding: FragmentMaterial1Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMaterial1Binding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.title = "Material Component Bindings"
    }
}
