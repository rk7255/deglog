package jp.ryuk.deglog.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.ryuk.deglog.database.Diary
import jp.ryuk.deglog.databinding.ItemDetailListBinding


class DiaryListAdapter(
    private val unitWeight: String,
    private val unitLength: String,
    private val clickListener: DiaryListListener
) : ListAdapter<Diary, DiaryListAdapter.ViewHolder>(DiaryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item, unitWeight, unitLength, clickListener)
    }

    class ViewHolder private constructor(
        private val binding: ItemDetailListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Diary, unitWeight: String, unitLength: String, clickListener: DiaryListListener) {
            with(binding) {
                diary = item
                this.unitWeight = unitWeight
                this.unitLength = unitLength
                this.clickListener = clickListener
                hasMemo = !item.note.isNullOrEmpty()
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemDetailListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class DiaryDiffCallback : DiffUtil.ItemCallback<Diary>() {
    override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
        return oldItem == newItem
    }
}

class DiaryListListener(val clickListener: (id: Long) -> Unit) {
    fun onClick(id: Long) = clickListener(id)
}