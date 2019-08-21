package reactivecircus.flowbinding.material

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import reactivecircus.flowbinding.material.test.R

class MaterialFragment : Fragment(R.layout.fragment_material) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Toolbar>(R.id.toolbar).title = "Material Component Bindings"
    }
}
