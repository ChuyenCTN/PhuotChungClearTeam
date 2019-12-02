package com.clearteam.phuotnhom.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.model.ServiceAround;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.network.RetrofitClient;
import com.clearteam.phuotnhom.network.api.APIService;
import com.clearteam.phuotnhom.notification.Token;
import com.clearteam.phuotnhom.ui.map.model.PlaceResponse;
import com.clearteam.phuotnhom.utils.Const;
import com.clearteam.phuotnhom.utils.DialogServiceAround;
import com.clearteam.phuotnhom.utils.DialogServiceAroundMemberOnline;
import com.clearteam.phuotnhom.utils.GpsUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private ImageView imgCurentLocation;

    private GoogleMap mMap;
    private List<ServiceAround> mServiceAroundList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();

    private static MapFragment INSTANCE;
    private LinearLayout mLiServiceAround, mLiFriend;


    public static MapFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MapFragment();
        }
        return INSTANCE;
    }

    //    place

    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentLocationmMarker;
    private Marker placeMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 10000;

    private String mContentPlace = "";


    //    my
    private double mLatitude = 0.0;

    private double mLongitude = 0.0;

    private Geocoder mGeocoder;

    private FusedLocationProviderClient mFusedLocationClient;

    private LocationCallback locationCallback;

    private boolean isContinue = false;

    private boolean isGPS = false;

    private PlaceResponse mPlaceResponse;

    private String placeKey = "AIzaSyB9RoG4vLRQ1GqZ9XDJSeyfAa-PGMuLnxA";

    private Retrofit mRetrofit;
    private APIService mApiService;

    //firebase
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatustBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        setHasOptionsMenu(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        mRetrofit = RetrofitClient.getRetrofitClient();
        mApiService = mRetrofit.create(APIService.class);

        imgCurentLocation = (ImageView) view.findViewById(R.id.img_curent_location);
        mLiServiceAround = view.findViewById(R.id.line_service_around);
        mLiFriend = view.findViewById(R.id.line_friend);

        mLiServiceAround.setOnClickListener(this);
        mLiFriend.setOnClickListener(this);
        imgCurentLocation.setOnClickListener(this::onClick);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initLocation();

        getAutocompletePlace();

        initfirebase();

//        initRequestLocation();

        return view;
    }

    private void initRequestLocation() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                getCurrentLocation();
            }
        }, 0, Const.TIME_REQUEST, TimeUnit.SECONDS);

    }

    private void initfirebase() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        updateToken(FirebaseInstanceId.getInstance().getToken());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
    }

    public void getAutocompletePlace() {

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyAtpPlLN4Y-NrUsrg48F9_oAJnbL1B0tF4");
        }
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplate);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setCountry("VN");
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);

        ImageView searchIcon = (ImageView) ((LinearLayout) autocompleteFragment.getView()).getChildAt(0);
        EditText edSeach = (EditText) ((LinearLayout) autocompleteFragment.getView()).getChildAt(1);
        TextView tvSeach = (TextView) ((LinearLayout) autocompleteFragment.getView()).getChildAt(1);
        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_seach_map));
        searchIcon.setPadding(50, 0, 0, 0);
        edSeach.setTextSize(18);
        tvSeach.setTextSize(18);
        //   tvSeach.getResources().getColor(R.color.text_seach);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                String location = place.getName();
                String address = place.getAddress();


                List<Address> addressList = null;
                MarkerOptions markerOptions = new MarkerOptions();

                if (!location.equals("")) {
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 5);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addressList != null) {
                        for (int i = 0; i < addressList.size(); i++) {
                            Address myAddress = addressList.get(i);
                            LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                            markerOptions.position(latLng);
                            markerOptions.title(location);
                            markerOptions.draggable(true);
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map));
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.addMarker(markerOptions);
                        }
                    }
                }
            }

            @Override
            public void onError(Status status) {
                Log.i("AAA", "An error occurred: " + status);
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.line_service_around:
                DialogServiceAround dialogServiceAround = new DialogServiceAround(mServiceAroundList, new DialogServiceAround.IChoose() {
                    @Override
                    public void onChoose(ServiceAround serviceAround) {
                        Toast.makeText(getContext(), serviceAround.getTvName() + "\n", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClick(String nameService, String title) {
                        mContentPlace = title;
                        Toast.makeText(getContext(), "Đang tìm " + title + " gần đây", Toast.LENGTH_SHORT).show();
                        getNearbyPlace(String.valueOf(mLatitude + "," + mLongitude), nameService);
                    }
                });
                dialogServiceAround.show(getChildFragmentManager(), "ADAS");

                break;
            case R.id.line_friend:
                DialogServiceAroundMemberOnline dialogServiceAroundMemberOnline = new DialogServiceAroundMemberOnline(userList, true, new DialogServiceAroundMemberOnline.IChoose() {
                    @Override
                    public void onLocationClick(User user) {
                        Toast.makeText(getContext(), user.getUsername() + "   location", Toast.LENGTH_SHORT).show();
                        showMarkerNearby(Double.parseDouble(user.getLatitude()), Double.parseDouble(user.getLongitude()));

                    }

                    @Override
                    public void onMessageClick(User user) {
                        Toast.makeText(getContext(), user.getUsername() + "   message", Toast.LENGTH_SHORT).show();
                    }
                });
                dialogServiceAroundMemberOnline.show(getChildFragmentManager(), "ADAD");
                break;
            case R.id.img_curent_location:

//                moveCamMy = true;
//                sendRequest();
//                getCurrentLocation();
                break;
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }


    protected synchronized void bulidGoogleApiClient() {
        client = new GoogleApiClient.Builder(getContext()).addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) getContext()).addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) getContext()).addApi(LocationServices.API).build();
        client.connect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        lastlocation = location;
        showMarker(mLatitude, mLongitude);
        if (client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }


    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Const.LOCATION_REQUEST);

        } else {
            if (isContinue) {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                    if (location != null) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                        showMarker(mLatitude, mLongitude);

                    } else {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                });
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (isContinue) {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    } else {
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                            if (location != null) {
                                mLatitude = location.getLatitude();
                                mLongitude = location.getLongitude();

                            } else {
                                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            bulidGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
        }

    }


    public void showMarker(double latitude, double longitude) {
        try {

//            cai nay de update vi tri cua minh len firebase
            updateLatlng(String.valueOf(latitude), String.valueOf(longitude));
            Log.d("zxcvbnm,", String.valueOf(latitude) + "\n" + String.valueOf(longitude));
            Log.d("zxcvbnm,",latitude+"");
            Log.d("zxcvbnm,",longitude+"");

            LatLng latLng = new LatLng(latitude, longitude);
            mGeocoder = new Geocoder(getContext());
            List<Address> addresses = mGeocoder.getFromLocation(latitude, longitude, 1);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(addresses.get(0).getAdminArea()).snippet(addresses.get(0).getAddressLine(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            currentLocationmMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String position = marker.getId();
                String position1 = marker.getSnippet();
                String position2 = String.valueOf(marker.getZIndex());
                String position3 = marker.getTitle();

//                Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), position1 + "", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), position2 + "", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), position3 + "", Toast.LENGTH_SHORT).show();


                return false;
            }
        });
/*
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
*/
    }

    private void changeStatustBar() {
        Window window = getActivity().getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.bg_tab));
        }
    }

    private void initLocation() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 200); // 5 seconds

        new GpsUtils(getContext()).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
                getCurrentLocation();
            }
        });
    }

    private void getNearbyPlace(String location, String type) {
        Call<PlaceResponse> call = mApiService.getPlaceNearby(location, Const.RADIUS_PLACE, type, "", placeKey);
        call.enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                if (response.code() == 200) {
                    if (response != null) {
                        mPlaceResponse = new PlaceResponse();
                        mPlaceResponse = (PlaceResponse) response.body();
                        if (mPlaceResponse.getStatus().equalsIgnoreCase(Const.STATUS_OK)) {
                            Log.d("zxcvbnm,.", mPlaceResponse.getResults().size() + "");
                            for (int i = 0; i < mPlaceResponse.getResults().size(); i++) {
                                double lon = mPlaceResponse.getResults().get(i).getGeometry().getViewport().getNortheast().getLng();
                                double lat = mPlaceResponse.getResults().get(i).getGeometry().getViewport().getNortheast().getLat();
                                showMarkerNearby(lat, lon);
                            }
                        } else {
                            Toast.makeText(getContext(), "Không tìm thấy " + mContentPlace + " gần đây", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
                Log.e("NearbyPlace: ", t.getMessage() + "");
                Log.e("NearbyPlace: ", t.getLocalizedMessage() + "");
            }
        });

    }


    public void showMarkerNearby(double latitude, double longitude) {
        try {

            LatLng latLng = new LatLng(latitude, longitude);
            mGeocoder = new Geocoder(getContext());
            List<Address> addresses = mGeocoder.getFromLocation(latitude, longitude, 1);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(addresses.get(0).getAdminArea()).snippet(addresses.get(0).getAddressLine(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(11));


        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String position = marker.getId();
                String position1 = marker.getSnippet();
                String position2 = String.valueOf(marker.getZIndex());
                String position3 = marker.getTitle();

//                Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), position1 + "", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), position2 + "", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), position3 + "", Toast.LENGTH_SHORT).show();


                return false;
            }
        });
/*
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
*/
    }

