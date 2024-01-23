package hu.bme.aut.android.highrollersden.history

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonElement
import hu.bme.aut.android.highrollersden.R
import hu.bme.aut.android.highrollersden.betting.MatchData
import hu.bme.aut.android.highrollersden.betting.MatchDataManager
import hu.bme.aut.android.highrollersden.betting.MatchDataManager.match
import hu.bme.aut.android.highrollersden.betting.NetworkManager
import hu.bme.aut.android.highrollersden.data.PlayerDataDB
import hu.bme.aut.android.highrollersden.data.PlayerDataManager
import hu.bme.aut.android.highrollersden.data.PlayerDataManager.player
import hu.bme.aut.android.highrollersden.databinding.ItemHistoryBinding
import hu.bme.aut.android.highrollersden.history.HistoryDataManager.bets
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class HistoryAdapter(val listener: Fragment) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private val bets: MutableList<HistoryData> = ArrayList()
    private lateinit var listenerFragment : BalanceUpdateListener


    interface BalanceUpdateListener
    {
           fun onWinningSlip(amount: Int, id: Long)
           fun onLosingSlip(id: Long)

    }


    inner class HistoryViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView){
       var binding = ItemHistoryBinding.bind(itemView)
        fun bind(newData: HistoryData)
        {
            binding.date.text = newData.date
            binding.wager.text = newData.wager
            binding.details.setBackgroundColor(Color.WHITE)
            binding.winnable.text = newData.winnable

            var matchResults: ArrayList<String> = ArrayList<String>()

            val bundle = Bundle()

            bundle.putStringArrayList("home_match", newData.home_team)
            bundle.putStringArrayList("away_match", newData.away_team)
            bundle.putStringArrayList("odds", newData.guess)
            bundle.putStringArrayList("event_key", newData.event_key)

            getGuessCorrectness(newData, itemView, bundle)

        }
    }
    fun getGuessCorrectness(newData: HistoryData, itemView: View, bundle: Bundle){
        var event_number : Int = newData.event_key.size
        val matchResults: ArrayList<String> = ArrayList<String>()

        var results :HashMap<String, String> = HashMap<String, String>()

        for(i in 0..<event_number) {
            NetworkManager.getMatchWinner(newData.event_key.get(i))?.enqueue(object : Callback<JsonElement?> {
                override fun onResponse(
                    call: Call<JsonElement?>,
                    response: Response<JsonElement?>
                ){
                    if (response.isSuccessful) {
                        var matchData = response.body()

                        var data = matchData?.asJsonObject?.get("result")?.asJsonArray

                        val result = data?.get(0)?.asJsonObject?.get("event_ft_result")?.toString()
                        Log.d("result:", result!!)
                        Log.d("home", newData.home_team.get(i))
                        Log.d("away", newData.away_team.get(i))


                        if(result.length > 2)
                        {
                            val homeScoreFull = result?.substringBefore('-')
                            val awayScoreFull = result?.substringAfter('-')

                            val homeScore = homeScoreFull?.substring(1,2)!!
                            val awayScore = awayScoreFull?.substring(1,2)!!

                            if(homeScore.toInt() == awayScore.toInt())
                            {
                                results.put(newData.event_key.get(i), "X")
                            }
                            else if(homeScore.toInt() > awayScore.toInt())
                            {
                                results.put(newData.event_key.get(i), "1")
                            }
                            else
                            {
                                results.put(newData.event_key.get(i), "2")
                            }
                        }
                        else
                        {
                            results.put(newData.event_key.get(i), "NOT YET ENDED")
                        }

                        if(results.size == newData.event_key.size)
                        {
                            var binding = ItemHistoryBinding.bind(itemView)
                            var winningSlip : Boolean = true
                            var not_yet_ended: Boolean = false
                            for(i in 0..< results.size)
                            {
                                if(results.get(newData.event_key.get(i)) == "NOT YET ENDED")
                                {
                                    binding.details.setBackgroundColor(Color.BLACK)
                                    not_yet_ended = true
                                    break
                                }
                                else if(results.get(newData.event_key.get(i)) != newData.guess.get(i))
                                {
                                    winningSlip = false
                                    break
                                }
                            }

                            if(not_yet_ended)
                            {
                                binding.details.setBackgroundColor(Color.BLACK)
                            }

                            if(!not_yet_ended && winningSlip)
                            {
                                binding.details.setBackgroundColor(Color.GREEN)
                            }
                            else if (!not_yet_ended)
                            {
                                binding.details.setBackgroundColor(Color.RED)
                            }

                            if(!newData.already_decided && !not_yet_ended)
                            {
                                listenerFragment = listener as BalanceUpdateListener
                                if(winningSlip)
                                {
                                    listenerFragment.onWinningSlip(newData.winnable.toInt(), newData.id!!)
                                }
                                else
                                {
                                    listenerFragment.onLosingSlip(newData.id!!)
                                }
                            }


                            newData.event_key.forEach {
                                match -> matchResults.add(results.get(match)!!)
                            }

                            bundle.putStringArrayList("event_result", matchResults)

                            binding.details.setOnClickListener()
                            {

                                listener.findNavController().navigate(R.id.action_historyFragment_to_historyDetailFragment, bundle)
                            }
                        }

                    } else {
                        Log.d("maritt", response.message())
                    }

                }
                override fun onFailure(
                    call: Call<JsonElement?>,
                    throwable: Throwable
                ) {
                    throwable.printStackTrace()
                    Log.d("HIBA2222", "HIBA VAN")
                }

            })
        }
        }

    fun addHistory(newData: HistoryData)
    {
        bets.add(newData)
        notifyItemInserted(bets.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    fun clear()
    {
        bets.clear()
    }

    override fun getItemCount(): Int {
        return bets.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = bets[position]
        holder.bind(item)
    }
}