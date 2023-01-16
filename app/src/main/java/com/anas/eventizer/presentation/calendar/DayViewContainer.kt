package com.anas.eventizer.presentation.calendar

import android.view.View
import com.anas.eventizer.databinding.CalendarDayLayoutBinding
import com.anas.eventizer.domain.models.PersonalEvent
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.LocalDate

class DayViewContainer(val binding: CalendarDayLayoutBinding,fragment: CalendarFragment) : ViewContainer(binding.root) {
    lateinit var day: CalendarDay
    private val selectedDate:LocalDate? = null

    init {
        view.setOnClickListener {
            if (day.owner == DayOwner.THIS_MONTH) {
                if (selectedDate != day.date){

                    (fragment as CalendarSelectedListener).onDateSelected(day.date)

                }
            }
        }
    }

    fun bind(day: CalendarDay){
        this.day = day
        if (day.owner == DayOwner.THIS_MONTH) {

        }
    }


}