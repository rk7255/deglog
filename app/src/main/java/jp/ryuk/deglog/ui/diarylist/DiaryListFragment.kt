package jp.ryuk.deglog.ui.diarylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import jp.ryuk.deglog.R
import jp.ryuk.deglog.adapters.DiaryListPagerAdapter
import jp.ryuk.deglog.adapters.LENGTH_PAGE_INDEX
import jp.ryuk.deglog.adapters.MEMO_PAGE_INDEX
import jp.ryuk.deglog.adapters.WEIGHT_PAGE_INDEX
import jp.ryuk.deglog.data.DiaryRepository
import jp.ryuk.deglog.data.ProfileRepository
import jp.ryuk.deglog.databinding.FragmentDiaryListBinding


class DiaryListFragment : Fragment() {

    private lateinit var binding: FragmentDiaryListBinding
    private lateinit var diaryListViewModel: DiaryListViewModel
    private lateinit var args: DiaryListFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_diary_list, container, false)

        args = DiaryListFragmentArgs.fromBundle(arguments!!)
        diaryListViewModel = createViewModel(args.selectedName)

        var fromKey = args.fromKey
        when (fromKey) {
            ListKey.FROM_DETAIL_WEIGHT -> {
                makeSnackBar(getString(R.string.delete_success))
                fromKey = ListKey.FROM_WEIGHT
            }
            ListKey.FROM_DETAIL_LENGTH -> {
                makeSnackBar(getString(R.string.delete_success))
                fromKey = ListKey.FROM_LENGTH
            }
            ListKey.FROM_EDIT_WEIGHT -> {
                makeSnackBar(getString(R.string.save_success))
                fromKey = ListKey.FROM_WEIGHT
            }
            ListKey.FROM_EDIT_LENGTH -> {
                makeSnackBar(getString(R.string.save_success))
                fromKey = ListKey.FROM_LENGTH
            }
        }
        binding.appBarDiaryList.title = args.selectedName + getString(R.string.title_diary_detail_at_name)

        diaryListViewModel.diaries.observe(viewLifecycleOwner, Observer {
            it?.let {
                val tabLayout = binding.diaryListTab
                val viewPager = binding.diaryListViewPager

                viewPager.adapter = DiaryListPagerAdapter(this, args.selectedName , it)
                viewPager.setCurrentItem(fromKey, false)
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    tab.text = getTabTitle(position)
                }.attach()
            }
        })


        return binding.root
    }

    private fun makeSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            WEIGHT_PAGE_INDEX -> getString(R.string.title_weight)
            LENGTH_PAGE_INDEX -> getString(R.string.title_length)
            MEMO_PAGE_INDEX -> getString(R.string.title_memo)
            else -> null
        }
    }

    private fun createViewModel(selectedName: String): DiaryListViewModel {
        val application = requireNotNull(this.activity).application
        val dataSourceDiary = DiaryRepository.getInstance(application).diaryDao
        val dataSourceProfile = ProfileRepository.getInstance(application).profileDao
        val viewModelFactory = DiaryListViewModelFactory(selectedName, dataSourceDiary, dataSourceProfile)
        return ViewModelProvider(this, viewModelFactory).get(DiaryListViewModel::class.java)
    }
}
