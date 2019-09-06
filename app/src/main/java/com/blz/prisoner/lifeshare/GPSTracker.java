package com.blz.prisoner.lifeshare;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
public  class GPSTracker extends Service implements LocationListener {


    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    boolean isLocationAvailable = false;


    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;


    String msubLocality="",mthroughfare="",mlocality="",msubAdminArea="",marea="";

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }


    @SuppressLint("MissingPermission")
    public Location getLocation() {

        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(mContext, "Please Turn On your GPS or Data Connection", Toast.LENGTH_LONG).show();

                /*if(!isGPSEnabled){

                }
*/
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (latitude == 0 && longitude == 0) {

        }
        this.isLocationAvailable = true;
        return location;
    }




    public String getLocationAdress() {
        if (isLocationAvailable) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> listAddres = null;
            try {
                listAddres = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e1) {
                e1.printStackTrace();
                return ("IO Exception trying to get address\nPlease Turn On your Data Connection\n:" + e1);
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments "
                        + Double.toString(latitude) + " , "
                        + Double.toString(longitude)
                        + " passed to address service";
                e2.printStackTrace();
                return errorString;
            }
            if (listAddres != null && listAddres.size() > 0) {
                String address = "";


                if (listAddres.get(0).getSubLocality() != null) {
                    address += listAddres.get(0).getSubLocality() + ", ";

                    msubLocality += listAddres.get(0).getSubLocality();

                }

                if (listAddres.get(0).getThoroughfare() != null) {
                    address += listAddres.get(0).getThoroughfare() + ", ";

                    mthroughfare += listAddres.get(0).getThoroughfare();

                }


                if (listAddres.get(0).getLocality() != null) {
                    address += listAddres.get(0).getLocality() + ", ";
                    mlocality += listAddres.get(0).getLocality();
                }


                if (listAddres.get(0).getSubAdminArea() != null) {
                    address += listAddres.get(0).getSubAdminArea() + ", ";

                    msubAdminArea += listAddres.get(0).getSubAdminArea();



                }

                if (listAddres.get(0).getAdminArea() != null) {

                    address += listAddres.get(0).getAdminArea();

                    marea += listAddres.get(0).getAdminArea();

                }
                return address;

            } else {
                return "No address found by the service: Note to the developers, If no address is found by google itself, there is nothing you can do about it. :(";
            }

        } else {
            return "Location Not available";
        }
    }


    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("Location Access Is not Enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {

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


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public String getMsubLocality() {
        return msubLocality;
    }

    public String getMthroughfare() {
        return mthroughfare;
    }

    public String getMlocality() {
        return mlocality;
    }

    public String getMsubAdminArea() {
        return msubAdminArea;
    }

    public String getMarea() {
        return marea;
    }





}
