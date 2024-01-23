package hu.bme.aut.android.highrollersden.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hu.bme.aut.android.highrollersden.EditProfileDialogFragment
import hu.bme.aut.android.highrollersden.R
import hu.bme.aut.android.highrollersden.data.PlayerDataManager
import hu.bme.aut.android.highrollersden.data.PlayerDataManager.player
import hu.bme.aut.android.highrollersden.databinding.FragmentProfileBinding
import hu.bme.aut.android.highrollersden.databinding.FragmentProfileDetailBinding

class DetailsProfileFragment: Fragment(), EditProfileDialogFragment.ProfileEditedListener{
    private lateinit var binding: FragmentProfileDetailBinding
    private lateinit var dialog: EditProfileDialogFragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileDetailBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener(){
            dialog = EditProfileDialogFragment()
            dialog.setDetailListener(this)
            dialog.setMainListener(ProfilePageAdapter.main)
            dialog.show(
                parentFragmentManager,
                "Profil szerkesztése"
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val player = PlayerDataManager.player
        binding.tvUsername.text = player.username
        binding.tvEmail.text = player.email
        binding.tvIDCard.text = player.player_id_card
        binding.tvAddress.text = player.address
    }

    override fun onProfileEdited() {
        val player = PlayerDataManager.player
        binding.tvUsername.text = player.username
        binding.tvEmail.text = player.email
        binding.tvIDCard.text = player.player_id_card
        binding.tvAddress.text = player.address
    }

}