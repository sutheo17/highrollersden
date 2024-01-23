package hu.bme.aut.android.highrollersden.betting

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BettingApi {
    @GET("/football")
    fun getMatch(
        @Query("met") mode: String?,
        @Query("leagueId") leagueId: String?,
        @Query("from") fromDate: String?,
        @Query("to") toDate: String?,
        @Query("APIkey") appId: String?
    ): Call<MatchData?>?

    @GET("/football")
    fun getOdds(
        @Query("met") mode: String?,
        @Query("matchId") matchId: String?,
        @Query("APIkey") appId: String?
    ): Call<JsonElement?>?

    @GET("/football")
    fun getMatchWinner(
        @Query("met") mode: String?,
        @Query("matchId") matchId: String?,
        @Query("APIkey") appId: String?
    ): Call<JsonElement?>?
}