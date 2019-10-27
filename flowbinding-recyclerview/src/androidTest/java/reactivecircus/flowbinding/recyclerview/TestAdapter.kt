package reactivecircus.flowbinding.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

internal class TestAdapter : RecyclerView.Adapter<TestViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            android.R.layout.simple_list_item_1, parent, false
        ) as TextView
        return TestViewHolder(view)
    }

    override fun getItemCount(): Int = 100

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.textView.text = position.toString()
    }
}

internal class TestViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
