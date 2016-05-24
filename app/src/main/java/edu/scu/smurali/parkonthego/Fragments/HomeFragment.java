package edu.scu.smurali.parkonthego.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import edu.scu.smurali.parkonthego.GridAdapter;
import edu.scu.smurali.parkonthego.Location;
import edu.scu.smurali.parkonthego.LocationsOnMap;
import edu.scu.smurali.parkonthego.R;

/**
 * Created by shruthi on 24-05-2016.
 */
public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_screen_fragment, viewGroup, false);
        return view;
    }

//
//    PlaceAutocompleteFragment autocompleteFragment;
//
//    GridView grid, grid2;
//    String[] web = {
//            "START DATE",
//            "START TIME",
//
//
//    } ;
//    String[] web2 = {
//            "END DATE",
//            "END TIME",
//    } ;
//
//    // TextView startDate, startTime, endDate, endTime;
//    int[] imageId = {
//            R.drawable.calender,
//            R.drawable.clock,
//
//    };
//    int[] imageId2 = {
//            R.drawable.calender,
//            R.drawable.clock
//    };
//    ArrayList<Location> locationList = new ArrayList<Location>();
//    private Button searchParkingLocations;
//    private LatLng searchedLatLng;
//
//
//    ///////////////////////////////////////////////////////test code//////////////////////////////////////////////
//    private String searchedAddress;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//
//
//        locationList.add(new Location(1,37.346317, -121.938025, 10, "location1","help1"));
//        locationList.add(new Location(2,37.345325, -121.936551, 10, "location1","help1"));
//        locationList.add(new Location(3,37.345186, -121.936744, 10, "location1","help1"));
//        locationList.add(new Location(4,37.345135, -121.935977, 10, "location1","help1"));
//        locationList.add(new Location(5,37.348650, -121.939345, 10, "location1","help1"));
//        locationList.add(new Location(6,37.3496, -121.9390, 10, "location1","help1"));
//        locationList.add(new Location(7,37.347562, -121.932221, 10, "location1","help1"));
//
//
//        autocompleteFragment = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//
//                searchedLatLng=place.getLatLng();
//                searchedAddress= place.getAddress().toString();
//
//
//                Log.i("place name", "Place: " + place.getName());
//            }
//
//            @Override
//            public void onError(Status status) {
//
//                Log.i("error ", "An error occurred: " + status);
//            }
//        });
//
//
//        searchParkingLocations = (Button)findViewById(R.id.searchParkingLocation);
//        searchParkingLocations.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeFragment.this, LocationsOnMap.class);
//                intent.putExtra("locationList",locationList);
//                intent.putExtra("searchedLocationLat",searchedLatLng.latitude);
//                intent.putExtra("searchedLocationLong",searchedLatLng.longitude);
//                intent.putExtra("searchedLocationAddress",searchedAddress);
//                startActivity(intent);
//            }
//        });
//
//        GridAdapter adapter = new GridAdapter(HomeFragment.this, web, imageId);
//        grid=(GridView)findViewById(R.id.grid);
//        grid.setAdapter(adapter);
//
//        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Toast.makeText(HomeFragment.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        GridAdapter adapter2 = new GridAdapter(HomeFragment.this, web2, imageId2);
//        grid2=(GridView)findViewById(R.id.grid2);
//        grid2.setAdapter(adapter2);
//
//        grid2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Toast.makeText(HomeFragment.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        return inflater.inflate(R.layout.home_screen_fragment,container,false);
//
//
//    }
}
