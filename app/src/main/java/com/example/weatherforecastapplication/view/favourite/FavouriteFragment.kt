package com.example.weatherforecastapplication.view.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.data.database.RoomState
import com.example.weatherforecastapplication.databinding.FragmentFavouriteBinding
import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.repo.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment() {
    lateinit var favouriteFactory: FavouriteViewModelFactory
    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var favouriteAdapter: FavouriteAdapter
    private var _binding: FragmentFavouriteBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val repository = Repository.getInstance(requireActivity().application)
        favouriteFactory = FavouriteViewModelFactory(repository)

        favouriteViewModel =
            ViewModelProvider(this, favouriteFactory).get(FavouriteViewModel::class.java)

        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.fabFav.setOnClickListener {
            Navigation.findNavController(root).navigate(R.id.action_nav_favourite_to_mapFragment)
        }
        val lambda = { favouritePlace : FavouritePlace ->
            favouriteViewModel.getFavouriteWeather(favouritePlace)
            Toast.makeText(requireContext(), "$favouritePlace", Toast.LENGTH_LONG).show()
            val bundle = Bundle()
            bundle.putSerializable("favorite",favouritePlace)
            Navigation.findNavController(root)
                .navigate(R.id.action_nav_favourite_to_favouritePlaceFragment,bundle)
        }
        val lambda2 = { favouritePlace : FavouritePlace ->
            favouriteViewModel.deleteFavouritePlace(favouritePlace)
        }

        lifecycleScope.launch {
            favouriteViewModel.favouritePlaceList.collectLatest { result ->
                when (result) {
                    is RoomState.Success -> {
                        binding.pBar.visibility = View.GONE
                        binding.favouriteRecyclerView.visibility = View.VISIBLE
                        if (result.data.isEmpty()){
                            binding.noData.visibility = View.VISIBLE
                        }else{
                            binding.noData.visibility = View.GONE

                        }
                        favouriteAdapter = FavouriteAdapter(result.data , lambda,lambda2)
                        binding.favouriteRecyclerView.adapter = favouriteAdapter

                    }
                    is RoomState.Failure -> {
                        binding.pBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "${result.msg}", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        binding.pBar.visibility = View.VISIBLE
                        delay(1000)
                        binding.favouriteRecyclerView.visibility = View.GONE
                    }
                }
            }
        }
        return root
    }



}