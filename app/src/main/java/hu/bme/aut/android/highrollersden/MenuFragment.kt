package hu.bme.aut.android.highrollersden

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.highrollersden.betting.BettingSlip
import hu.bme.aut.android.highrollersden.data.PlayerDataManager
import hu.bme.aut.android.highrollersden.databinding.FragmentMenuBinding
import hu.bme.aut.android.highrollersden.databinding.FragmentProfileDetailBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_profileFragment)
        }
        binding.btnStatistics.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_statisticsFragment)
        }

        binding.btnBet.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_betFragment)
        }

        binding.btnHistory.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_historyFragment)
        }
        val player = PlayerDataManager.player
        if(!player.already_changed)
        {
            binding.btnHistory.setEnabled(false)
            binding.btnBet.setEnabled(false)
            binding.btnStatistics.setEnabled(false)
        }
        else
        {
            binding.btnHistory.setEnabled(true)
            binding.btnBet.setEnabled(true)
            binding.btnStatistics.setEnabled(true)
        }


    }

    override fun onResume() {
        super.onResume()
        BettingSlip.list.matches.clear()
        BettingSlip.list.odds.clear()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}