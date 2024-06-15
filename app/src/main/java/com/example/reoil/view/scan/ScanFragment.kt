package com.example.reoil.view.scan

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.reoil.R
import com.example.reoil.databinding.FragmentScanBinding
import com.example.reoil.utils.ImageClassifierHelper
import com.example.reoil.utils.getImageUri
import com.example.reoil.utils.reduceFileImage
import com.example.reoil.utils.uriToFile
import com.example.reoil.view.result.ResultActivity
import java.io.File

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private var getFile: File? = null
    private var currentImageUri: Uri? = null
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val view = binding.root

        imageClassifierHelper = ImageClassifierHelper()

        binding.btnCamera.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Scan Guide")
            builder.setMessage("Use a 600 ml mineral bottle as a container.\n" +
                    "Ensure good lightning for the scan.\n" +
                    "If an error occurs, please repeat the scan.\n" +
                    "Find an oil exchange location.\n" +
                    "Contact Store.").apply {
            }
            builder.setPositiveButton("OK") { dialog, which ->
                startCamera()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
            }

            val alertDialog = builder.create()
            alertDialog.show()

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.green))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.green))

            val dialogWindow = alertDialog.window
            dialogWindow?.setBackgroundDrawableResource(R.drawable.rounded_dialog)

            // Set font to Montserrat
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).typeface = ResourcesCompat.getFont(requireContext(), R.font.montserrat_semibold)
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).typeface = ResourcesCompat.getFont(requireContext(), R.font.montserrat_semibold)
        }

        binding.btnGallery.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Scan Guide")
            builder.setMessage("Use a 600 ml mineral bottle as a container.\n" +
                    "Ensure good lightning for the scan.\n" +
                    "If an error occurs, please repeat the scan.\n" +
                    "Find an oil exchange location.\n" +
                    "Contact Store.").apply {
            }
            builder.setPositiveButton("OK") { dialog, which ->
                openGallery()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
            }

            val alertDialog = builder.create()
            alertDialog.show()

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.green))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.green))

            val dialogWindow = alertDialog.window
            dialogWindow?.setBackgroundDrawableResource(R.drawable.rounded_dialog)

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).typeface = ResourcesCompat.getFont(requireContext(), R.font.montserrat_semibold)
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).typeface = ResourcesCompat.getFont(requireContext(), R.font.montserrat_semibold)
        }
        return view
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        launcherIntentGallery.launch(intent)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            getFile = uriToFile(currentImageUri!!, requireContext())
            getFile?.let {
                val reducedFile = reduceFileImage(it)
                classifyAndShowImage(Uri.fromFile(reducedFile))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            selectedImageUri?.let {
                getFile = uriToFile(it, requireContext())
                getFile?.let { file ->
                    val reducedFile = reduceFileImage(file)
                    classifyAndShowImage(Uri.fromFile(reducedFile))
                }
            }
        }
    }

    private fun classifyAndShowImage(imageUri: Uri) {
        getFile?.let { file ->
            showLoading(true)
            imageClassifierHelper.classifyImage(file) { result ->
                activity?.runOnUiThread {
                    showLoading(false)
                    val intent = Intent(requireContext(), ResultActivity::class.java)
                    intent.putExtra("imageUri", imageUri.toString())
                    intent.putExtra("result", result)
                    startActivity(intent)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
