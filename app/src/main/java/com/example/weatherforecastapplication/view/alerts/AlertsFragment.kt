package com.example.weatherforecastapplication.view.alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.databinding.FragmentAlertsBinding


class AlertsFragment : Fragment() {
    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val favouriteViewModel =
            ViewModelProvider(this).get(AlertsViewModel::class.java)

        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textViewAlerts
        favouriteViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
        // Inflate the layout for this fragment
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}