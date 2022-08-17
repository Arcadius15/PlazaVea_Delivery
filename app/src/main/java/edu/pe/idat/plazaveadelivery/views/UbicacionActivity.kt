package edu.pe.idat.plazaveadelivery.views

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import edu.pe.idat.plazaveadelivery.R
import edu.pe.idat.plazaveadelivery.databinding.ActivityUbicacionBinding

class UbicacionActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityUbicacionBinding

    private lateinit var ultimaUbicacion: Location
    private lateinit var flpc: FusedLocationProviderClient

    private lateinit var puntoDestino: LatLng
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUbicacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        flpc = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        puntoDestino = LatLng(intent.getStringExtra("lat")!!.toDouble(),
            intent.getStringExtra("lng")!!.toDouble())

        mMap.addMarker(MarkerOptions()
            .position(puntoDestino)
            .title("Destino")
            .icon(BitmapDescriptorFactory.fromBitmap(reducirIcono("destinomarker",170,170))))!!.showInfoWindow()

        cargarMapa()
    }

    private fun cargarMapa() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),1)
            return
        }

        mMap.isMyLocationEnabled = true
        flpc.lastLocation.addOnSuccessListener(this) { l ->
            if (l != null) {
                ultimaUbicacion = l
                val puntoActual = LatLng(l.latitude, l.longitude)

                println("${l.latitude}")

                mMap.addMarker(MarkerOptions()
                    .position(puntoActual)
                    .title("Tu Ubicación"))!!.showInfoWindow()

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puntoActual, 16.0F))

                mMap.addPolyline(PolylineOptions()
                    .color(ContextCompat.getColor(this,R.color.granate_700))
                    .width(12F)
                    .add(puntoActual)
                    .add(puntoDestino))
            }
        }
    }

    fun reducirIcono(nombre: String, width: Int, height: Int): Bitmap {
        val imagen = BitmapFactory.decodeResource(
            resources, resources.getIdentifier(
                nombre, "drawable",
                packageName
            )
        )
        return Bitmap.createScaledBitmap(imagen, width, height, false)
    }

}