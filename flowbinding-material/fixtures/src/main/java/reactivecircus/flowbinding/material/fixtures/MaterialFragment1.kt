package reactivecircus.flowbinding.material.fixtures

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import reactivecircus.flowbinding.material.fixtures.databinding.FragmentMaterial1Binding

class MaterialFragment1 : Fragment(R.layout.fragment_material_1) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMaterial1Binding.bind(view)
        binding.toolbar.title = "Material Component Bindings"
    }
}
