package hu.bme.aut.android.highrollersden

import android.app.ActionBar.DISPLAY_SHOW_CUSTOM
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import hu.bme.aut.android.highrollersden.betting.BetSlipDialogFragment
import hu.bme.aut.android.highrollersden.data.PlayerData
import hu.bme.aut.android.highrollersden.data.PlayerDataDB
import hu.bme.aut.android.highrollersden.data.PlayerDataManager
import hu.bme.aut.android.highrollersden.data.PlayerDataManager.player
import hu.bme.aut.android.highrollersden.databinding.ActionbarLayoutBinding
import hu.bme.aut.android.highrollersden.databinding.ActivityMainBinding
import hu.bme.aut.android.highrollersden.databinding.FragmentProfileDetailBinding
import hu.bme.aut.android.highrollersden.databinding.FragmentProfileMainBinding
import hu.bme.aut.android.highrollersden.history.HistoryDataDB
import hu.bme.aut.android.highrollersden.history.HistoryDataManager
import hu.bme.aut.android.highrollersden.history.HistoryDataManager.bets
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), EditProfileDialogFragment.ProfileEditedListener, BetSlipDialogFragment.BetSubmittedListener{

    private lateinit var binding : ActionbarLayoutBinding

    private lateinit var playerDB: PlayerDataDB
    private lateinit var historyDB: HistoryDataDB
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.actionbar_layout)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadPlayerData()
        loadHistoryData()


    }

    fun loadPlayerData()
    {
        playerDB= PlayerDataDB.getDatabase(applicationContext)
        thread {
            var playerData = playerDB.playerDataDao().getAll()

            if (playerData.size == 0) {
                val player = PlayerDataManager.player

                playerDB.playerDataDao().addPlayer(
                    PlayerData(
                        name = player.name,
                        username = player.username,
                        balance = player.balance.toLong(),
                        email = player.email,
                        address = player.address,
                        id_card = player.player_id_card,
                        already_changed = player.already_changed
                    )
                )
            }
            else
            {
                val player = PlayerDataManager.player
                player.name = playerData.first().name
                player.username = playerData.first().username
                player.player_id_card = playerData.first().id_card
                player.balance = playerData.first().balance
                player.address = playerData.first().address
                player.already_changed = playerData.first().already_changed
                player.email = playerData.first().email
            }
        }
    }

    fun loadHistoryData()
    {
        var historyDB = HistoryDataDB.getDatabase(applicationContext)
        thread{
            var historyData = historyDB.historyDataDAO().getAll()
            HistoryDataManager.bets = ArrayList(historyData)
        }
    }

    override fun onProfileEdited() {
        playerDB = PlayerDataDB.getDatabase(applicationContext)

        thread{
            val player = PlayerDataManager.player
            playerDB.playerDataDao().update(player.name, player.username, player.balance.toLong(), player.already_changed, player.email,
                player.player_id_card, player.address)
        }
    }

    override fun onBetSubmitted() {
        thread{
        playerDB= PlayerDataDB.getDatabase(applicationContext)
            val player = PlayerDataManager.player
            playerDB.playerDataDao().update(player.name, player.username, player.balance.toLong(), player.already_changed, player.email,
                player.player_id_card, player.address)
    }}
}