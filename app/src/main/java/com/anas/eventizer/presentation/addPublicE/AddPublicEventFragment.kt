package com.anas.eventizer.presentation.addPublicE

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.anas.eventizer.R
import com.anas.eventizer.data.remote.dto.EventLocationDto
import com.anas.eventizer.data.remote.dto.PublicEventDto
import com.anas.eventizer.databinding.FragmentAddPublicEventBinding
import com.anas.eventizer.presentation.addPublicE.adapter.ImagesAdapter
import com.anas.eventizer.presentation.eventsList.EventsListFragment
import com.anas.eventizer.presentation.maps.MapsFragment
import com.anas.eventizer.utils.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Named


private const val TAG = "AddPublicEventFragment"
@AndroidEntryPoint
class AddPublicEventFragment : Fragment(),OnClickListener {

    private val viewModel: AddPublicEventViewModel by viewModels()

    private val navArgs:AddPublicEventFragmentArgs by navArgs()

    private var title:String = ""
    private var date:Calendar = Calendar.getInstance()
    private var category:String = ""
    private var categoryPos:Int = 0


    @Inject
    @Named("addPublicEventAdapter")
    lateinit var imagesAdapter: ImagesAdapter

    private lateinit var _binding:FragmentAddPublicEventBinding
    private val binding get() = _binding

    private var imagesURIs = mutableListOf<Uri>()

    companion object{
        fun navigateToAddPublicEventFragment(fragment: Fragment){
            fragment.findNavController().navigate(R.id.addPublicEventFragment)
        }
    }

    override fun onStart() {
        super.onStart()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPublicEventBinding.inflate(inflater, container, false)

        initComponents()

        setListeners()

        return _binding.root
    }

    private val imagesLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        when (it.resultCode) {
            Activity.RESULT_OK -> {


                    val dataClips = it.data?.clipData
                    if (dataClips != null){
                        for ( uriIndex in 0 until dataClips.itemCount){
                            imagesURIs +=  dataClips.getItemAt(uriIndex).uri
                        }
                    }
                    imagesAdapter.imageURIs = imagesURIs


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

        imagesLauncher.launch(takePicture, ActivityOptionsCompat.makeBasic())
    }

    private fun checkCameraPermissions():Boolean
        =  ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun checkGalleryPermissions():Boolean
            =  ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED

    private fun initComponents(){

        val argTitle = navArgs.title.toString()
        val argsImagesUri = navArgs.imagesUrIs

        title = if(argTitle != "-1") argTitle else ""
        binding.eventTxtInput.editText?.setText(title)
        date = Calendar.Builder().setInstant(navArgs.date).build()
        binding.calendarView.date = date.timeInMillis
        categoryPos = navArgs.categoryPos
        category = binding.categotySpinner.selectedItem.toString()
        binding.categotySpinner.setSelection(categoryPos)
        if (argsImagesUri != null){
            imagesURIs = argsImagesUri.toMutableList()
            imagesAdapter.imageURIs = imagesURIs
        }

        binding.imagesRv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            PagerSnapHelper().attachToRecyclerView(this)
            adapter = imagesAdapter
        }


    }

    private fun showDialog(context: Context){


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

    private fun setListeners(){
        binding.submitBtn.setOnClickListener(this)
        binding.openMapTv.setOnClickListener(this)

        binding.calendarView.setOnDateChangeListener { view, _, _, _ ->
            date = Calendar.Builder().setInstant(view.date).build()
        }
        binding.categotySpinner.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoryPos = position
                category = binding.categotySpinner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.eventTxtInput.editText?.addTextChangedListener {
            title = it.toString()
        }


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chooseImagesBtn.setOnClickListener {

            showDialog(requireContext())


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



    override fun onClick(v: View?) {
        when(v){
            binding.submitBtn ->{

                    val lng = navArgs.longitude.toDoubleOrNull()
                    val lat = navArgs.latitude.toDoubleOrNull()

                   val latLng =  if (lng != null && lat != null ) {
                        com.google.android.gms.maps.model.LatLng(lat,lng)
                    }else{
                       null
                   }
                    val eventLocationDto = EventLocationDto(latLng = latLng,placeId = navArgs.placeId)
                    val publicEventDto = PublicEventDto(
                        eventName = title,
                        eventDate = date.time,
                        eventCategory = this.category,
                        eventLocation = eventLocationDto
                    )

                    viewModel.addPublicEvent(publicEventDto,imagesURIs)

                    EventsListFragment.navigateToEventsListFragment(this)

            }

            binding.openMapTv ->{

                val action = AddPublicEventFragmentDirections.actionAddPublicEventFragmentToMapsFragment(
                   title = title,date = date.timeInMillis, imagesUrIs =  imagesURIs.toTypedArray(), categoryPos = categoryPos
                )
                findNavController().navigate(action)
            }
        }
    }





}