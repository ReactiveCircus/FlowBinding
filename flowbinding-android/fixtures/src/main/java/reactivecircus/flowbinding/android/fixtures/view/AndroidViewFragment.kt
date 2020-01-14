package reactivecircus.flowbinding.android.fixtures.view

import android.content.ClipData
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import reactivecircus.flowbinding.android.fixtures.databinding.FragmentAndroidViewBinding

class AndroidViewFragment : Fragment() {

    private var _binding: FragmentAndroidViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAndroidViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.draggableView.setOnLongClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.draggableView.startDragAndDrop(
                    ClipData.newPlainText("", ""),
                    View.DragShadowBuilder(binding.draggableView),
                    true,
                    0
                )
            } else {
                @Suppress("deprecation")
                binding.draggableView.startDrag(
                    ClipData.newPlainText("", ""),
                    View.DragShadowBuilder(binding.draggableView),
                    true,
                    0
                )
            }
            true
        }
    }
}
