package com.example.weatherforecastapplication.view.alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.WorkManager
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.data.database.AlertState
import com.example.weatherforecastapplication.data.model.AlertModel
import com.example.weatherforecastapplication.databinding.FragmentAlertsBinding
import com.example.weatherforecastapplication.data.repo.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AlertsFragment : Fragment() {
    private var _binding: FragmentAlertsBinding? = null
    lateinit var alertFactory: AlertViewModelFactory
    lateinit var alertAdapter: AlertAdapter
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val repository = Repository.getInstance(requireActivity().application)
        alertFactory = AlertViewModelFactory(repository)

        val alertViewModel =
            ViewModelProvider(this , alertFactory )[AlertsViewModel::class.java]

        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabAlert.setOnClickListener {

            findNavController()
                .navigate(R.id.action_nav_alerts_to_addingAlertFragment)
        }
        lifecycleScope.launch {
            alertViewModel.alertList.collectLatest { result ->
                when (result) {
                    is AlertState.Success -> {
                        binding.pBar.visibility = View.GONE
                        binding.alertRecyclerView.visibility = View.VISIBLE
                        if (result.data.isEmpty()){
                            binding.noData.visibility = View.VISIBLE
                            binding.alert.visibility = View.GONE
                        }else{
                            binding.noData.visibility = View.GONE
                            binding.alert.visibility = View.VISIBLE
                        }
                        alertAdapter = AlertAdapter(result.data ,requireContext()){
                                alert : AlertModel ->
                            alertViewModel.deleteAlert(alert)
                            WorkManager.getInstance().cancelAllWorkByTag("${alert.id}")
                        }
                        binding.alertRecyclerView.adapter = alertAdapter

                    }
                    is AlertState.Failure -> {
                        binding.pBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "${result.msg}", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        binding.pBar.visibility = View.VISIBLE
                        delay(1000)
                        binding.alertRecyclerView.visibility = View.GONE
                    }
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}