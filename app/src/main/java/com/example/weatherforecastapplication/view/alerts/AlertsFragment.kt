package com.example.weatherforecastapplication.view.alerts

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.weatherforecastapplication.AlertBroadCastReceiver
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.data.database.AlertState
import com.example.weatherforecastapplication.databinding.FragmentAlertsBinding
import com.example.weatherforecastapplication.data.model.LocalAlert
import com.example.weatherforecastapplication.data.repo.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AlertsFragment : Fragment() {
    private var _binding: FragmentAlertsBinding? = null
    lateinit var alertFactory: AlertViewModelFactory
    lateinit var alertAdapter: AlertAdapter
    private val binding get() = _binding!!
    private val MY_ACTION = "MY_ACTION"
    lateinit var datePickerDialog :DatePickerDialog
    lateinit var alertBroadCastReceiver: AlertBroadCastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
//            var dialog = AddingAlertFragment()
//            fragmentManager?.let { it1 -> dialog.show(it1.beginTransaction(),"Add Alert") }

            findNavController()
                .navigate(R.id.action_nav_alerts_to_addingAlertFragment)
        }


        val lambda = { alert : LocalAlert ->
            alertViewModel.deleteAlert(alert)
        }

        lifecycleScope.launch {
            alertViewModel.alertList.collectLatest { result ->
                when (result) {
                    is AlertState.Success -> {
                        binding.pBar.visibility = View.GONE
                        binding.alertRecyclerView.visibility = View.VISIBLE
                        if (result.data.isEmpty()){
                            binding.noData.visibility = View.VISIBLE
                        }else{
                            binding.noData.visibility = View.GONE

                        }
                        alertAdapter = AlertAdapter(result.data , lambda)
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


       /* val intentFilter = IntentFilter(MY_ACTION)
        alertBroadCastReceiver = AlertBroadCastReceiver()
        registerReceiver(alertBroadCastReceiver , intentFilter)

        binding.notifyBtn.setOnClickListener {
            val intent = Intent()
            intent.action = MY_ACTION
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            sendBr
        }*/
        return root
        // Inflate the layout for this fragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}