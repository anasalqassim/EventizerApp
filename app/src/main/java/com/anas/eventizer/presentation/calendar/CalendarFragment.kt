package com.anas.eventizer.presentation.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import coil.load
import com.anas.eventizer.R
import com.anas.eventizer.databinding.CalendarDayLayoutBinding
import com.anas.eventizer.databinding.FragmentCalendarBinding
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.models.eventDateIdPair
import com.anas.eventizer.utils.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.ScrollMode
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

private const val TAG = "CalendarFragment"
@AndroidEntryPoint
class CalendarFragment : Fragment(),CalendarSelectedListener {

    private var personalEvents: Map<LocalDate, List<PersonalEvent>> = emptyMap()
    private val viewModel: CalendarViewModel by viewModels()
    private lateinit var _binding:FragmentCalendarBinding
    private val binding get() = _binding
    private lateinit var eventId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //findNavController().navigate(R.id.qrCodeFragment)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        viewModel.initPersonalEvent(Firebase.auth.currentUser?.uid!!)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton2.setOnClickListener {
            findNavController().navigate(R.id.addPersonalEventFragment)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.personalEventsStateFlow.collectLatest {
                if (it is Resource.Success) {

                     personalEvents = it.data!!.groupBy { it.eventDate.toLocalDate() }


                    val currentMonth = YearMonth.now()
                    val firstMonth = currentMonth.minusMonths(10)
                    val lastMonth = currentMonth.plusMonths(10)
                    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
                    binding.calendarView.setup(YearMonth.of(currentMonth.year,1), YearMonth.of(currentMonth.year,12), firstDayOfWeek)
                    binding.calendarView.scrollMode = ScrollMode.PAGED
                    binding.calendarView.monthHeaderResource = R.layout.calendar_day_layout
                    binding.calendarView.scrollToMonth(currentMonth)


                    binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {

                        // Called only when a new container is needed.
                        override fun create(view: View):DayViewContainer {
                            val binding = CalendarDayLayoutBinding.bind(view)

                           return DayViewContainer(binding,this@CalendarFragment)
                        }

                        // Called every time we need to reuse a container.
                        override fun bind(container: DayViewContainer, day: CalendarDay) {

                            container.bind(day)
                            container.binding.calendarDayText.text = day.date.dayOfMonth.toString()
                            if (day.owner == DayOwner.THIS_MONTH){
                                val personalEvents = personalEvents[day.date]
                                Log.d(TAG, "bind: ${day.date}")
                                if (personalEvents != null){
                                    
                                    container.binding.reservedView.visibility = View.VISIBLE
                                }

                                }

                            }

                        }


                    binding.calendarView.monthHeaderBinder =
                        object : MonthHeaderFooterBinder<DayViewContainer> {
                            override fun bind(container: DayViewContainer, month: CalendarMonth) {
                                container.binding.calendarDayText.text = month.month.toString()

                            }

                            override fun create(view: View): DayViewContainer =
                                DayViewContainer( CalendarDayLayoutBinding.bind(view),this@CalendarFragment)

                        }

                }
            }
        }



        binding.eventCard.setOnClickListener {
            val action  = CalendarFragmentDirections.actionCalendarFragmentToEventDetailsFragment(eventId)
            findNavController().navigate(action)
                
        }

    }

    override fun onDateSelected(day: LocalDate) {
        val personalEvent = personalEvents[day]?.first()

        if (personalEvent != null){
            eventId = personalEvent.id
            binding.eventCard.visibility = View.VISIBLE
            binding.eventDate.text = day.toString()
            binding.eventTitle.text = personalEvent.eventName.ifEmpty { "No Title" }
            binding.eventImg.load(personalEvent.eventPicsUrls[0])
        }else{
            binding.eventCard.visibility = View.GONE
        }
    }


}

fun Date.toLocalDate():LocalDate
{
    val dateCalender = Calendar.Builder().setInstant(this).build()

    return LocalDate.of(dateCalender[Calendar.YEAR],dateCalender[Calendar.MONTH],dateCalender[Calendar.DAY_OF_MONTH])
}