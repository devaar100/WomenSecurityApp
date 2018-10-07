package aarnav100.developer.indiragandhisafety;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker;
    private TextView placeName;
    private ArrayList<Review> reviews = new ArrayList<>();
    private boolean on = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        placeName = findViewById(R.id.placeName);

        final MediaPlayer mp;
        mp = MediaPlayer.create(MapsActivity.this, R.raw.siren);

        findViewById(R.id.review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MapsActivity.this);
                View  dialogView =((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_rate, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                ((Button)dialogView.findViewById(R.id.submit)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        Toast.makeText(MapsActivity.this, "Thanks for your review", Toast.LENGTH_LONG).show();
                    }
                });
                ((ListView)dialogView.findViewById(R.id.reviewlist)).setAdapter(new ReviewAdapter());
                alertDialog.show();
            }
        });

        findViewById(R.id.sound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!on) {
                    Toast.makeText(MapsActivity.this, "Details sent to emergency contacts", Toast.LENGTH_SHORT).show();
                    mp.start();
                } else{
                    mp.stop();
                }
                on = !on;
            }
        });

        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Latitude : 28.66542159\nLongitude : 77.23240508\nTravelling towards : This direction");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "I AM NOT SAFE HELP ME");
                startActivity(Intent.createChooser(shareIntent, "Share..."));
            }
        });

        findViewById(R.id.people).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this,PeopleActivity.class));
            }
        });

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorMain));

        reviews.add(new Review("Anjana","I dont feel safe here"));
        reviews.add(new Review("Ruhi","I dont feel safe here"));
        reviews.add(new Review("Neelanjana","I dont feel safe here"));

        Intent i=new Intent(MapsActivity.this, MyService.class);
        startService(i);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(null);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng igdtu = new LatLng(28.66542159, 77.23240508);
        placeName.setText("IGDTUW, Kashmere Gate");
        marker = googleMap.addMarker(new MarkerOptions().position(igdtu).title("Safety Score : 4.5"));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(igdtu).tilt(45).zoom(17).build()),2500,null);
        marker.showInfoWindow();
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng ll) {
                marker.remove();
                marker = googleMap.addMarker(new MarkerOptions().position(ll).title("Safety Score : "+getScore()));
                marker.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
                placeName.setText(getPlaceFromCoord(ll));
            }
        });

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    public String getPlaceFromCoord(LatLng ll){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(ll.latitude, ll.longitude, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                return listAddresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "There was an error", Toast.LENGTH_SHORT).show();
        }
        return "Cant Get Place Name";
    }

    public double getScore(){
        Random generator = new Random();
        double number = generator.nextDouble() * 3;
        number = Math.round(number * 10.0) / 10.0;
        return number+2;
    }

    public class Review{
        String name,text;

        public Review(String name, String text) {
            this.name = name;
            this.text = text;
        }
    }

    public class ReviewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Review getItem(int i) {
            return reviews.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Review rv = getItem(i);
            View v;
            LayoutInflater li=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=li.inflate(R.layout.layout_review,null);
            ((TextView)view.findViewById(R.id.name)).setText(rv.name);
            ((TextView)view.findViewById(R.id.rtext)).setText(rv.text);
            return view;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i=new Intent(this, MyService.class);
        stopService(i);
    }

}
