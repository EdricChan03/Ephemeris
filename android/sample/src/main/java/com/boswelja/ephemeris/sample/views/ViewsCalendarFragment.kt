package com.boswelja.ephemeris.sample.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.boswelja.ephemeris.core.data.CalendarMonthPageSource
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.data.CalendarWeekPageSource
import com.boswelja.ephemeris.core.data.DisplayMonthFocusMode
import com.boswelja.ephemeris.core.data.WeekdayFocusMode
import com.boswelja.ephemeris.sample.R
import com.boswelja.ephemeris.sample.databinding.FragmentViewsCalendarBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.DayOfWeek

class ViewsCalendarFragment : Fragment(R.layout.fragment_views_calendar) {

    private val binding by viewBinding(FragmentViewsCalendarBinding::bind)
    private val vm by viewModels<CalendarViewVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchView.setOnClickListener {
            vm.toggleView()
        }

        binding.monthCalendar.initCalendar(
            pageSource = CalendarMonthPageSource(firstDayOfWeek = DayOfWeek.SUNDAY),
            focusMode = DisplayMonthFocusMode,
            dayBinder = CalendarDayBinder()
        )

        binding.weekCalendar.initCalendar(
            pageSource = CalendarWeekPageSource(DayOfWeek.SUNDAY),
            focusMode = WeekdayFocusMode,
            dayBinder = CalendarDayBinder()
        )

        observeCalendarState()
    }

    private fun observeCalendarState() {
        vm.toggle.onEach {
            val loader: CalendarPageSource
            val buttonText: String
            if (it) {
                loader = CalendarMonthPageSource(DayOfWeek.SUNDAY)
                buttonText = getString(R.string.toggle_view, getString(R.string.week))
            } else {
                loader = CalendarWeekPageSource(DayOfWeek.SUNDAY)
                buttonText = getString(R.string.toggle_view, getString(R.string.month))
            }
            binding.switchCalendar.initCalendar(
                pageSource = loader,
                focusMode = WeekdayFocusMode,
                dayBinder = CalendarDayBinder()
            )
            binding.switchView.text = buttonText
        }.launchIn(lifecycleScope)
    }
}
