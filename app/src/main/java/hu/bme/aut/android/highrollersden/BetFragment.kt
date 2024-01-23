package hu.bme.aut.android.highrollersden

import android.content.Context
import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import android.R.menu;
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.set
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonElement
import hu.bme.aut.android.highrollersden.betting.BetAdapter
import hu.bme.aut.android.highrollersden.betting.BetSlipDialogFragment
import hu.bme.aut.android.highrollersden.betting.BettingSlip
import hu.bme.aut.android.highrollersden.betting.MatchData
import hu.bme.aut.android.highrollersden.betting.MatchDataManager
import hu.bme.aut.android.highrollersden.betting.NetworkManager
import hu.bme.aut.android.highrollersden.betting.Result
import hu.bme.aut.android.highrollersden.databinding.FragmentBetBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class BetFragment : Fragment(), BetSlipDialogFragment.BetSubmittedListener{

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentBetBinding? = null
    private lateinit var adapter: BetAdapter
    private lateinit var listener : EditProfileDialogFragment.ProfileEditedListener

    private val binding get() = _binding!!


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as EditProfileDialogFragment.ProfileEditedListener
    }

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



        _binding = FragmentBetBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = BetAdapter()


        getNumberOfMatchesByLeague("354")

        return view
    }

    private fun initRecyclerView(numberOfMatches: Int, id: String)
    {


        if(numberOfMatches > 0)
        {
            if (numberOfMatches>= 5)
            {
                for(i in 0..<5)
                {
                    getMatchByLeague(id, i)
                }
            }
            else
            {
                for(i in 0..<numberOfMatches)
                {
                    getMatchByLeague(id, i)
                }
            }

        }
        else
        {
            Toast.makeText(context, "Nincsen mérkőzés az elkövetkezendő két hétben ebben a kategóriában!", Toast.LENGTH_LONG).show()
        }
        binding.mainRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.inflateMenu(R.menu.menu_toolbar);
        val toolbarMenu: Menu = binding.toolbar.menu
        for (i in 0 until toolbarMenu.size()) {
            val menuItem: MenuItem = toolbarMenu.getItem(i)
            menuItem.setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
            if (menuItem.hasSubMenu()) {
                val subMenu: SubMenu = menuItem.subMenu!!
                for (j in 0 until subMenu.size()) {
                    subMenu.getItem(j)
                        .setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
                }
            }
        }

    }

    private fun anyMatchSelected() : Boolean{
        var matches = BettingSlip.list.odds
        matches.forEach { number ->
            if (number.value.toDouble() != 0.0) {
                return true;
            }
        }
        return false;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.eb -> {
                item.isChecked = true
                adapter.clear()
                getNumberOfMatchesByLeague("354")
                true
            }
            R.id.vb_afr -> {
                item.isChecked = true
                adapter.clear()
                getNumberOfMatchesByLeague("21")
                true

            }
            R.id.vb_eur -> {
                item.isChecked = true
                adapter.clear()
                getNumberOfMatchesByLeague("24")
                true
            }
            R.id.pl -> {
                item.isChecked = true
                adapter.clear()
                getNumberOfMatchesByLeague("152")
                true
            }
            R.id.seriea -> {
                item.isChecked = true
                adapter.clear()
                getNumberOfMatchesByLeague("207")
                true
            }
            R.id.bundesliga -> {
                item.isChecked = true
                adapter.clear()
                getNumberOfMatchesByLeague("175")
                true
            }
            R.id.ligue1 -> {
                item.isChecked = true
                adapter.clear()
                getNumberOfMatchesByLeague("168")
                true
            }
            R.id.laliga -> {
                item.isChecked = true
                adapter.clear()
                getNumberOfMatchesByLeague("302")
                true
            }
            R.id.betslip -> {
                if(anyMatchSelected())
                {
                    BetSlipDialogFragment(this).show(parentFragmentManager, "Szelvény")
                }
                else
                {
                    Toast.makeText(context, "Nincsen egyetlen mérkőzés sem kiválasztva!", Toast.LENGTH_LONG).show()
                }
                true

            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getNumberOfMatchesByLeague(id: String)
    {
        var from = LocalDate.now().year.toString() + "-" + LocalDate.now().monthValue.toString() + "-" + LocalDate.now().dayOfMonth.toString()
        var from_added_week = LocalDate.now().plusDays(14)
        var to = from_added_week.year.toString() + "-" + from_added_week.monthValue.toString() + "-" + from_added_week.dayOfMonth.toString()

        NetworkManager.getMatch(id, from, to)?.enqueue(object : Callback<MatchData?> {
            override fun onResponse(
                call: Call<MatchData?>,
                response: Response<MatchData?>
            ){
                Log.d("TAG", "onResponse: " + response.code())
                if (response.isSuccessful) {
                    var matchData = response.body()
                    if(matchData?.result === null)
                    {
                        initRecyclerView(0, id)
                    }
                    else
                    {
                        initRecyclerView(matchData?.result?.size!!, id)
                    }


                } else {
                    Toast.makeText(context, "Hiba az adatok lekérése során: " + response.message().toString(), Toast.LENGTH_LONG).show()
                }

            }
            override fun onFailure(
                call: Call<MatchData?>,
                throwable: Throwable
            ) {
                throwable.printStackTrace()
                Toast.makeText(context, "Hiba az adatok lekérése során!", Toast.LENGTH_LONG).show()
            }

    })
    }

    private fun getMatchByLeague(id: String, posFromLast: Int)
    {
        var match = MatchDataManager.match
        var from = LocalDate.now().year.toString() + "-" + LocalDate.now().monthValue.toString() + "-" + LocalDate.now().dayOfMonth.toString()
        var from_added_week = LocalDate.now().plusDays(14)
        var to = from_added_week.year.toString() + "-" + from_added_week.monthValue.toString() + "-" + from_added_week.dayOfMonth.toString()

        NetworkManager.getMatch(id, from, to)?.enqueue(object : Callback<MatchData?> {
            override fun onResponse(
                call: Call<MatchData?>,
                response: Response<MatchData?>
            ){
                Log.d("TAG", "onResponse: " + response.code())
                if (response.isSuccessful) {
                    var matchData = response.body()
                    if(matchData?.result === null)
                    {

                    }
                    else
                    {
                        var pos = matchData?.result?.size!! - posFromLast - 1
                        NetworkManager.getOdds(matchData?.result?.get(pos)?.event_key.toString())?.enqueue(object : Callback<JsonElement?> {
                            override fun onResponse(
                                call: Call<JsonElement?>,
                                response: Response<JsonElement?>
                            ) {
                                Log.d("TAG", "onResponse: " + response.code())
                                if (response.isSuccessful) {
                                    var oddsData = response.body()

                                    var oddsresult = oddsData?.asJsonObject?.get("result")?.asJsonObject

                                    var oddsresult_match = oddsresult?.get(matchData?.result?.get(pos)?.event_key.toString())?.asJsonArray

                                    match.odds_home = oddsresult_match?.get(4)?.asJsonObject?.get("odd_1").toString()
                                    match.odds_away = oddsresult_match?.get(4)?.asJsonObject?.get("odd_2").toString()
                                    match.odds_draw = oddsresult_match?.get(4)?.asJsonObject?.get("odd_x").toString()
                                    match.event_home_team = matchData?.result?.get(pos)?.event_home_team.toString()
                                    match.event_away_team = matchData?.result?.get(pos)?.event_away_team.toString()
                                    match.event_date = matchData?.result?.get(pos)?.event_date.toString()
                                    match.event_time = matchData?.result?.get(pos)?.event_time.toString()
                                    match.home_team_logo = matchData?.result?.get(pos)?.home_team_logo
                                    match.away_team_logo = matchData?.result?.get(pos)?.away_team_logo
                                    match.event_key = matchData?.result?.get(pos)?.event_key

                                    adapter.addMatch(Result(match.event_key, match.event_home_team, match.event_away_team, match.event_date, match.event_time, match.home_team_logo, match.away_team_logo, match.odds_home, match.odds_draw, match.odds_away))


                                } else {
                                    Toast.makeText(context, "Hiba az adatok lekérése során: " + response.message().toString(), Toast.LENGTH_LONG).show()
                                }
                            }
                            override fun onFailure(
                                call: Call<JsonElement?>,
                                throwable: Throwable
                            ) {
                                throwable.printStackTrace()
                                Toast.makeText(context, "Hiba az adatok lekérése során!", Toast.LENGTH_LONG).show()
                            }
                        })
                    }

                } else {
                    Toast.makeText(context, "Hiba az adatok lekérése során: " + response.message().toString(), Toast.LENGTH_LONG).show()
                }

            }
            override fun onFailure(
                call: Call<MatchData?>,
                throwable: Throwable
            ) {
                throwable.printStackTrace()
                Toast.makeText(context, "Hiba az adatok lekérése során!", Toast.LENGTH_LONG).show()
            }

        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onBetSubmitted() {
        adapter.resetButtons()

    }

}