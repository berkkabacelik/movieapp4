package com.example.movieapp4

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp4.R
import com.example.movieapp4.adapter.MoviesAdapter
import com.example.movieapp4.databinding.FragmentMainBinding
import com.example.movieapp4.fragment.DetailFragment
import com.example.movieapp4.fragment.MOVIE_BACKDROP
import com.example.movieapp4.fragment.MOVIE_OVERVIEW
import com.example.movieapp4.fragment.MOVIE_POSTER
import com.example.movieapp4.fragment.MOVIE_RATING
import com.example.movieapp4.fragment.MOVIE_RELEASE_DATE
import com.example.movieapp4.fragment.MOVIE_TITLE
import com.example.movieapp4.model.Movie
import com.example.movieapp4.service.MovieRepository

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private lateinit var popularMoviesAdapter: MoviesAdapter
    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager


    private lateinit var topRatedMoviesAdapter: MoviesAdapter
    private lateinit var topRatedMoviesLayoutMgr: LinearLayoutManager

    private lateinit var upcomingMoviesAdapter: MoviesAdapter
    private lateinit var upcomingMoviesLayoutMgr: LinearLayoutManager

    private var upcomingMoviesPage = 1
    private var topRatedMoviesPage = 1
    private var popularMoviesPage = 1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPopularMovies()
        setupTopRatedMovies()
        setupUpcomingMovies()

        getPopularMovies()
        getTopRatedMovies()
        getUpcomingMovies()
    }

    private fun setupPopularMovies() {
        binding.popularMovies.apply {
            popularMoviesLayoutMgr = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = popularMoviesLayoutMgr
            popularMoviesAdapter = MoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
            adapter = popularMoviesAdapter
        }
    }

    private fun setupTopRatedMovies() {
        binding.topRatedMovies.apply {
            topRatedMoviesLayoutMgr = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = topRatedMoviesLayoutMgr
            topRatedMoviesAdapter = MoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
            adapter = topRatedMoviesAdapter
        }
    }

    private fun setupUpcomingMovies() {
        binding.upcomingMovies.apply {
            upcomingMoviesLayoutMgr = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = upcomingMoviesLayoutMgr
            upcomingMoviesAdapter = MoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
            adapter = upcomingMoviesAdapter

            attachUpcomingMoviesOnScrollListener()
        }
    }

    private fun getPopularMovies() {
        MovieRepository.getPopularMovies(popularMoviesPage, ::onPopularMoviesFetched, ::onError)
    }

    private fun getTopRatedMovies() {
        MovieRepository.getTopRatedMovies(topRatedMoviesPage, ::onTopRatedMoviesFetched, ::onError)
    }

    private fun getUpcomingMovies() {
        MovieRepository.getUpcomingMovies(upcomingMoviesPage, ::onUpcomingMoviesFetched, ::onError)
    }


    // Popüler filmler alındığında çağrılır
    private fun onPopularMoviesFetched(movies: List<Movie>) {
        popularMoviesAdapter.appendMovies(movies)
        attachPopularMoviesOnScrollListener()
    }

    private fun onTopRatedMoviesFetched(movies: List<Movie>) {
        topRatedMoviesAdapter.appendMovies(movies)
        attachTopRatedMoviesOnScrollListener()
    }

    private fun onUpcomingMoviesFetched(movies: List<Movie>) {
        upcomingMoviesAdapter.appendMovies(movies)
        attachUpcomingMoviesOnScrollListener()
    }

    // Popüler filmler için sonsuz kaydırma işleyicisini ekler
    private fun attachPopularMoviesOnScrollListener() {
        binding.popularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // Toplam öğe sayısını, görünen öğe sayısını ve ilk görünen öğeyi alır
                val totalItemCount = popularMoviesLayoutMgr.itemCount
                val visibleItemCount = popularMoviesLayoutMgr.childCount
                val firstVisibleItem = popularMoviesLayoutMgr.findFirstVisibleItemPosition()
                // Kullanıcı listenin yarısına geldiğinde yeni verileri alır
                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.popularMovies.removeOnScrollListener(this)
                    popularMoviesPage++
                    getPopularMovies()
                }
            }
        })
    }
    private fun attachTopRatedMoviesOnScrollListener() {
        binding.topRatedMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = topRatedMoviesLayoutMgr.itemCount
                val visibleItemCount = topRatedMoviesLayoutMgr.childCount
                val firstVisibleItem = topRatedMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.topRatedMovies.removeOnScrollListener(this)
                    topRatedMoviesPage++
                    getTopRatedMovies()
                }
            }
        })
    }
    private fun attachUpcomingMoviesOnScrollListener() {
        binding.upcomingMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = upcomingMoviesLayoutMgr.itemCount
                val visibleItemCount = upcomingMoviesLayoutMgr.childCount
                val firstVisibleItem = upcomingMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.upcomingMovies.removeOnScrollListener(this)
                    upcomingMoviesPage++
                    getUpcomingMovies()
                }
            }
        })
    }
    // Bir film detaylarını göstermek için Detay Fragmentine geçiş yapar
    private fun showMovieDetails(movie: Movie) {
        val args = bundleOf(
            MOVIE_BACKDROP to movie.backdropPath,
            MOVIE_POSTER to movie.posterPath,
            MOVIE_TITLE to movie.title,
            MOVIE_RATING to movie.rating,
            MOVIE_RELEASE_DATE to movie.releaseDate,
            MOVIE_OVERVIEW to movie.overview
        )
        findNavController().navigate(R.id.detayGecis, args)
    }




    private fun onError() {
        Toast.makeText(requireContext(), getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }
}
