package com.example.lenovo.roadbuddy;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener, LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {
    public GoogleApiClient mApiClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap mMap;
    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];
    Location loc=new Location("");
    LocationManager locationManager;
public int vehiclein=0;
Button sos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
sos=(Button)findViewById(R.id.fab);
        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,1000, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 01, 1000, this);


        loc= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
       // Toast.makeText(this, ""+loc, Toast.LENGTH_SHORT).show();
        if(loc==null)
        {
            loc= locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
          //  Toast.makeText(this, ""+loc, Toast.LENGTH_SHORT).show();

//            loc.setLatitude(-34);
//            loc.setLongitude(151);
        }

        sockconn("sendpothole");


        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();

    }

    public void setSos(View v){
     //   Toast.makeText(this, "Jaan bachao aur ka", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Calling for help...Location sent to next of kin", Toast.LENGTH_SHORT).show();
        SmsManager smgr = SmsManager.getDefault();
        EditText tv=(EditText) findViewById(R.id.tvv1);
        tv.setVisibility(View.INVISIBLE);
        sockconn("Breakdown");
        try {
            smgr.sendTextMessage("" + tv.getText(), "8890336585", "Accident occured at coordinates:" + loc.getLongitude() + "," + loc.getLatitude(), null, null);
            Intent sInt = new Intent(Intent.ACTION_VIEW);
            sInt.putExtra("address", new String[]{tv.getText().toString()});
            sInt.putExtra("sms_body","Accident occured at coordinates:" + loc.getLongitude() + "," + loc.getLatitude());
            sInt.setType("vnd.android-dir/mms-sms");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
public void detectdriving(){
//    ActivityRecognitionClient mActivityRecognitionClient = new ActivityRecognitionClient(this);
//
//    Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(
//            10,
//            mPendingIntent);
//
//    ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
//    ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();
//
//    for (DetectedActivity activity : detectedActivities) {
//        Log.e(TAG, "Detected activity: " + activity.getType() + ", " + activity.getConfidence());
  //  }
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }
    @IgnoreExtraProperties
    public class Point {
        public Float x;
        public Float y;
        public Float z;
        public Integer index;
        public Double lat;
        public Double longi;
        public Point() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Point(Integer index,Float x, Float y, Float z,Double lat,Double longi) {
            this.index=index;
            this.x = x;
            this.y = y;
            this.z = z;
            this.lat=lat;
            this.longi=longi;
        }


    }
    Integer t=0;
    public void writeNewPoint(Integer index,Float x, Float y, Float z,Double lat,Double longi) {
        Point p = new Point(index,x,y,z,lat,longi);

        //mDatabase.child("Observations").child(index).setValue(x);
    }
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    Integer count=0;
    private FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    public void onSensorChanged(SensorEvent event) {
        //Point user = new Point(name, email);
        rootNode=FirebaseDatabase.getInstance();
        reference=rootNode.getReference("Observations");
        

        //mDatabase.child("users").child(userId).setValue(user);

        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        final float alpha = 0.8f;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];


        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];
        //Get all the values
        count++;

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
        {
            if(getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if(location!=null)
                                {
                                    Double lat=location.getLatitude();
                                    Double longt=location.getLongitude();
                                    if(count%10==0) {
                                        t = t + 1;
                                        Point p1 = new Point(t, linear_acceleration[0], linear_acceleration[1], linear_acceleration[2],lat,longt);
                                        reference.child(t.toString()).setValue(p1);
                                    }
                                }
                            }
                        });
            }
            else {
                //requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
            }
        }
        float scalarProduct = gravity[0] * linear_acceleration[0] +
                gravity[1] * linear_acceleration[1] +
                gravity[2] * linear_acceleration[2];
        float gravityVectorLength = (float) Math.sqrt(gravity[0] * gravity[0] +
                gravity[1] * gravity[1] + gravity[2] * gravity[2]);
        float lianearAccVectorLength = (float) Math.sqrt(linear_acceleration[0] * linear_acceleration[0] +
                linear_acceleration[1] * linear_acceleration[1] + linear_acceleration[2] * linear_acceleration[2]);

        float cosVectorAngle = scalarProduct / (gravityVectorLength * lianearAccVectorLength);

        //TextView tv = (TextView) findViewById(R.id.tv);
        if (lianearAccVectorLength > 12) {//increase to detect only bigger accelerations, decrease to make detection more sensitive but noisy
            if (cosVectorAngle > 0.65 && cosVectorAngle < 0.85) {
          //      tv.setText("Down");
//                Toast.makeText(this, "Down", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Pothole Detected", Toast.LENGTH_SHORT).show();

                markpothole(loc);
            } else if (cosVectorAngle > -0.85 && cosVectorAngle < -0.65) {
            //    tv.setText("Up");
//                Toast.makeText(this, "Up", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Speed Breaker", Toast.LENGTH_SHORT).show();

//                LatLng sydney = new LatLng(loc.getLatitude(),loc.getLongitude());
             //   Toast.makeText(this, ""+loc, Toast.LENGTH_SHORT).show();
//        mMap.addMarker(new MarkerOptions().position(sydney));
            }
        }

        if (lianearAccVectorLength > 12) {//increase to detect only bigger accelerations, decrease to make detection more sensitive but noisy
            if (0<cosVectorAngle  && cosVectorAngle < 0.5) {
                //      tv.setText("Down");
//                Toast.makeText(this, "Down", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Accident1", Toast.LENGTH_SHORT).show();
sockconn("accident:"+loc.getLatitude()+":"+loc.getLongitude());
                markpothole(loc);
            } else if (cosVectorAngle > -0.5 && 0>cosVectorAngle) {
                //    tv.setText("Up");
//                Toast.makeText(this, "Up", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Accident2", Toast.LENGTH_SHORT).show();

//                LatLng sydney = new LatLng(loc.getLatitude(),loc.getLongitude());
                //   Toast.makeText(this, ""+loc, Toast.LENGTH_SHORT).show();
//        mMap.addMarker(new MarkerOptions().position(sydney));
            }
        }



    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
