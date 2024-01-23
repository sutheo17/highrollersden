package hu.bme.aut.android.highrollersden.betting

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.contentValuesOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import hu.bme.aut.android.highrollersden.R
import hu.bme.aut.android.highrollersden.databinding.ItemMatchBinding

class BetAdapter() : RecyclerView.Adapter<BetAdapter.MatchViewHolder>() {
    private val matches: MutableList<Result?> = ArrayList()

    inner class MatchViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding = ItemMatchBinding.bind(itemView)

        fun bind(newMatch: Result?)
        {
            binding.matches.text = newMatch?.event_key
            binding.homeNameTeam.text = newMatch?.event_home_team
            binding.awayNameTeam.text = newMatch?.event_away_team
            binding.date.text = newMatch?.event_date + "-" + newMatch?.event_time

            Glide.with(itemView).load(newMatch?.home_team_logo).transition(
                DrawableTransitionOptions().crossFade()).into(binding.homeLogo)
            Glide.with(itemView).load(newMatch?.away_team_logo).transition(DrawableTransitionOptions().crossFade()).into(binding.awayLogo)

            Log.d("NEWMACTTH:" , newMatch?.odds_home.toString())

            if(newMatch?.odds_home.toString() == "null" || newMatch?.odds_away.toString() == "null" || newMatch?.odds_draw.toString() == "null")
            {
                binding.oddsHome.text = "TBD"
                binding.oddsDraw.text = "TBD"
                binding.oddsAway.text = "TBD"
            }
            else
            {
                binding.oddsHome.text = newMatch?.odds_home
                binding.oddsDraw.text = newMatch?.odds_draw
                binding.oddsAway.text = newMatch?.odds_away
            }


            if(!BettingSlip.list.matches.contains(binding.matches.text))
            {
                BettingSlip.list.matches.put(newMatch?.event_key.toString(), "")
                BettingSlip.list.odds.put(newMatch?.event_key.toString(), "0")
                BettingSlip.list.home_team.put(newMatch?.event_key.toString(), newMatch?.event_home_team.toString())
                BettingSlip.list.away_team.put(newMatch?.event_key.toString(), newMatch?.event_away_team.toString())
                binding.oddsHome.setBackgroundColor(Color.BLACK)
                binding.oddsAway.setBackgroundColor(Color.BLACK)
                binding.oddsDraw.setBackgroundColor(Color.BLACK)
            }
            else
            {
                if(BettingSlip.list.matches.get(binding.matches.text) == "1")
                {
                    binding.oddsHome.setBackgroundColor(Color.BLUE)
                    binding.oddsAway.setBackgroundColor(Color.BLACK)
                    binding.oddsDraw.setBackgroundColor(Color.BLACK)
                }
                else if(BettingSlip.list.matches.get(binding.matches.text) == "2")
                {
                    binding.oddsAway.setBackgroundColor(Color.BLUE)
                    binding.oddsHome.setBackgroundColor(Color.BLACK)
                    binding.oddsDraw.setBackgroundColor(Color.BLACK)
                }
                else if(BettingSlip.list.matches.get(binding.matches.text) == "X")
                {
                    binding.oddsDraw.setBackgroundColor(Color.BLUE)
                    binding.oddsHome.setBackgroundColor(Color.BLACK)
                    binding.oddsAway.setBackgroundColor(Color.BLACK)
                }
                else
                {
                    binding.oddsHome.setBackgroundColor(Color.BLACK)
                    binding.oddsAway.setBackgroundColor(Color.BLACK)
                    binding.oddsDraw.setBackgroundColor(Color.BLACK)
                }
            }

            Log.d("JOOO", newMatch?.event_key.toString())

            if(binding.oddsHome.text != "TBD" || binding.oddsAway.text != "TBD" || binding.oddsDraw.text != "TBD")
            {
                binding.oddsHome.setOnClickListener(){
                    if(BettingSlip.list.matches.get(binding.matches.text) != "1")
                    {
                        binding.oddsHome.setBackgroundColor(Color.BLUE)
                        binding.oddsAway.setBackgroundColor(Color.BLACK)
                        binding.oddsDraw.setBackgroundColor(Color.BLACK)
                        BettingSlip.list.matches.put(binding.matches.text.toString(), "1")
                        BettingSlip.list.odds.put(binding.matches.text.toString(), binding.oddsHome.text.toString())
                    }
                    else
                    {
                        binding.oddsHome.setBackgroundColor(Color.BLACK)
                        BettingSlip.list.matches.put(binding.matches.text.toString(), "")
                        BettingSlip.list.odds.put(binding.matches.text.toString(), "0.0")
                    }
                }

                binding.oddsAway.setOnClickListener(){
                    if(BettingSlip.list.matches.get(binding.matches.text) != "2")
                    {
                        binding.oddsAway.setBackgroundColor(Color.BLUE)
                        binding.oddsHome.setBackgroundColor(Color.BLACK)
                        binding.oddsDraw.setBackgroundColor(Color.BLACK)
                        BettingSlip.list.matches.put(binding.matches.text.toString(), "2")
                        BettingSlip.list.odds.put(binding.matches.text.toString(), binding.oddsAway.text.toString())
                    }
                    else
                    {
                        binding.oddsAway.setBackgroundColor(Color.BLACK)
                        BettingSlip.list.matches.put(binding.matches.text.toString(), "")
                        BettingSlip.list.odds.put(binding.matches.text.toString(), "0.0")
                    }
                }

                binding.oddsDraw.setOnClickListener(){
                    if(BettingSlip.list.matches.get(binding.matches.text) != "X")
                    {
                        binding.oddsDraw.setBackgroundColor(Color.BLUE)
                        binding.oddsHome.setBackgroundColor(Color.BLACK)
                        binding.oddsAway.setBackgroundColor(Color.BLACK)
                        BettingSlip.list.matches.put(binding.matches.text.toString(), "X")
                        BettingSlip.list.odds.put(binding.matches.text.toString(), binding.oddsDraw.text.toString())
                    }
                    else
                    {
                        binding.oddsDraw.setBackgroundColor(Color.BLACK)
                        BettingSlip.list.matches.put(binding.matches.text.toString(), "")
                        BettingSlip.list.odds.put(binding.matches.text.toString(), "0.0")
                    }
                }
            }
        }
    }

    fun addMatch(match: Result?)
    {
        if(match?.event_home_team == null)
        {

        }
        else
        {
            matches.add(match)
            notifyItemInserted(matches.size - 1)
        }
    }

    fun clear()
    {
        matches.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_match, parent, false)
        return MatchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return matches.size
    }

    fun resetButtons()
    {
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val item = matches[position]
        holder.bind(item)

    }
}