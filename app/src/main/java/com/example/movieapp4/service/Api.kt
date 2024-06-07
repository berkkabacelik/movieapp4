package com.example.movieapp4.service

import com.example.movieapp4.model.GetMoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = "34fc71aa5972ffbff70b167092be8fdf",
        @Query("page") page: Int): Call<GetMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String = "34fc71aa5972ffbff70b167092be8fdf",
        @Query("page") page: Int): Call<GetMoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String = "34fc71aa5972ffbff70b167092be8fdf",
        @Query("page") page: Int): Call<GetMoviesResponse>
}