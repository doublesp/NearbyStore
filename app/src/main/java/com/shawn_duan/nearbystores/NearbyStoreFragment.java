package com.shawn_duan.nearbystores;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.shawn_duan.nearbystores.googleplace.GooglePlaceClient;
import com.shawn_duan.nearbystores.gplace.GPlace;
import com.shawn_duan.nearbystores.gplace.Result;

import java.util.ArrayList;
import java.util.IllegalFormatConversionException;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class NearbyStoreFragment extends Fragment {

    private final static String TAG = NearbyStoreFragment.class.getSimpleName();

    private RecyclerView rvStores;
    private ArrayList<Result> mStoreList;
    private RecyclerView.Adapter mAdapter;

    private Subscription searchNearbyStoresSubscription;

    public NearbyStoreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        rvStores = (RecyclerView) view.findViewById(R.id.rvStores);
        rvStores.setLayoutManager(new LinearLayoutManager(getActivity()));

        mStoreList = new ArrayList<>();
        mAdapter = new NearbyStoreAdapter(getActivity(), mStoreList);

        rvStores.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        LatLng lastKnownLocation = ((MainActivity) getActivity()).getLastKnownLocation();


        String latAndLng;

        latAndLng = (lastKnownLocation != null) ? Double.toString(lastKnownLocation.latitude) + "," +
                Double.toString(lastKnownLocation.longitude) : null;

        subscribeNearbyStores(latAndLng);
    }

    private void subscribeNearbyStores(String latAndLng) {
        searchNearbyStoresSubscription = GooglePlaceClient.newInstance()
                .searchStores(latAndLng)
                .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SearchNearbyStoresSubscriber(true));
    }

    private class SearchNearbyStoresSubscriber extends Subscriber<GPlace> {
        private boolean clearOldList;

        public SearchNearbyStoresSubscriber(boolean clearOldList) {
            this.clearOldList = clearOldList;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof HttpException) {
                HttpException response = (HttpException) e;
                int code = response.code();
                Log.d(TAG, "Rx Subscriber error with code: " + code);
            } else if (e instanceof IllegalFormatConversionException) {

            }

        }

        @Override
        public void onNext(GPlace response) {
            List<Result> results = response.getResults();
            Log.d(TAG, "results: " + results.size());
            if (clearOldList) {
                mStoreList.clear();
            }
            mStoreList.addAll(results);
            mAdapter.notifyDataSetChanged();
        }
    }
}
