package com.example.weatherforecastapplication.view.alerts

import android.app.*
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentAddingAlertBinding
import com.example.weatherforecastapplication.data.model.LocalAlert
import com.example.weatherforecastapplication.data.repo.Repository
import java.util.concurrent.TimeUnit

class AddingAlertFragment : DialogFragment() {

    private var _binding: FragmentAddingAlertBinding? = null
    private val binding get() = _binding!!
    lateinit var fromDatePickerDialog: DatePickerDialog
    lateinit var toDatePickerDialog: DatePickerDialog
    lateinit var alertFactory: AlertViewModelFactory
    var start : Long = 0
    var end : Long = 0
    var time : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_WeatherForecastApplication_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val repository = Repository.getInstance(requireActivity().application)
        alertFactory = AlertViewModelFactory(repository)

        val alertViewModel =
            ViewModelProvider(this , alertFactory )[AlertsViewModel::class.java]
        _binding = FragmentAddingAlertBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initFromDatePicker()
        initToDatePicker()

        binding.fromDate.text = getFromDate()
        binding.toDate.text = getToDate()

        binding.fromDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(requireContext(),{ view, year, monthOfYear, dayOfMonth ->
                binding.fromDate.text =makeDateString(dayOfMonth, monthOfYear+1, year)
                val toDate = "$dayOfMonth-${monthOfYear+1}-$year"
                start = Utility.dateToLong(toDate)
                },
                year,
                month,
                day
            )
            val dp: DatePicker = datePickerDialog.getDatePicker()
            dp.minDate = c.timeInMillis
            dp.setMinDate(System.currentTimeMillis() - 1000)
            datePickerDialog.show()
        }
        binding.toDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(requireContext(),{ view, year, monthOfYear, dayOfMonth ->
                binding.toDate.text =makeDateString(dayOfMonth, monthOfYear+1, year)
                val toDate = "$dayOfMonth-${monthOfYear+1}-$year"
                end = Utility.dateToLong(toDate)
            },
                year,
                month,
                day
            )
            val dp: DatePicker = datePickerDialog.getDatePicker()
            dp.minDate = c.timeInMillis
            dp.setMinDate(System.currentTimeMillis() - 1000)
            datePickerDialog.show()
        }
        binding.whenTime.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val startHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val startMinute = currentTime.get(Calendar.MINUTE)

            TimePickerDialog(requireContext(), { view, hourOfDay, minute ->
                time = (TimeUnit.MINUTES.toSeconds(minute.toLong()) + TimeUnit.HOURS.toSeconds(hourOfDay.toLong()))
                time = time.minus(3600L * 2)
                binding.whenTime.text = "$hourOfDay : $minute "

            }, startHour, startMinute, false).show()
        }
        binding.wherePlace.setOnClickListener {
            findNavController()
                .navigate(R.id.alertMapFragment)
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("cityName")?.observe(
            viewLifecycleOwner) { result ->
            binding.wherePlace.text=result
        }
        binding.savaBtn.setOnClickListener {
            alertViewModel.insertAlert(
                LocalAlert(
                countryName = binding.wherePlace.text.toString() ,
                time = time,
                startDate = start,
                endDate = end
            )
            )
            NavHostFragment.findNavController(this)
                .popBackStack()
        }
        return root
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return getMonthFormat(month) + ", " + day + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        if (month == 1) return "Jan"
        if (month == 2) return "Feb"
        if (month == 3) return "Mar"
        if (month == 4) return "Apr"
        if (month == 5) return "May"
        if (month == 6) return "Jun"
        if (month == 7) return "Jul"
        if (month == 8) return "Aug"
        if (month == 9) return "Sep"
        if (month == 10) return "Oct"
        if (month == 11) return "Nov"
        return if (month == 12) "Dec" else "Jan"
    }

    private fun getFromDate(): String? {
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        var month: Int = cal.get(Calendar.MONTH)
        month += 1
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        return makeDateString(day, month, year)
    }

    private fun getToDate(): String? {
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        var month: Int = cal.get(Calendar.MONTH)
        month += 1
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        return makeDateString(day, month, year)
    }

    private fun initToDatePicker() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                var month = month
                month += 1
                val date = makeDateString(day, month, year)
                binding.toDate.text = date
            }
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        val style: Int = AlertDialog.THEME_HOLO_LIGHT
        toDatePickerDialog =
            DatePickerDialog(requireContext(), style, dateSetListener, year, month, day)
    }

    private fun initFromDatePicker() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                var month = month
                month += 1
                val date = makeDateString(day, month, year)
                binding.fromDate.text = date
            }
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        val style: Int = AlertDialog.THEME_HOLO_LIGHT
        fromDatePickerDialog =
            DatePickerDialog(requireContext(), style, dateSetListener, year, month, day)
    }


}