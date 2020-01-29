package reactivecircus.flowbinding.android.fixtures.view

import android.content.ClipData
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import reactivecircus.flowbinding.android.fixtures.R
import reactivecircus.flowbinding.android.fixtures.databinding.FragmentAndroidViewBinding

class AndroidViewFragment : Fragment(R.layout.fragment_android_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAndroidViewBinding.bind(view)
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
