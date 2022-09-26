package com.anas.eventizer.presentation.maps

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.anas.eventizer.R
import com.anas.eventizer.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment() {


    private var placeId : String? = null
    private var longitude : String = ""
    private var latitude : String = ""

    private lateinit var _binding:FragmentMapsBinding
    private val binding get() = _binding

    companion object{

        fun navigateToMapsFragment(fragment: Fragment){
            fragment.findNavController().navigate(R.id.mapsFragment)
        }

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


        googleMap.setOnPoiClickListener {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(it.latLng).title(it.name))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it.latLng))

            longitude = it.latLng.longitude.toString()
            latitude = it.latLng.latitude.toString()
            placeId = it.placeId
            binding.button.visibility = View.VISIBLE

        }


        googleMap.setOnMapClickListener {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(it))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))

            longitude = it.longitude.toString()
            latitude = it.latitude.toString()
            placeId = null
            binding.button.visibility = View.VISIBLE

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        binding.button.setOnClickListener {

            val navAction = MapsFragmentDirections
                .actionMapsFragmentToAddPublicEventFragment(
                    longitude = longitude,
                    latitude = latitude,
                    placeId = placeId
                )
            findNavController().navigate(navAction)

        }

    }
}