package com.anas.eventizer.presentation.calendar

import com.kizitonwose.calendarview.model.CalendarDay
import java.time.LocalDate

interface CalendarSelectedListener {

    fun onDateSelected(day: LocalDate)
}