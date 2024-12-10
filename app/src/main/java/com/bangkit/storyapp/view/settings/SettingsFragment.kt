package com.bangkit.storyapp.view.settings

import android.content.Context
import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.storyapp.R
import com.bangkit.storyapp.databinding.FragmentSettingsBinding
import com.bangkit.storyapp.utils.LogoutCallback

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private var callback: LogoutCallback? = null

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSettingsBinding.bind(view)
        _binding = binding

//        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        setupAction()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as? LogoutCallback
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    private fun setupAction() {
        _binding?.ivLanguage?.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        _binding?.logoutButton?.setOnClickListener {
            callback?.onLogout()
        }
    }
}