package edu.scu.smurali.parkonthego.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.scu.smurali.parkonthego.R;

/**
 * Created by shruthi on 24-05-2016.
 */
public class MyReservationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_reservation_fragment,container,false);
    }
}
