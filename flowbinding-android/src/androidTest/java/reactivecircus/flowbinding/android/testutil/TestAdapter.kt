package reactivecircus.flowbinding.android.testutil

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class TestAdapter : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? = null

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = 0
}