//    private void updateData(final String toString) {
//        final String name = edName.getText().toString();
//        final String email = edEmail.getText().toString();
//        final String address = edAddress.getText().toString();
//        final String numberPhone = edNumberPhone.getText().toString();
//        final String numberPhoneRelatives = ed_number_phone_relatives.getText().toString();
//        final String sexx = edSex.getText().toString();
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference(mStoragePath);
//        Query query = reference.orderByChild("username").equalTo(name1);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    dataSnapshot1.getRef().child("username").setValue(name);
//                    dataSnapshot1.getRef().child("email").setValue(email);
//                    dataSnapshot1.getRef().child("imageURL").setValue(toString);
//                    dataSnapshot1.getRef().child("address").setValue(address);
//                    dataSnapshot1.getRef().child("numberPhone").setValue(numberPhone);
//                    dataSnapshot1.getRef().child("numberPhoneRelatives").setValue(numberPhoneRelatives);
//                    dataSnapshot1.getRef().child("sex").setValue(sexx);
//                }
//                mProgressDialog.dismiss();
//                Toast.makeText(EditInformationActivity.this, "sửa thành công", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(EditInformationActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }

    private void updateLatlng(String latitude, String longitude) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("latitude", latitude);
        hashMap.put("longitude", longitude);
        reference.updateChildren(hashMap);
    }
}