//        mMap.setMinZoomPreference(12);
    //    Toast.makeText(this, "changed to "+location, Toast.LENGTH_SHORT).show();
//        CircleOptions circleOptions = new CircleOptions();
//        circleOptions.center(new LatLng(location.getLatitude(),
//                location.getLongitude()));
//
//        circleOptions.radius(20);
//        circleOptions.fillColor(Color.RED);
//        circleOptions.strokeWidth(3);
//
//        mMap.addCircle(circleOptions);
        LatLng a=new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(a));
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(a);
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(16);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
loc=location;
    }

public void markpothole(Location location){
//    CircleOptions circleOptions = new CircleOptions();
//    circleOptions.center(new LatLng(location.getLatitude(),
//            location.getLongitude()));
//
//    circleOptions.radius(4);
//    circleOptions.fillColor(Color.RED);
//    circleOptions.strokeWidth(1);

//    mMap.addCircle(circleOptions);
    LatLng sydney = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney));
sockconn("android:"+location.getLatitude()+","+location.getLongitude());
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

    public void sockconn(final String msg)
    {
       // Toast.makeText(this, "sending message", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Uploading data", Toast.LENGTH_SHORT).show();
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //Replace below IP with the IP of that device in which server socket open.
                    //If you change port then change the port number in the server side code also.
                    Socket s = new Socket("192.168.137.1", 9002);

                    OutputStream out = s.getOutputStream();

                    PrintWriter output = new PrintWriter(out);

//                    output.println(msg);
                    output.print(msg);
                    output.flush();
////////////////  //////////////////////
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
//
                    final String st = input.readLine()+"";

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(MapsActivity.this, ""+st, Toast.LENGTH_SHORT).show();
//                           if(st!="pothl")
                            try {if (st.trim().length() != 0) {
                                String[] str11 = st.split(":");
                                for (String sx1 : str11) {
                                    String[] str22 = sx1.split(",");
                                    LatLng sydney = new LatLng(Double.parseDouble(str22[0]), Double.parseDouble(str22[1]));
                                    mMap.addMarker(new MarkerOptions().position(sydney));
                                }
//                                Toast.makeText(MapsActivity.this, "", Toast.LENGTH_SHORT).show();
//    JSONParser parser = new JSONParser();
//                                JSONObject json = (JSONObject) parser.parse(stringToParse);
//
                            }
//   try {
//                                    JSONObject jsona=new JSONObject(st);
//                                    Toast.makeText(MapsActivity.this, ""+ jsona, Toast.LENGTH_SHORT).show();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }

                            }
                            catch(Exception e)
                            {e.printStackTrace();}//  Toast.makeText(getApplicationContext() , "\nFrom Server : " + st,Toast.LENGTH_SHORT).show();
                        }
                    });
/////////////////////////////////////////
                    output.close();
                    out.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Intent intent = new Intent( this, ActivityRecognizedService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mApiClient, 2000, pendingIntent );
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}