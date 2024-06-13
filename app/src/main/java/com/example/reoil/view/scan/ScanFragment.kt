package com.example.reoil.view.scan

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
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

        imageClassifierHelper = ImageClassifierHelper(requireContext())

        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        binding.btnGallery.setOnClickListener {
            openGallery()
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
        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val result = imageClassifierHelper.classifyImage(bitmap)

        val intent = Intent(requireContext(), ResultActivity::class.java)
        intent.putExtra("imageUri", imageUri.toString())
        intent.putExtra("result", result)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
