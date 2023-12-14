package com.example.Login_DB

import Pomodoro_timer
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.content.Context
import android.widget.EditText
import android.widget.Button
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.Login_DB.R


class SettingsFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var callback: Pomodoro_timer.PomodoroCallback // Define callback variable
    private lateinit var notesLayout: LinearLayout // Define notesLayout variable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences(
            "TimerPrefs",
            Context.MODE_PRIVATE
        )
        val editTextPomodoroDuration: EditText = view.findViewById(R.id.editTextPomodoroDuration)
        val editTextBreakDuration: EditText = view.findViewById(R.id.editTextBreakDuration)
        val buttonSave: Button = view.findViewById(R.id.btnSave)
        // Load saved timer durations or set default values
        val pomodoroDuration = sharedPreferences.getLong("PomodoroDuration", 25 * 60 * 1000)
        val breakDuration = sharedPreferences.getLong("BreakDuration", 5 * 60 * 1000)

        editTextPomodoroDuration.setText((pomodoroDuration / 1000).toString())
        editTextBreakDuration.setText((breakDuration / 1000).toString())

        buttonSave.setOnClickListener {
            val pomodoroDurationInput = editTextPomodoroDuration.text.toString().toLongOrNull()
            val breakDurationInput = editTextBreakDuration.text.toString().toLongOrNull()

            // Save entered durations to SharedPreferences
            pomodoroDurationInput?.let {
                saveDuration("PomodoroDuration", it * 1000)
            }

            breakDurationInput?.let {
                saveDuration("BreakDuration", it * 1000)
            }

            // Obtain an instance of Pomodoro_timer (assuming it's instantiated somewhere accessible)
            val pomodoroTimer = Pomodoro_timer(callback, notesLayout) // Ensure callback and notesLayout are defined or passed

            // Update the timer durations in Pomodoro_timer using setter methods
            pomodoroTimer.setPomodoroDuration(pomodoroDurationInput ?: pomodoroDuration)
            pomodoroTimer.setBreakDuration(breakDurationInput ?: breakDuration)
        }
    }

    private fun saveDuration(key: String, duration: Long) {
        sharedPreferences.edit().putLong(key, duration).apply()
    }

    // Setter methods for callback and notesLayout
    fun setCallback(callbackInstance: Pomodoro_timer.PomodoroCallback) {
        callback = callbackInstance
    }

    fun setNotesLayout(layout: LinearLayout) {
        notesLayout = layout
    }
}
