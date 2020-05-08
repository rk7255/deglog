package jp.ryuk.deglog.ui.diarylist.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import jp.ryuk.deglog.R
import jp.ryuk.deglog.adapters.*
import jp.ryuk.deglog.data.Diary
import jp.ryuk.deglog.databinding.FragmentMemoBinding
import jp.ryuk.deglog.ui.diarylist.DiaryListFragmentDirections
import jp.ryuk.deglog.ui.diarylist.ListKey

class MemoFragment(
    private val selectedName: String,
    private val diaries: List<Diary>) : Fragment() {

    private lateinit var binding: FragmentMemoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_memo, container, false)
        binding.lifecycleOwner = this

        val recyclerView = binding.recyclerViewMemo
        val adapter = MemoAdapter(MemoListener { id -> onClick(id) })
        recyclerView.adapter = adapter

        adapter.submitList(diaries)
        val decoration = DiaryStickerDecoration(activity!!, diaries)
        recyclerView.addItemDecoration(decoration)

        return binding.root
    }

    private fun onClick(id: Long) {
        this.findNavController().navigate(
            DiaryListFragmentDirections
                .actionDiaryListFragmentToDiaryDetailFragment(id, selectedName))
    }
}
