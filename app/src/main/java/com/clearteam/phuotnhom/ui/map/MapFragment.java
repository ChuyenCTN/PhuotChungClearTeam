package com.clearteam.phuotnhom.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.clearteam.phuotnhom.model.direction.DirectionFinder;
import com.clearteam.phuotnhom.model.direction.DirectionFinderListener;
import com.clearteam.phuotnhom.model.direction.Route;
import com.clearteam.phuotnhom.network.RetrofitClient;
import com.clearteam.phuotnhom.network.api.APIService;
import com.clearteam.phuotnhom.notification.Token;
import com.clearteam.phuotnhom.ui.map.model.PlaceResponse;
import com.clearteam.phuotnhom.utils.CommonUtils;
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
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.clearteam.phuotnhom.utils.Const.PATTERN_DASH_LENGTH_PX;
import static com.clearteam.phuotnhom.utils.Const.PATTERN_GAP_LENGTH_PX;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, DirectionFinderListener, GoogleMap.OnMarkerClickListener {

    private ImageView imgCurentLocation;
    private ImageView imgTypeMap;


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
    private String mNamePlace = "";

    private MarkerOptions[] markers;

    private Marker markerNearby;

    MarkerOptions markerOptions;

    public static String address = "";


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
    private String autoPlaceKey = "AIzaSyAtpPlLN4Y-NrUsrg48F9_oAJnbL1B0tF4";

    private Retrofit mRetrofit;
    private APIService mApiService;

    private boolean moveCamFriend = true;
    private boolean moveCamNearby = true;
    private boolean moveCamMy = true;

    //firebase
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;


    //    direction
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();


    private String modeDirection = "";

    public static final PatternItem DOT = new Dot();
    public static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    public static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    public static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    private List<User> listUser;

    //    dialog
    private TextView tvNameInfoDialog;
    private TextView tvAddressInfoDialog;
    private TextView tvInfo1InfoDialog;
    private Button btnDrivingInfoDialog;
    private Button btnWalkingInfoDialog;
    private Button btnShareLocationInfoDialog;

    AlertDialog.Builder mBuilder;

    AlertDialog dialogInfo;


    //    bottomsheet info
    private BottomSheetBehavior mSheetBehavior;
    private View bottomSheetInfoDirection;
    private ImageView imgCloseBottomInfo;
    private TextView tvNameBottomInfo;
    private TextView tvAddressBottomInfo;
    private TextView tvDistanceBottomInfo;
    private TextView tvTimeBottomInfo;
    private TextView tvChooseWalkingBottomInfo;
    private TextView tvChooseDrivingBottomInfo;

    private String mTimeBottomInfo = "";
    private String mDistanceBottomInfo = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatustBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapping(view);
        setHasOptionsMenu(true);

        initBottomSheet();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        mRetrofit = RetrofitClient.getRetrofitClient();
        mApiService = mRetrofit.create(APIService.class);


        mLiServiceAround.setOnClickListener(this);
        mLiFriend.setOnClickListener(this);
        imgCurentLocation.setOnClickListener(this::onClick);
        imgTypeMap.setOnClickListener(this::onClick);

        markerOptions = new MarkerOptions();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initLocation();

        getAutocompletePlace();

        listUser = new ArrayList<>();
//
        initfirebase();

//        initRequestLocation();

        return view;
    }

    private void mapping(View view) {
        imgCurentLocation = (ImageView) view.findViewById(R.id.img_curent_location);
        mLiServiceAround = view.findViewById(R.id.line_service_around);
        mLiFriend = view.findViewById(R.id.line_friend);
        imgTypeMap = (ImageView) view.findViewById(R.id.img_type_map);

        bottomSheetInfoDirection = view.findViewById(R.id.bottom_sheet_info_direction);
        imgCloseBottomInfo = (ImageView) view.findViewById(R.id.img_close_bottom_info);
        tvNameBottomInfo = (TextView) view.findViewById(R.id.tv_name_bottom_info);
        tvAddressBottomInfo = (TextView) view.findViewById(R.id.tv_address_bottom_info);
        tvDistanceBottomInfo = (TextView) view.findViewById(R.id.tv_distance_bottom_info);
        tvTimeBottomInfo = (TextView) view.findViewById(R.id.tv_time_bottom_info);
        tvChooseWalkingBottomInfo = (TextView) view.findViewById(R.id.tv_choose_walking_bottom_info);
        tvChooseDrivingBottomInfo = (TextView) view.findViewById(R.id.tv_choose_driving_bottom_info);
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
            Places.initialize(getApplicationContext(), autoPlaceKey);
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
        searchIcon.setMaxWidth(16);
        edSeach.setTextSize(16);
        tvSeach.setTextSize(16);
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
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        imgTypeMap.setImageDrawable(getResources().getDrawable(R.drawable.vetinh));
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
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
                        mNamePlace = nameService;
                        Toast.makeText(getContext(), getResources().getString(R.string.txt_label_finding) + title + getResources().getString(R.string.txt_label_recently), Toast.LENGTH_SHORT).show();
                        getNearbyPlace(String.valueOf(mLatitude + "," + mLongitude), nameService);

                    }
                });
                dialogServiceAround.show(getChildFragmentManager(), "ADAS");

                break;
            case R.id.line_friend:
                DialogServiceAroundMemberOnline dialogServiceAroundMemberOnline = new DialogServiceAroundMemberOnline(userList, true, new DialogServiceAroundMemberOnline.IChoose() {
                    @Override
                    public void onLocationClick(User user) {
                        moveCamFriend = true;
                        showMarkerFriend(user.getUsername(), user.getImageURL(), Double.parseDouble(user.getLatitude()), Double.parseDouble(user.getLongitude()));
                    }

                    @Override
                    public void onMessageClick(User user) {
                        Toast.makeText(getContext(), user.getUsername() + "   message", Toast.LENGTH_SHORT).show();
                    }
                });
                dialogServiceAroundMemberOnline.show(getChildFragmentManager(), "ADAD");
                break;
            case R.id.img_curent_location:
                moveCamMy = true;
                getCurrentLocation();
                break;
            case R.id.img_type_map:
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    imgTypeMap.setImageDrawable(getResources().getDrawable(R.drawable.normal));
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
                    imgTypeMap.setImageDrawable(getResources().getDrawable(R.drawable.vetinh));
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
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

            LatLng latLng = new LatLng(latitude, longitude);
            mGeocoder = new Geocoder(getContext());
            List<Address> addresses = mGeocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAdminArea() + " - " + addresses.get(0).getAddressLine(0);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(addresses.get(0).getAdminArea()).snippet(addresses.get(0).getAddressLine(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            currentLocationmMarker = mMap.addMarker(markerOptions);
            currentLocationmMarker.setTag(0);


            if (moveCamMy) {
                moveCamMy = false;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


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
        Call<PlaceResponse> call = mApiService.getPlaceNearby(location, Const.RADIUS_PLACE, type, "true", "", placeKey);
        call.enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                if (response.code() == 200) {
                    if (response != null) {
                        mPlaceResponse = new PlaceResponse();
                        mPlaceResponse = (PlaceResponse) response.body();
                        if (mPlaceResponse.getStatus().equalsIgnoreCase(Const.STATUS_OK)) {
                            markers = new MarkerOptions[mPlaceResponse.getResults().size()];
                            mMap.clear();
                            showMarker(mLatitude, mLongitude);
                            for (int i = 0; i < mPlaceResponse.getResults().size(); i++) {
                                double lon = mPlaceResponse.getResults().get(i).getGeometry().getViewport().getNortheast().getLng();
                                double lat = mPlaceResponse.getResults().get(i).getGeometry().getViewport().getNortheast().getLat();
                                showMarkerNearby(lat, lon);
                                markerOptions.position(new LatLng(lat, lon));
                            }
                        } else {
                            Toast.makeText(getContext(), getResources().getString(R.string.txt_label_not_founding) + mContentPlace + getResources().getString(R.string.txt_label_recently), Toast.LENGTH_SHORT).show();
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

            markerOptions.position(latLng);
            markerOptions.title(addresses.get(0).getAdminArea()).snippet(addresses.get(0).getAddressLine(0));
            if (mNamePlace.equalsIgnoreCase(Const.ATM_PLACE)) {
                markerOptions.icon(CommonUtils.bitmapDescriptorFromVector(getContext(), R.drawable.atm));
            } else if (mNamePlace.equalsIgnoreCase(Const.RESTAURANT_PLACE)) {
                markerOptions.icon(CommonUtils.bitmapDescriptorFromVector(getContext(), R.drawable.restaurant));
            } else if (mNamePlace.equalsIgnoreCase(Const.PETROLEUM_PLACE)) {
                markerOptions.icon(CommonUtils.bitmapDescriptorFromVector(getContext(), R.drawable.gas));
            } else if (mNamePlace.equalsIgnoreCase(Const.GROCERY_PLACE)) {
                markerOptions.icon(CommonUtils.bitmapDescriptorFromVector(getContext(), R.drawable.grocery));
            } else if (mNamePlace.equalsIgnoreCase(Const.HOTEL_PLACE)) {
                markerOptions.icon(CommonUtils.bitmapDescriptorFromVector(getContext(), R.drawable.hotel));
            } else if (mNamePlace.equalsIgnoreCase(Const.HOSPITAL_PLACE)) {
                markerOptions.icon(CommonUtils.bitmapDescriptorFromVector(getContext(), R.drawable.hospital));
            } else if (mNamePlace.equalsIgnoreCase(Const.PHARMACIES_PLACE)) {
                markerOptions.icon(CommonUtils.bitmapDescriptorFromVector(getContext(), R.drawable.pharmacy));
            } else if (mNamePlace.equalsIgnoreCase(Const.TOURIES_PLACE)) {
                markerOptions.icon(CommonUtils.bitmapDescriptorFromVector(getContext(), R.drawable.placename));
            } else {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            }
            markerNearby = mMap.addMarker(markerOptions);
            mMap.addMarker(markerOptions);
            if (moveCamNearby) {
                moveCamNearby = false;
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(14f));
            }

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    showDataBottomSheet(getAddress(latLng)[0], marker.getPosition(), marker.getPosition().latitude + "," + marker.getPosition().longitude);

//                    showDialogInfo(getAddress(latLng)[0], getAddress(latLng)[1], marker.getPosition().latitude + "," + marker.getPosition().longitude);
                    return true;
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMarkerFriend(String name, String imageUrl, double latitude, double longitude) {
        try {
            LatLng latLng = new LatLng(latitude, longitude);
            mGeocoder = new Geocoder(getContext());
            List<Address> addresses = mGeocoder.getFromLocation(latitude, longitude, 1);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(name).snippet(addresses.get(0).getAddressLine(0));
            if (imageUrl.equalsIgnoreCase("default")) {
                markerOptions.icon(CommonUtils.bitmapDescriptorFromVectorFriend(getContext(), R.drawable.avatar));
            } else {
                Picasso.get().load(imageUrl).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Bitmap bitmapSmall = Bitmap.createScaledBitmap(bitmap, Const.WIDTH_MARKER_FRIEND, Const.HEIGHT_MARKER_FRIEND, false);
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmapSmall));
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }

            mMap.addMarker(markerOptions);
            if (moveCamFriend) {
                moveCamFriend = false;
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12f));
            }

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    showDataBottomSheet(name, marker.getPosition(), marker.getPosition().latitude + "," + marker.getPosition().longitude);
//                    showDialogInfo(name, addresses.get(0).getAddressLine(0), latitude + "," + longitude);
                    return true;
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("zxcvbn", e.getMessage());
        }
/*
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
*/
    }


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


    private void sendRequestDirection(String destination, String mode) {
        String origin = mLatitude + "," + mLongitude;
        if (origin != null && destination != null)
            try {
                new DirectionFinder((DirectionFinderListener) this, origin, destination, mode).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onDirectionFinderStart() {
        Toast.makeText(getContext(), getString(R.string.txt_finding_direction), Toast.LENGTH_SHORT).show();
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {

        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            mTimeBottomInfo = route.duration.text;
            mDistanceBottomInfo = route.distance.text;
            if (tvTimeBottomInfo != null) {
                tvTimeBottomInfo.setVisibility(View.VISIBLE);
                tvDistanceBottomInfo.setVisibility(View.VISIBLE);
                tvTimeBottomInfo.setText(CommonUtils.formatTimeDirection(route.duration.text));
                tvDistanceBottomInfo.setText(route.distance.text);
            }


            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(20);
            if (modeDirection.equalsIgnoreCase(Const.MODE_WALKING)) {
                polylineOptions.pattern(Collections.singletonList(DOT));
            }
            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getCurrentLocation();
            }
        }, 3000);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void ReceiverProduct(User friend) {
        if (friend != null) {
            Toast.makeText(getActivity(), getResources().getString(R.string.txt_finding_location_friend) + friend.getUsername(), Toast.LENGTH_SHORT).show();
            moveCamFriend = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showMarkerFriend(friend.getUsername(), friend.getImageURL(), Double.parseDouble(friend.getLatitude()), Double.parseDouble(friend.getLongitude()));

                }
            }, 1000);
            EventBus.getDefault().removeAllStickyEvents();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void ReceiverUserList(List<User> userList) {
        if (userList != null) {
            listUser = userList;
            Toast.makeText(getActivity(), getResources().getString(R.string.txt_finding_location_group_friends), Toast.LENGTH_SHORT).show();
            moveCamFriend = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < userList.size(); i++) {
                        showMarkerFriend(userList.get(i).getUsername(), userList.get(i).getImageURL(), Double.parseDouble(userList.get(i).getLatitude()), Double.parseDouble(userList.get(i).getLongitude()));
                    }
                }
            }, 1000);

            EventBus.getDefault().removeAllStickyEvents();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    private void showDialogInfo(String name, String address, String location) {
        mBuilder = new AlertDialog.Builder(getActivity());

        View dialog = (View) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_info_marker, null);
        mBuilder.setView(dialog);

        tvNameInfoDialog = (TextView) dialog.findViewById(R.id.tv_name_info_dialog);
        tvAddressInfoDialog = (TextView) dialog.findViewById(R.id.tv_address_info_dialog);
        tvInfo1InfoDialog = (TextView) dialog.findViewById(R.id.tv_info1_info_dialog);
        btnDrivingInfoDialog = (Button) dialog.findViewById(R.id.btn_driving_info_dialog);
        btnWalkingInfoDialog = (Button) dialog.findViewById(R.id.btn_walking_info_dialog);
        btnShareLocationInfoDialog = (Button) dialog.findViewById(R.id.btn_share_location_info_dialog);

        if (name != null) {
            tvNameInfoDialog.setText(name);
            tvInfo1InfoDialog.setText(getResources().getString(R.string.txt_label_follow_direction) + name);
        }
        if (address != null) {
            tvAddressInfoDialog.setText(address);
        }
        if (location != null) {
            btnDrivingInfoDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendRequestDirection(location, Const.MODE_DRIVING);
                    modeDirection = Const.MODE_DRIVING;
                    dialogInfo.dismiss();
                }
            });

            btnShareLocationInfoDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogInfo.dismiss();
                }
            });

            btnWalkingInfoDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modeDirection = Const.MODE_WALKING;
                    sendRequestDirection(location, Const.MODE_WALKING);
                    dialogInfo.dismiss();
                }
            });
        }

        dialogInfo = mBuilder.show();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return true;
    }

    public String[] getAddress(LatLng latLng) {
        String[] add = new String[2];
        try {
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            add[0] = addresses.get(0).getAdminArea();
            add[1] = addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return add;

    }

    private void showDataBottomSheet(String name, LatLng latLng, String location) {
        mSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        tvNameBottomInfo.setText(getResources().getString(R.string.txt_label_follow_direction) + name);
        tvAddressBottomInfo.setText(getAddress(latLng)[1]);

        tvChooseWalkingBottomInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeDirection = Const.MODE_WALKING;
                sendRequestDirection(location, Const.MODE_WALKING);
            }
        });

        tvChooseDrivingBottomInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestDirection(location, Const.MODE_DRIVING);
                modeDirection = Const.MODE_DRIVING;
            }
        });


    }

    private void initBottomSheet() {
        mSheetBehavior = BottomSheetBehavior.from(bottomSheetInfoDirection);
        mSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


        imgCloseBottomInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });


    }
}
