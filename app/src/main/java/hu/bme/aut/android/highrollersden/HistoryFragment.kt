package hu.bme.aut.android.highrollersden

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.highrollersden.betting.BetAdapter
import hu.bme.aut.android.highrollersden.betting.BetSlipDialogFragment
import hu.bme.aut.android.highrollersden.betting.MatchDataManager.match
import hu.bme.aut.android.highrollersden.data.PlayerDataDB
import hu.bme.aut.android.highrollersden.data.PlayerDataManager
import hu.bme.aut.android.highrollersden.databinding.FragmentBetBinding
import hu.bme.aut.android.highrollersden.databinding.FragmentHistoryBinding
import hu.bme.aut.android.highrollersden.history.HistoryAdapter
import hu.bme.aut.android.highrollersden.history.HistoryDataDB
import hu.bme.aut.android.highrollersden.history.HistoryDataManager
import kotlin.concurrent.thread

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment(), HistoryAdapter.BalanceUpdateListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var playerDataDB: PlayerDataDB
    private lateinit var historyDataDB: HistoryDataDB

    private var _binding: FragmentHistoryBinding? = null
    private lateinit var adapter: HistoryAdapter
    private val binding get() = _binding!!

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
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = HistoryAdapter(this)
        binding.mainRecyclerView.adapter = adapter

        initRecycleView()

        return view
    }

    override fun onResume() {
        super.onResume()
        adapter.clear()
        if(HistoryDataManager.bets.size > 0)
        {
            var history = HistoryDataManager.bets
            history!!.forEach { match ->  adapter.addHistory(match)}
        }
    }

    private fun initRecycleView()
    {

        if(HistoryDataManager.bets.size > 0)
        {
            var history = HistoryDataManager.bets
            history!!.forEach { match ->  adapter.addHistory(match)}
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onWinningSlip(amount: Int, id: Long) {
        thread{
            playerDataDB= PlayerDataDB.getDatabase(requireActivity())
            val player = PlayerDataManager.player
            player.balance += amount.toLong()
            playerDataDB.playerDataDao().update(player.name, player.username, player.balance.toLong(), player.already_changed, player.email,
                player.player_id_card, player.address)

            historyDataDB= HistoryDataDB.getDatabase(requireActivity())
            historyDataDB.historyDataDAO().updateHistory(id, true, true)
            HistoryDataManager.bets = ArrayList(historyDataDB.historyDataDAO().getAll())
        }
    }

    override fun onLosingSlip(id: Long) {
        var history = HistoryDataManager.bets
        var player = PlayerDataManager.player
        var all_decided = true
        history.forEach{
            match -> if(!match.already_decided) {
                all_decided = false
        }
        }
        if(all_decided && player.balance.toInt() == 0)
        {
            thread{
                playerDataDB= PlayerDataDB.getDatabase(requireActivity())
                player.balance = 1000
                playerDataDB.playerDataDao().update(player.name, player.username, player.balance.toLong(), player.already_changed, player.email,
                    player.player_id_card, player.address)


            }
        }
        thread{
            historyDataDB= HistoryDataDB.getDatabase(requireActivity())
            historyDataDB.historyDataDAO().updateHistory(id, true, false)
            HistoryDataManager.bets = ArrayList(historyDataDB.historyDataDAO().getAll())
        }

    }
}