package com.example.pkl_v1.ui.dashboard

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.example.pkl_v1.R
import com.example.pkl_v1.databinding.FragmentDashboardBinding
import com.example.pkl_v1.model.Score
import com.example.pkl_v1.service.AlarmBoardcasReceiver
import com.example.pkl_v1.util.SharedPref
import com.example.pkl_v1.viewmodel.DashboardViewModel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.afollestad.materialdialogs.datetime.datePicker
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.pkl_v1.databinding.Example7CalendarDayBinding
import com.example.pkl_v1.model.ModelActivity
import com.example.pkl_v1.model.ModelSchedule
import com.example.pkl_v1.util.LoadingHelper
import com.example.pkl_v1.viewmodel.NotifViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.Size
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    var dateNow: String
    private var scoreList = ArrayList<Score>()

    private var selectedDate = LocalDate.now()

    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    private val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    private val dayFormat = DateTimeFormatter.ofPattern("ddMMyyyy")

    private val monthFormatter = DateTimeFormatter.ofPattern("MMM")
    private val mDashboardViewModel by lazy {
        ViewModelProvider(this).get(DashboardViewModel::class.java)
    }
    private val mNotifViewModel by lazy {
        ViewModelProvider(this).get(NotifViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    init {
        val dates = SimpleDateFormat("ddMMyyyy")
        val DateNow = dates.format(Date())
        dateNow = DateNow
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniData()
        updateDiagram(dateNow)
        setUpCalendar()
//        mDashboardViewModel.uploadSensor(ModelActivity(0,7,10,3,4))
        binding.imageView2.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_questionnaireFragment)
        }
    }

    fun updateDiagram(dateNow: String) {
        mDashboardViewModel.getSensor(dateNow).observe(viewLifecycleOwner, {
            scoreList.clear()
            scoreList.add(Score("Duduk (Jam)", it.sit))
            scoreList.add(Score("Berdiri (Jam)", it.stand))
            scoreList.add(Score("Berbaring (Jam)", it.walk))
            scoreList.add(Score("Berjalan (Jam)", it.run))
            val barChart = binding.chart1
            barChart.apply {
                axisLeft.setDrawGridLines(false)
                xAxis.setDrawGridLines(false)
                xAxis.setDrawAxisLine(false)
                axisRight.isEnabled = false
                legend.isEnabled = false
                description.isEnabled = false
                animateY(300)
                xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
                xAxis.valueFormatter = MyAxisFormatter()
                xAxis.setDrawLabels(true)
                xAxis.granularity = 1f
                xAxis.labelRotationAngle = +90f
                val entries: ArrayList<BarEntry> = ArrayList()
                for (i in scoreList.indices) {
                    val score = scoreList[i]
                    entries.add(BarEntry(i.toFloat(), score.score.toFloat()))
                }
                val barDataSet = BarDataSet(entries, "")
                barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
                val data = BarData(barDataSet)
                barChart.data = data
                invalidate()
            }
        })
    }


    inner class MyAxisFormatter : IndexAxisValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < scoreList.size) {
                scoreList[index].name
            } else {
                ""
            }
        }
    }


    fun iniData() {
        val loading = LoadingHelper(requireContext())
        loading.show()
        YoYo.with(Techniques.Bounce).duration(5000).repeat(100).delay(500).playOn(binding.cardView5)

        mDashboardViewModel.getProfile().observe(viewLifecycleOwner, {
            if (it.idPasien.isEmpty()) {
            } else {
                loading.dismiss()

                binding.IDDashboardNamaPasien.text = "${it.namaPaien}"
                binding.IDDashboardTxtNamaPesan.text = "Hai ${it.namaPaien}"
                Picasso.get().load(it.fotoPasien).into(binding.IDDashboardImgView)
            }
        })

        val mypref = SharedPref(requireContext())
        if (mypref.getAlarmStatus()) {
            MaterialAlertDialogBuilder(requireContext()).setCancelable(false)
                .setMessage("Silahkan Mengisi Kuisioner")
                .setNegativeButton("Tidak") { _, _ ->
                }
                .setPositiveButton("Isi") { dialog, which ->
                    findNavController().navigate(R.id.action_dashboardFragment_to_questionnaireFragment)
                }.show()
        }

        mNotifViewModel.readAllQuestion.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()){

            val data =  it.sortedBy {
                it.date
            }.last()
            binding.IDDashboardTxtPesan.text = data.message
            }else{
                binding.IDDashboardTxtPesan.text = "Tidak ada Pesan"

            }
        })
    }


    fun setUpCalendar() {
        val dm = DisplayMetrics()
        val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        binding.exSevenCalendar.apply {
            val dayWidth = dm.widthPixels / 5
            val dayHeight = (dayWidth * 1.25).toInt()
            daySize = Size(dayWidth, dayHeight)
        }
        class DayViewContainer(view: View) : ViewContainer(view) {
            val bind = Example7CalendarDayBinding.bind(view)
            lateinit var day: CalendarDay

            init {
                view.setOnClickListener {
                    val firstDay = binding.exSevenCalendar.findFirstVisibleDay()
                    val lastDay = binding.exSevenCalendar.findLastVisibleDay()
                    if (firstDay == day) {
                        // If the first date on screen was clicked, we scroll to the date to ensure
                        // it is fully visible if it was partially off the screen when clicked.
                        binding.exSevenCalendar.smoothScrollToDate(day.date)
                        val DateNow = dayFormat.format(day.date)
                        updateDiagram(DateNow)
                    } else if (lastDay == day) {
                        // If the last date was clicked, we scroll to 4 days ago, this forces the
                        // clicked date to be fully visible if it was partially off the screen.
                        // We scroll to 4 days ago because we show max of five days on the screen
                        // so scrolling to 4 days ago brings the clicked date into full visibility
                        // at the end of the calendar view.
                        val DateNow = dayFormat.format(day.date)
                        updateDiagram(DateNow)
                        binding.exSevenCalendar.smoothScrollToDate(day.date.minusDays(4))
                    } else {
                        val DateNow = dayFormat.format(day.date)
                        updateDiagram(DateNow)

                    }

                    // Example: If you want the clicked date to always be centered on the screen,
                    // you would use: exSevenCalendar.smoothScrollToDate(day.date.minusDays(2))

                    if (selectedDate != day.date) {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        binding.exSevenCalendar.notifyDateChanged(day.date)
                        oldDate?.let { binding.exSevenCalendar.notifyDateChanged(it) }
                    }
                }
            }

            fun bind(day: CalendarDay) {
                this.day = day
                bind.exSevenDateText.text = dateFormatter.format(day.date)
                bind.exSevenDayText.text = dayFormatter.format(day.date)
                bind.exSevenMonthText.text = monthFormatter.format(day.date)

                bind.exSevenDateText.setTextColor(view.context.getColor(if (day.date == selectedDate) R.color.example_7_yellow else R.color.white))
                bind.exSevenSelectedView.isVisible = day.date == selectedDate
            }
        }

        binding.exSevenCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) = container.bind(day)
        }

        val currentMonth = YearMonth.now()
        // Value for firstDayOfWeek does not matter since inDates and outDates are not generated.
        binding.exSevenCalendar.setup(
            currentMonth,
            currentMonth.plusMonths(3),
            DayOfWeek.values().random()
        )
        binding.exSevenCalendar.scrollToDate(LocalDate.now())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
