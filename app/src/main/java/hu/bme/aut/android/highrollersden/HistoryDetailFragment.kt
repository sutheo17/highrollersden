package hu.bme.aut.android.highrollersden

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.highrollersden.databinding.FragmentHistoryBinding
import hu.bme.aut.android.highrollersden.databinding.FragmentHistoryDetailBinding
import hu.bme.aut.android.highrollersden.history.HistoryAdapter
import hu.bme.aut.android.highrollersden.history.HistoryDataManager
import hu.bme.aut.android.highrollersden.history.HistoryDetailAdapter
import hu.bme.aut.android.highrollersden.history.HistoryDetailData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryDetailFragment() : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentHistoryDetailBinding? = null
    private lateinit var adapter: HistoryDetailAdapter
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
        _binding = FragmentHistoryDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = HistoryDetailAdapter()
        binding.mainRecyclerView.adapter = adapter



        initRecycleView()

        return view
    }

    override fun onResume() {
        super.onResume()
        adapter.clear()
        initRecycleView()
    }

    private fun initRecycleView()
    {
        val home_matches = arguments?.getStringArrayList("home_match")!!
        val away_matches = arguments?.getStringArrayList("away_match")!!
        val guess = arguments?.getStringArrayList("odds")!!
        val result = arguments?.getStringArrayList("event_result")!!

        for(i in 0..<home_matches.size)
        {
            Log.d("guess:", guess.get(i))
            Log.d("result:", result.get(i))
            if(result.get(i) == "NOT YET ENDED")
            {
                if(guess.get(i) == "1")
                {
                    adapter.addMatch(HistoryDetailData(home_matches.get(i), away_matches.get(i), "H"))

                }
                else if(guess.get(i) == "2")
                {
                    adapter.addMatch(HistoryDetailData(home_matches.get(i), away_matches.get(i), "A"))

                }
                else
                {
                    adapter.addMatch(HistoryDetailData(home_matches.get(i), away_matches.get(i), "X"))

                }
            }
            else if(guess.get(i) == "1")
            {
                if(result.get(i) == guess.get(i))
                {
                    adapter.addMatch(HistoryDetailData(home_matches.get(i), away_matches.get(i), "HC"))
                }
                else
                {
                    adapter.addMatch(HistoryDetailData(home_matches.get(i), away_matches.get(i), "HW"))
                }

            }
            else if(guess.get(i) == "2")
            {
                if(result.get(i) == guess.get(i))
                {
                    adapter.addMatch(HistoryDetailData(home_matches.get(i), away_matches.get(i), "AC"))
                }
                else
                {
                    adapter.addMatch(HistoryDetailData(home_matches.get(i), away_matches.get(i), "AW"))
                }
            }
            else
            {
                if(result.get(i) == guess.get(i))
                {
                    adapter.addMatch(HistoryDetailData(home_matches.get(i), away_matches.get(i), "XC"))
                }
                else
                {
                    adapter.addMatch(HistoryDetailData(home_matches.get(i), away_matches.get(i), "XW"))
                }
            }

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment historyDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}