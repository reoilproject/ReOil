package com.example.reoil.view.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.reoil.databinding.FragmentSettingsBinding
import com.example.reoil.store.DashboardActivity
import com.example.reoil.utils.PreferencesHelper
import com.example.reoil.view.login.LoginActivity
import com.example.reoil.view.map.MapActivity
import com.example.reoil.view.notification.NotificationActivity
import com.example.reoil.view.register.RegisterPartnerFormActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferencesHelper = PreferencesHelper(requireContext())

        binding.btnNotification.setOnClickListener {
            val intent = Intent(context, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.containerGeneral.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.containerLocation.setOnClickListener {
            val intent = Intent(context, MapActivity::class.java)
            startActivity(intent)
        }

        binding.containerRegisterPartner.setOnClickListener {
            if (preferencesHelper.isPartnerRegistered()) {
                val intent = Intent(context, DashboardActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(context, RegisterPartnerFormActivity::class.java)
                startActivity(intent)
            }
        }

        binding.containerAboutus.setOnClickListener {
            val intent = Intent(context, AboutUsActivity::class.java)
            startActivity(intent)
        }

        binding.containerLogout.setOnClickListener {
            logout()
        }

        if (preferencesHelper.isPartnerRegistered()) {
            binding.tvRegisterpartner.text = "Dashboard"
        }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        preferencesHelper.clearLoginStatus()
        preferencesHelper.setPartnerRegistered(false) // Add this line
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
