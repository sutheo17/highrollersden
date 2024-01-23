package hu.bme.aut.android.highrollersden

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import hu.bme.aut.android.highrollersden.databinding.FragmentStatisticsBinding
import hu.bme.aut.android.highrollersden.history.HistoryDataManager
import java.time.LocalDate
import java.time.LocalDateTime

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [StatisticsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatisticsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentStatisticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentStatisticsBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bets = HistoryDataManager.bets

        var sum : Int = 0
        var won : Int = 0

        binding.btnAllTime.setOnClickListener{
            sum = 0
            won = 0
            bets.forEach{
                bets -> sum += bets.wager.toInt()
                if(bets.already_decided && bets.won)
                {
                    won += bets.winnable.toInt()
                }
            }
            loadDiagram(sum, won)
        }
        binding.btnWeek.setOnClickListener{
            sum = 0
            won = 0
            val dateWeek = LocalDate.now().minusDays(7)

            val dateWeekYear = dateWeek.year
            val dateWeekMonth = dateWeek.monthValue
            val dateWeekDay = dateWeek.dayOfMonth

            var betYear : Int = 0
            var betMonth: Int = 0
            var betDay: Int = 0

            bets.forEach{
                    bets ->
                betYear = bets.date.substring(0,4).toInt()
                betMonth = bets.date.substring(5,7).toInt()
                betDay = bets.date.substring(8).toInt()
                if(betYear > dateWeekYear || (betYear == dateWeekYear && betMonth > dateWeekMonth) || (betYear == dateWeekYear && betMonth == dateWeekMonth && betDay >= dateWeekDay))
                {
                    sum += bets.wager.toInt()
                    if(bets.already_decided && bets.won)
                    {
                        won += bets.winnable.toInt()
                    }
                }

            }
            loadDiagram(sum, won)
        }
        binding.btnMonth.setOnClickListener{
            sum = 0
            won = 0
            val dateWeek = LocalDate.now().minusDays(30)

            val dateWeekYear = dateWeek.year
            val dateWeekMonth = dateWeek.monthValue
            val dateWeekDay = dateWeek.dayOfMonth

            var betYear : Int = 0
            var betMonth: Int = 0
            var betDay: Int = 0

            bets.forEach{
                    bets ->
                betYear = bets.date.substring(0,4).toInt()
                betMonth = bets.date.substring(5,7).toInt()
                betDay = bets.date.substring(8).toInt()
                if(betYear > dateWeekYear || (betYear == dateWeekYear && betMonth > dateWeekMonth) || (betYear == dateWeekYear && betMonth == dateWeekMonth && betDay >= dateWeekDay))
                {
                    sum += bets.wager.toInt()
                    if(bets.already_decided && bets.won)
                    {
                        won += bets.winnable.toInt()
                    }
                }

            }
            loadDiagram(sum, won)
        }
    }

    fun loadDiagram(sum: Int, won: Int)
    {
        val entries = listOf(PieEntry(sum.toFloat(), "Wagered"),
            PieEntry(won.toFloat(), "Won")
        )

        val dataSet = PieDataSet(entries, "Betting statistics")
        dataSet.setColors(Color.RED, Color.GREEN)

        val data = PieData(dataSet)


        binding.chartHoliday.data = data
        binding.chartHoliday.invalidate()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StatisticsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatisticsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}