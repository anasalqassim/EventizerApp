package com.anas.eventizer.presentation.addPublicE

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import androidx.core.content.res.ResourcesCompat.ThemeCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anas.eventizer.R
import com.anas.eventizer.databinding.FragmentAddPublicEventBinding
import com.anas.eventizer.utils.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


private const val TAG = "AddPublicEventFragment"
@AndroidEntryPoint
class AddPublicEventFragment : Fragment() {


    private val viewModel: AddPublicEventViewModel by viewModels()

    private lateinit var _binding:FragmentAddPublicEventBinding
    private val binding get() = _binding
    private var numOfTakenPics = 0

    private val imagesURIs = mutableListOf<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPublicEventBinding.inflate(inflater, container, false)


        return _binding.root
    }

    private val imagesLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        when (it.resultCode) {
            Activity.RESULT_OK -> {
                if (numOfTakenPics != 4){
                    if (checkCameraPermissions()){
                        takeImageFromCamera()
                        numOfTakenPics++
                    }
                }else{
                    numOfTakenPics = 0
                    val dataClips = it.data?.clipData
                    if (dataClips != null){
                        for ( uriIndex in 0..dataClips.itemCount){
                            imagesURIs +=  dataClips.getItemAt(uriIndex).uri
                        }
                    }
                }



            }
        }

    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
        if (result){
            takeImageFromCamera()
        }
    }
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
        if (result){
            chooseImageFromGallery()
        }
    }




    private fun chooseImageFromGallery(){
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        val intentChooser = Intent.createChooser(galleryIntent,"Choose image")
        imagesLauncher.launch(intentChooser)
    }

    private fun takeImageFromCamera(){

        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        imagesLauncher.launch(takePicture)
    }

    private fun checkCameraPermissions():Boolean
        =  ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun checkGalleryPermissions():Boolean
            =  ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chooseImagesBtn.setOnClickListener {


           context?.let { context ->
               MaterialAlertDialogBuilder(
                   context,
                   com.google.android.material.R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
               )

                   .setMessage("hgiuygigiugkhkjhjh")
                   .setNegativeButton("cam") { _, _ ->
                      if (checkCameraPermissions()){
                          takeImageFromCamera()
                      }else{
                          cameraLauncher.launch(Manifest.permission.CAMERA)
                      }
                   }
                   .setPositiveButton("Gall") { _, _ ->
                       if (checkGalleryPermissions()){
                           chooseImageFromGallery()
                       }else{
                           galleryLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                       }
                   }
                   .show()
           }

       }



        viewModel.addingStateFlow.onEach {
            when(it){
                is Resource.Error ->{
                    Log.d(TAG, "onViewCreated: ${it.massage}")
                }
                is Resource.Loading -> {
                    Log.d(TAG, "loading: ")
                }
                else -> {
                    Log.d(TAG, "onViewCreated: dsfsdjfns")
                }
            }
        }.launchIn(lifecycleScope)
    }


}