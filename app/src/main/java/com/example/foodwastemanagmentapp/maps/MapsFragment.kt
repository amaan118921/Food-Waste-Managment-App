package com.example.foodwastemanagmentapp.maps

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private val navArgs: MapsFragmentArgs by navArgs()
    private lateinit var binding: FragmentMapsBinding
    private val callback = OnMapReadyCallback { googleMap ->

        val location = LatLng(lat, lon)
        googleMap.addMarker(MarkerOptions().position(location).title("Marker in Sydney").draggable(true))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))

//        googleMap.setOnMarkerDragListener(object: GoogleMap.OnMarkerDragListener {
//            override fun onMarkerDragStart(p0: Marker) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onMarkerDrag(p0: Marker) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onMarkerDragEnd(marker: Marker) {
//                val res = marker.position
//                lat = 41.21
//                lon = 174.0
//            }
//
//        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lat = navArgs.lat.toDouble()
        lon = navArgs.lon.toDouble()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}