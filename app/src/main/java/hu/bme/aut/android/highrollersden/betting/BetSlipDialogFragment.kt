package hu.bme.aut.android.highrollersden.betting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.highrollersden.EditProfileDialogFragment
import hu.bme.aut.android.highrollersden.betting.MatchDataManager.match
import hu.bme.aut.android.highrollersden.data.PlayerDataDB
import hu.bme.aut.android.highrollersden.data.PlayerDataManager
import hu.bme.aut.android.highrollersden.databinding.DialogBettinslipBinding
import hu.bme.aut.android.highrollersden.history.HistoryData
import hu.bme.aut.android.highrollersden.history.HistoryDataDB
import hu.bme.aut.android.highrollersden.history.HistoryDataManager
import java.time.LocalDate
import kotlin.concurrent.thread

class BetSlipDialogFragment(val listener: BetSubmittedListener): DialogFragment(){

    private lateinit var listenerBetSubmit : BetSubmittedListener
    private lateinit var historyDB: HistoryDataDB
    interface BetSubmittedListener{
        fun onBetSubmitted()
    }

    private lateinit var binding: DialogBettinslipBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listenerBetSubmit = context as BetSubmittedListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogBettinslipBinding.inflate(LayoutInflater.from(context))

        var sum : Double = 0.0
        var matches = BettingSlip.list.odds
        matches.forEach { number -> if(sum == 0.0) {
            sum += number.value.toDouble()
        }
        else if(number.value.toDouble() != 0.0)
        {
            sum *= number.value.toDouble()
        }
        }

        var first = sum.toString().substringBefore('.')
        var second = sum.toString().substringAfter('.')
        if(second.length > 4)
        {
            second = second.substring(0, 4)
        }

        var sumText = "Eredő odds: " + first + "." + second

        binding.sum.text = sumText
        binding.balanceAmount.text = PlayerDataManager.player.balance.toString()

        return AlertDialog.Builder(requireContext())
            .setTitle("Szelvény")
            .setView(binding.root)
            .setPositiveButton("Fogadás mentése") { dialogInterface, i ->
                if(binding.etWager.text.toString().toDouble() > PlayerDataManager.player.balance.toDouble())
                {
                    Toast.makeText(context, "Nincsen ennyi egyenleged!", Toast.LENGTH_LONG).show()
                }
                else
                {


                    val player = PlayerDataManager.player
                    val newBal = player.balance.toInt() - binding.etWager.text.toString().toInt()
                    player.balance = newBal.toLong()


                    var history = HistoryDataManager.bets
                    var sum_odds = (first + "." + second).toDouble()
                    var winnable_Double : Double = binding.etWager.text.toString().toDouble() * sum_odds

                    var winnable = winnable_Double.toString().substringBefore('.')

                    val date = LocalDate.now().year.toString() + "-" + LocalDate.now().monthValue.toString() + "-" + LocalDate.now().dayOfMonth.toString()
                    var eventKey : ArrayList<String> = ArrayList<String>()
                    BettingSlip.list.matches.forEach {
                            match -> if(match.value.toString() != ""){
                        eventKey.add(match.key.toString())
                    }
                    }

                    val homeTeamList : ArrayList<String> = ArrayList<String>()
                    val awayTeamList: ArrayList<String> = ArrayList<String>()
                    val oddsMatches : ArrayList<String> = ArrayList<String>()

                    eventKey.forEach { match ->
                        homeTeamList.add(BettingSlip.list.home_team.get(match)!!)
                        awayTeamList.add(BettingSlip.list.away_team.get(match)!!)
                        oddsMatches.add(BettingSlip.list.matches.get(match)!!)
                    }

                    historyDB = HistoryDataDB.getDatabase(requireActivity())

                    thread {
                        historyDB.historyDataDAO().addHistory(
                            HistoryData(
                                winnable = winnable.toString(),
                                date = date,
                                wager = binding.etWager.text.toString(),
                                home_team = homeTeamList,
                                away_team = awayTeamList,
                                guess = oddsMatches,
                                event_key = eventKey,
                                already_decided = false,
                                won = false
                            )
                        )
                        var historyData = historyDB.historyDataDAO().getAll()
                        HistoryDataManager.bets = ArrayList(historyData)
                    }


                    BettingSlip.list.matches.clear()
                    BettingSlip.list.odds.clear()
                    BettingSlip.list.home_team.clear()
                    BettingSlip.list.away_team.clear()
                    listener.onBetSubmitted()
                    listenerBetSubmit.onBetSubmitted()
                }

            }
            .setNegativeButton("Mégse", null)
            .create()
    }
}