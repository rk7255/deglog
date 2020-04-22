package jp.ryuk.deglog.ui.diarydetail.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import jp.ryuk.deglog.R
import jp.ryuk.deglog.adapters.*
import jp.ryuk.deglog.data.DiaryRepository
import jp.ryuk.deglog.databinding.FragmentLengthBinding

class LengthFragment(private val selectedName: String) : Fragment() {

    private lateinit var binding: FragmentLengthBinding
    private lateinit var lengthViewModel: LengthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_length, container, false)
        lengthViewModel = createViewModel(selectedName)
        binding.viewModel = lengthViewModel
        binding.lifecycleOwner = this

        /**
         * RecyclerView
         */
        val recyclerView = binding.recyclerViewLength
        val adapter = LengthAdapter(LengthListener { /* click listener */ })
        recyclerView.adapter = adapter

        lengthViewModel.diaries.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                val decoration = DiaryStickerDecoration(activity!!, it)
                recyclerView.addItemDecoration(decoration)
            }
        })


        return binding.root
    }

    private fun createViewModel(selectedName: String): LengthViewModel {
        val application = requireNotNull(this.activity).application
        val dataSourceDiary = DiaryRepository.getInstance(application).diaryDao
        val viewModelFactory =
            LengthViewModelFactory(selectedName, dataSourceDiary)
        return ViewModelProvider(this, viewModelFactory).get(LengthViewModel::class.java)
    }

}
