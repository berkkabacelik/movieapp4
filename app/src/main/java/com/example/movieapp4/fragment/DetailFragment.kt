package com.example.movieapp4.fragment



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.movieapp4.databinding.FragmentDetailBinding

const val MOVIE_BACKDROP = "extra_movie_backdrop"
const val MOVIE_POSTER = "extra_movie_poster"
const val MOVIE_TITLE = "extra_movie_title"
const val MOVIE_RATING = "extra_movie_rating"
const val MOVIE_RELEASE_DATE = "extra_movie_release_date"
const val MOVIE_OVERVIEW = "extra_movie_overview"

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var backdrop: ImageView
    private lateinit var poster: ImageView
    private lateinit var title: TextView
    private lateinit var rating: RatingBar
    private lateinit var releaseDate: TextView
    private lateinit var overview: TextView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val extras = arguments

        if (extras != null) {
            populateDetails(extras)
        } else {
            requireActivity().finish()
        }
    }

    private fun populateDetails(extras: Bundle) {
        extras.getString(MOVIE_BACKDROP)?.let { backdropPath ->
            Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/w1280$backdropPath")
                .transform(CenterCrop())
                .into(binding.movieBackdrop)
        }

        extras.getString(MOVIE_POSTER)?.let { posterPath ->
            Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/w342$posterPath")
                .transform(CenterCrop())
                .into(binding.moviePoster)
        }

        binding.movieTitle.text = extras.getString(MOVIE_TITLE, "")
        binding.movieRating.rating = extras.getFloat(MOVIE_RATING, 0f) / 2
        binding.movieReleaseDate.text = extras.getString(MOVIE_RELEASE_DATE, "")
        binding.movieOverview.text = extras.getString(MOVIE_OVERVIEW, "")
    }
}
