package com.example.reoil.view.settings

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.reoil.R
import com.example.reoil.databinding.FragmentSettingsBinding
import com.example.reoil.utils.PreferencesHelper
import com.example.reoil.view.login.LoginActivity
import com.example.reoil.view.map.MapActivity
import com.example.reoil.view.notification.NotificationActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        binding.containerAboutus.setOnClickListener {
            val intent = Intent(context, AboutUsActivity::class.java)
            startActivity(intent)
        }

        binding.containerLocation.setOnClickListener {
            binding.imageViewProgress.visibility = View.VISIBLE
            val animator = AnimatorInflater.loadAnimator(context, R.animator.bounce_progress) as Animator
            animator.setTarget(binding.imageViewProgress)
            animator.start()

            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    val intent = Intent(context, MapActivity::class.java)
                    startActivity(intent)
                    binding.imageViewProgress.visibility = View.GONE
                }
            })
        }

        binding.containerLogout.setOnClickListener {
            logout()
        }

    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        preferencesHelper.clearLoginStatus()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
