package mx.edu.cetis108.cetis1084am_15325061080038_app004;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //declaro dos variables para latitud y longitud de tipo double
    double latitud = 0;
    double longitud = 0;
    private GoogleMap mMap;
    //creo un Marker llamado marcador para utilizar sus propiedades al crear nuevos Markers
    private Marker marcador;
    //Defino métodos para actualizaciones del location
    private LocationListener locationListener = new LocationListener() {
        @Override
        //Cuando la ubicación del location cambie actualice la ubicación
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        miUbicacion();//Mando llamar al método para que cuando suceda onMapReady me cargue la ubicación
       /* LatLng miCasa = new LatLng(25.589200, -108.47346);
        mMap.addMarker(new MarkerOptions().position(miCasa).title("15325061080038 Félix López Alfonso Neil").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miCasa, 18));*/
    }

    //Creo un método para agregar un nuevo marcador al mapa, recibiendo como parámetros latitud y longitud de tipo double.
    private void agregarMarcador(double pLatitud, double pLongitud) {
        LatLng puntoCoordenado = new LatLng(pLatitud, pLongitud); //Instancío un nuevo objeto de la clase LatLng para crear un punto coordenado
        CameraUpdate ubicacion = CameraUpdateFactory.newLatLngZoom(puntoCoordenado, 18); //Creo un nuevo CameraUpdate para establecer como y donde quiero que se vea mi marcador
        if (marcador != null) //Si el marcador es NULL
            marcador.remove(); //Elimino marcador
        marcador = mMap.addMarker(new MarkerOptions().title("Mi posición actual").position(puntoCoordenado).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        mMap.animateCamera(ubicacion); //El mapa me lleva a la posición del CameraUpadte 'ubicacion'
    }

    //Creo un método para actualizar la ubicación
    private void actualizarUbicacion(Location location) {
        if (location != null) {
            latitud = location.getLatitude(); //Asigno a latitud el valor de la latitud de location
            longitud = location.getLongitude(); //Asigno a longitud el valor de la longitud de location
            agregarMarcador(latitud, longitud); //Mando llamar al método agregarMarcador mandandole como parámetros latitud y longitud
        }
    }

    //Creo un método para mandar llamar al servicio de LOCATION
    private void miUbicacion() {
        //Esta condición es para verificar que el dispositivo tenga permiso de acceder a la ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //Mandamos llamar al proveedor de GPS
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0, locationListener); //Estará dando actualizaciones de ubicación
    }

}
