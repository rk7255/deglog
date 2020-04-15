package jp.ryuk.deglog.ui.reminder

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity

import jp.ryuk.deglog.R
import kotlinx.android.synthetic.main.fragment_chart.view.*
import kotlinx.android.synthetic.main.fragment_reminder.view.*

/**
 * A simple [Fragment] subclass.
 */
class ReminderFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reminder, container, false)

        (activity as AppCompatActivity).setSupportActionBar(view.app_bar_reminder)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

}
