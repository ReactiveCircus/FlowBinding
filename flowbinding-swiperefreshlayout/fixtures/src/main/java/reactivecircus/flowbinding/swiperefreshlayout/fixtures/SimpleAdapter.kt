package reactivecircus.flowbinding.swiperefreshlayout.fixtures

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

internal class SimpleAdapter(private val size: Int) : RecyclerView.Adapter<SimpleViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            android.R.layout.simple_list_item_1, parent, false
        ) as TextView
        return SimpleViewHolder(view)
    }

    override fun getItemCount(): Int = size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        holder.textView.text = position.toString()
    }
}

internal class SimpleViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
