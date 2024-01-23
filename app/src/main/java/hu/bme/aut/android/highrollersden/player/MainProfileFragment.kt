package hu.bme.aut.android.highrollersden.player

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hu.bme.aut.android.highrollersden.EditProfileDialogFragment
import hu.bme.aut.android.highrollersden.data.PlayerDataManager
import hu.bme.aut.android.highrollersden.data.PlayerDataManager.player
import hu.bme.aut.android.highrollersden.databinding.FragmentProfileMainBinding

class MainProfileFragment: Fragment(), EditProfileDialogFragment.ProfileEditedListener{
    private lateinit var binding: FragmentProfileMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val player = PlayerDataManager.player
        binding.tvName.text = player.name
        binding.tvBalance.text = player.balance.toString()
        binding.tvBalance.setTextColor(Color.RED)
    }

    override fun onProfileEdited() {
        val player = PlayerDataManager.player
        binding.tvName.text = player.name
    }

}