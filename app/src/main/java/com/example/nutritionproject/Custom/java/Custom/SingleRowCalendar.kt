package com.example.nutritionproject.Custom.java.Custom

import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.nutritionproject.Custom.java.Utility.Event
import com.example.nutritionproject.Custom.java.Utility.EventCallback
import com.example.nutritionproject.Custom.java.Utility.EventContext
import com.example.nutritionproject.R
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendar
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class SingleRowCalendar {
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var lastDateRecorded = ""

    constructor(singleRowCalendar : SingleRowCalendar,
                monthLabel : TextView,
                yearLabel : TextView,
                leftBtn : Button,
                rightBtn : Button,
                callback: EventCallback?) {
        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]

        val myCalendarViewManager = object : CalendarViewManager {
            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                holder.itemView.findViewById<TextView>(R.id.dayCalendarItem).text = DateUtils.getDayNumber(date)
                holder.itemView.findViewById<TextView>(R.id.dateCalendarItem).text = DateUtils.getDay3LettersName(date)
            }

            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                val cal = Calendar.getInstance()
                cal.time = date

                return if (isSelected) {
                    R.layout.calender_item_selected_view
                } else {
                    R.layout.calender_item_view
                }
            }
        }

        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                val cal = Calendar.getInstance()
                cal.time = date

                if (lastDateRecorded == date.toString())
                {
                    return true;
                }

                lastDateRecorded = date.toString()

                callback?.onSuccess(EventContext.Builder().withData(date).build())

                return true
            }
        }

        val myCalendarChangesObserver = object : CalendarChangesObserver {
            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                monthLabel.text = "${DateUtils.getMonthName(date)} "
                yearLabel.text = DateUtils.getYear(date)

                super.whenSelectionChanged(isSelected, position, date)
            }

            override fun whenWeekMonthYearChanged(
                weekNumber: String,
                monthNumber: String,
                monthName: String,
                year: String,
                date: Date
            ) {
                monthLabel.text = "${DateUtils.getMonthName(date)} "
                yearLabel.text = DateUtils.getYear(date)

                super.whenWeekMonthYearChanged(weekNumber, monthNumber, monthName, year, date)
            }
        }

        val singleRowCalendar = singleRowCalendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            setDates(getFutureDatesOfCurrentMonth())
            init()

            val date = Calendar.getInstance().get(Calendar.DATE)

            select(date - 1)
            smoothScrollToPosition(date - 1)
        }

        rightBtn.setOnClickListener {
            singleRowCalendar.setDates(getDatesOfNextMonth())
            singleRowCalendar.init()
        }

        leftBtn.setOnClickListener {
            singleRowCalendar.setDates(getDatesOfPreviousMonth())
            singleRowCalendar.init()
        }

    }

    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++
        if (currentMonth == 12) {
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth--
        if (currentMonth == -1) {
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11
        }
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }
}