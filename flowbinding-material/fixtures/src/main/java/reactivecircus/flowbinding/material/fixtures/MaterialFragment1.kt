package reactivecircus.flowbinding.material.fixtures

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

class MaterialFragment1 : Fragment(R.layout.fragment_material_1) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Toolbar>(R.id.toolbar).title = "Material Component Bindings"
    }
}
