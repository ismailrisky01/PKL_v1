package com.example.pkl_v1.ui.dashboard


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pkl_v1.R
import com.example.pkl_v1.databinding.FragmentDashboardBinding
import com.example.pkl_v1.model.AlarmModel
import com.example.pkl_v1.model.ModelPasien
import com.example.pkl_v1.model.ModelQuestion
import com.example.pkl_v1.model.Score
import com.example.pkl_v1.util.SharedPref
import com.example.pkl_v1.viewmodel.DashboardViewModel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.firestore.FirebaseFirestore


class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private var scoreList = ArrayList<Score>()
    private val mDashboardViewModel by lazy {
        ViewModelProvider(this).get(DashboardViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkProfile()
        scoreList = getScoreList()
        setChart2()
        binding.IDDashboardBtnSettingProfile.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_profileFragment)
        }
    }

    fun setChart() {
        val barChart = binding.chart1
        val entries: ArrayList<BarEntry> = ArrayList()
        entries.add(BarEntry(1f, 4f))
        entries.add(BarEntry(2f, 10f))
        entries.add(BarEntry(3f, 2f))
        entries.add(BarEntry(4f, 15f))
        entries.add(BarEntry(5f, 13f))
        entries.add(BarEntry(6f, 2f))

        val barDataSet = BarDataSet(entries, "")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val data = BarData(barDataSet)
        barChart.data = data

        //hide grid lines
        barChart.axisLeft.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.setDrawAxisLine(false)

        //remove right y-axis
        barChart.axisRight.isEnabled = false

        //remove legend
        barChart.legend.isEnabled = false


        //remove description label
        barChart.description.isEnabled = false


        //add animation
        barChart.animateY(3000)


        //draw chart
        barChart.invalidate()

    }
    fun setChart2(){
        val barChart = binding.chart1

        initBarChart()
        //now draw bar chart with dynamic data
        val entries: ArrayList<BarEntry> = ArrayList()

        //you can replace this data object with  your custom object
        for (i in scoreList.indices) {
            val score = scoreList[i]
            entries.add(BarEntry(i.toFloat(), score.score.toFloat()))
        }

        val barDataSet = BarDataSet(entries, "Jam")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val data = BarData(barDataSet)
        barChart.data = data

        barChart.invalidate()
    }
    private fun initBarChart() {
        val barChart = binding.chart1


//        hide grid lines
        barChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        barChart.axisRight.isEnabled = false

        //remove legend
        barChart.legend.isEnabled = false


        //remove description label
        barChart.description.isEnabled = false


        //add animation
        barChart.animateY(3000)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f

    }


    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            Log.d("PKL", "getAxisLabel: index $index")
            return if (index < scoreList.size) {
                scoreList[index].name
            } else {
                ""
            }
        }
    }


    // simulate api call
    // we are initialising it directly
    private fun getScoreList(): ArrayList<Score> {
        scoreList.add(Score("Duduk", 5))
        scoreList.add(Score("Berdiri", 10))
        scoreList.add(Score("Berbaring", 5))
        scoreList.add(Score("Berjalan", 4))

        return scoreList
    }


    fun checkProfile() {
        mDashboardViewModel.getProfile().observe(viewLifecycleOwner, {
            if (it.idPasien.isEmpty()) {
            } else {
                setUpData(it)
            }
        })
    }

    private fun setUpData(modelPasien: ModelPasien) {
        binding.IDDashboardNamaPasien.text = "Nama : ${modelPasien.namaPaien}"
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.toolbar_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.setting -> findNavController().navigate(R.id.action_dashboardFragment_to_alarmFragment)
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
