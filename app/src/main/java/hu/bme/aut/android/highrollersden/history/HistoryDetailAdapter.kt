package hu.bme.aut.android.highrollersden.history

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.highrollersden.R
import hu.bme.aut.android.highrollersden.databinding.ItemDetailHistoryBinding
import hu.bme.aut.android.highrollersden.databinding.ItemHistoryBinding
import hu.bme.aut.android.highrollersden.history.HistoryDataManager.bets

class HistoryDetailAdapter : RecyclerView.Adapter<HistoryDetailAdapter.HistoryDetailViewHolder>(){
    private val matches: MutableList<HistoryDetailData> = ArrayList()

    inner class HistoryDetailViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding = ItemDetailHistoryBinding.bind(itemView)
        fun bind(newMatch: HistoryDetailData)
        {
            binding.homeTeam.text = newMatch.home
            binding.awayTeam.text = newMatch.away
            if(newMatch.guess.length == 1)
            {
                binding.guess.text = newMatch.guess
            }
            else
            {
                binding.guess.text = newMatch.guess.substring(0, 1)
                if(newMatch.guess.substring(1,2) == "C")
                {
                    binding.guess.setTextColor(Color.GREEN)
                }
                else
                {
                    binding.guess.setTextColor(Color.RED)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_history, parent, false)
        return HistoryDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return matches.size
    }

    fun addMatch(newMatch: HistoryDetailData)
    {
        matches.add(newMatch)
        notifyItemInserted(matches.size - 1)
    }

    fun clear()
    {
        matches.clear()
    }

    override fun onBindViewHolder(holder: HistoryDetailViewHolder, position: Int) {
        val item = matches[position]
        holder.bind(item)
    }
}