package hu.bme.aut.android.highrollersden.betting

import com.google.gson.JsonElement
import hu.bme.aut.android.highrollersden.R
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private val retrofit: Retrofit
    private val bettingApi: BettingApi

    private const val SERVICE_URL = "https://apiv2.allsportsapi.com"
    private const val APP_ID = "44bfb179d9746ffed2e364d9a4771c92040d00af662c4d7ff0584e9fb553dc5a"



    init {
        retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        bettingApi = retrofit.create(BettingApi::class.java)
    }

    fun getMatch(leagueId: String, from: String, to: String): Call<MatchData?>? {
        return bettingApi.getMatch("Fixtures",leagueId, from, to, APP_ID)
    }

    fun getOdds(matchId: String): Call<JsonElement?>?{
        return bettingApi.getOdds("Odds", matchId, APP_ID)
    }

    fun getMatchWinner(matchId: String): Call<JsonElement?>?{
        return bettingApi.getMatchWinner("Fixtures", matchId, APP_ID)
    }

}