package hu.bme.aut.android.highrollersden

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import hu.bme.aut.android.highrollersden.data.PlayerDataDB
import hu.bme.aut.android.highrollersden.data.PlayerDataManager
import hu.bme.aut.android.highrollersden.databinding.DialogEditProfileBinding
import hu.bme.aut.android.highrollersden.player.DetailsProfileFragment
import javax.security.auth.callback.Callback
import kotlin.concurrent.thread

class EditProfileDialogFragment: DialogFragment() {

    interface ProfileEditedListener{
        fun onProfileEdited()
    }

    private lateinit var listenerMain: ProfileEditedListener
    private lateinit var listenerDetail: ProfileEditedListener
    private lateinit var listener : ProfileEditedListener
    private lateinit var binding: DialogEditProfileBinding

    fun setMainListener(editedListener: ProfileEditedListener)
    {
        listenerMain = editedListener
    }
    fun setDetailListener(editedListener: ProfileEditedListener)
    {
        listenerDetail = editedListener
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as ProfileEditedListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditProfileBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireContext())
            .setTitle("Profil szerkesztése")
            .setView(binding.root)
            .setPositiveButton("Mentés") { dialogInterface, i ->
                var player = PlayerDataManager.player
                if(binding.etName.text.toString().isEmpty() || binding.etUsername.text.toString().isEmpty()||
                    binding.etEmail.text.toString().isEmpty() || binding.etAddress.text.toString().isEmpty() ||
                    binding.etIDCard.text.toString().isEmpty())
                {
                    Toast.makeText(context, "Az összes mezőhöz adj meg adatot!", Toast.LENGTH_LONG).show()
                }
                else
                {
                    player.already_changed = true
                    player.name = binding.etName.text.toString()
                    player.username = binding.etUsername.text.toString()
                    player.email = binding.etEmail.text.toString()
                    player.address = binding.etAddress.text.toString()
                    player.player_id_card = binding.etIDCard.text.toString()
                    listenerMain.onProfileEdited()
                    listenerDetail.onProfileEdited()
                    listener.onProfileEdited()

                }

            }
            .setNegativeButton("Mégse", null)
            .create()
    }
}