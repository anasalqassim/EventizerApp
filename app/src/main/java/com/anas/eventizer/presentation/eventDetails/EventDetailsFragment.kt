package com.anas.eventizer.presentation.eventDetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.anas.eventizer.R
import com.anas.eventizer.databinding.FragmentEventDetailsBinding
import com.anas.eventizer.domain.models.EventLocation
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.models.Support
import com.anas.eventizer.presentation.eventDetails.rvAdapter.EventImagesAdapter
import com.anas.eventizer.presentation.eventDetails.rvAdapter.SupportsAdapter
import com.anas.eventizer.utils.Resource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.ktx.api.net.fetchPlaceRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

private const val TAG = "EventDetailsFragment"
@AndroidEntryPoint
class EventDetailsFragment : Fragment() {


    private val viewModel: EventDetailsViewModel by viewModels()

    private lateinit var _binding: FragmentEventDetailsBinding
    private val binding get() = _binding
    private lateinit var googleMap: GoogleMap
    private lateinit var eventLocation: EventLocation

    private val args:EventDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         **/
        this.googleMap = googleMap


        val fetchPlaceRequest = eventLocation.placeId?.let {
            FetchPlaceRequest.newInstance( it
                , listOf( Place.Field.LAT_LNG))
        }
        if (fetchPlaceRequest != null) {
            Log.d(TAG, "jgjhgjhgjh: ")
          placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener {
              val place = it.place
              place.latLng?.let {
                  Log.d(TAG, "jgjhgjhgjh: $it")

                  googleMap.moveCamera(CameraUpdateFactory
                      .newCameraPosition(CameraPosition.fromLatLngZoom(
                          it,15f
                      ))


                  )
                  MarkerOptions().position(
                      it
                  )

              }?.let {googleMap.addMarker(it)}

          }
        }

            eventLocation.latLng?.let {
                Log.d(TAG, "jgjhgjhgjh: $it")

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
                MarkerOptions().position(
                    it
                )

            }?.let {googleMap.addMarker(it)}





        googleMap.setOnPoiClickListener {
            googleMap.clear()


        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        initComponents()
        viewModel.getPersonalEventById(args.eventId)
        return _binding.root
    }

    private fun initComponents() {
        binding.supportsRv.apply {
            this.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            setHasFixedSize(true)
        }

        binding.eventImagesRv.apply {
            this.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            setHasFixedSize(true)
            PagerSnapHelper().attachToRecyclerView(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        lifecycleScope.launchWhenCreated {

                viewModel.personalEventStateFlow.collect { result ->
                    when (result) {
                        is Resource.Error -> {

                        }
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            val personalEvent = result.data!!
                            setPageData(personalEvent)

                            val mapsFragment =
                                childFragmentManager.findFragmentById(R.id.map_fragments) as SupportMapFragment?
                            mapsFragment?.getMapAsync(callback)

                        }

                    }

            }

        }


    }


    private fun setPageData(personalEvent: PersonalEvent){
        binding.detailsTv.text = personalEvent.eventCategory
        binding.EventLocationTv.text = "Riyadh"
        binding.eventTitleTv.text = personalEvent.eventName
        val supports = listOf(
            Support(supportName = "فرش جلسات", supportPics = listOf("https://png.pngitem.com/pimgs/s/506-5067022_sweet-shap-profile-placeholder-hd-png-download.png")),
            Support(supportName = "شوي عشاء", supportPics = listOf("https://png.pngitem.com/pimgs/s/506-5067022_sweet-shap-profile-placeholder-hd-png-download.png")))
        binding.supportsRv.adapter = SupportsAdapter(supports)
        binding.detailsTv.text = "رحلة عائلية الى حديقة الملك عبدالله"
       eventLocation = personalEvent.eventLocation

        binding.eventImagesRv.adapter = EventImagesAdapter(personalEvent.eventPicsUrls)
    }
}