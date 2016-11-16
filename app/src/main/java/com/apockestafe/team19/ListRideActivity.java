package com.apockestafe.team19;

import com.apockestafe.team19.Event;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListRideActivity extends AppCompatActivity {

    private Button submitRide, cancelRide;
    //private TextView address, seatCount;
    private EditText streetAddressValue, cityValue, stateValue, zipcodeValue, seatCountValue;
    private TextView errorText;
    private boolean rideListed;
    private int seatCount;
    private String addressValue;
    private Event e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ride);

//        address = (TextView) findViewById(R.id.address);
//        seatCount = (TextView)findViewById(R.id.seatCount);
        streetAddressValue = (EditText) findViewById(R.id.streetAddressValue);
        cityValue = (EditText) findViewById(R.id.cityValue);
        stateValue = (EditText) findViewById(R.id.stateValue);
        zipcodeValue = (EditText) findViewById(R.id.zipcodeValue);
        seatCountValue = (EditText) findViewById(R.id.seatCountValue);
        errorText = (TextView)findViewById(R.id.errorText);

        submitRide = (Button) findViewById(R.id.submitRide);
        submitRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressValue = createAddress(streetAddressValue, cityValue, stateValue, zipcodeValue);
                rideListed = checkIfListedRide(addressValue);
                seatCount = getSeatCount(seatCountValue);
                System.out.println("Valid address: " + checkValidAddress(addressValue));

                if (addressValue.length() == 0 && seatCount == 0) {
                    errorText.setText("Error. Enter your car address and seat count");
                } else if (addressValue.length() == 0) {
                    errorText.setText("Error. Enter your car address");
                } else if (seatCount == 0) {
                    errorText.setText("Error. Enter your seat count.");
                } else if (!checkValidAddress(addressValue)) {
                    errorText.setText("Not a valid address.");
                } else if (!rideListed && addressValue.length() > 0 && seatCount != 0 && checkValidAddress(addressValue)) {
                    errorText.setText(" ");
                    //e.addRideLocation(addressValue.getText().toString());
                    startActivity(new Intent(ListRideActivity.this, EventInfo.class));
                } else {
                    errorText.setText("You've already listed your ride.");
                }
            }
        });

        cancelRide = (Button) findViewById(R.id.cancelRide);
        cancelRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public boolean checkIfListedRide(String aV) {
//        ArrayList<String> cL = e.getRideLocation();
//        for (int i = 0; i < cL.size(); i++) {
//            if (carLocation.compareTo(cL.get(i)) == 0) {
//                return true;
//            }
//        }
        return false;
    }

    public int getSeatCount(EditText scv) {
        String sc = scv.getText().toString();
        int seatCountInt = 0;
        if (sc.matches("[0-9]+"))
            seatCountInt = Integer.parseInt(sc);
        return seatCountInt;
    }

    public String createAddress(EditText sav, EditText cv, EditText sv, EditText zcv) {
        String address = sav.getText().toString() + ", " +
                        cv.getText().toString() + ", " +
                        sv.getText().toString() + ", " +
                        zcv.getText().toString();

        System.out.println("Address: " + address);
        return address;
    }

    public boolean checkValidAddress(String streetAddress) {
        boolean validAddress = false;
        LatLng latlng;
        latlng = getLocationFromAddress(this, streetAddress);
        if (latlng != null)
            validAddress = true;

        return validAddress;
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng latlng = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            latlng = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return latlng;
    }
}
