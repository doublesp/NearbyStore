package com.shawn_duan.nearbystores;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.shawn_duan.nearbystores.gplace.Result;

import java.util.ArrayList;

/**
 * Created by sduan on 12/4/16.
 */

public class NearbyStoreAdapter extends RecyclerView.Adapter<NearbyStoreAdapter.StoreViewHolder> {

    private Activity mActivity;
    private ArrayList<Result> mResultsList;

    public NearbyStoreAdapter(Activity activity, ArrayList<Result> results) {
        mActivity = activity;
        mResultsList = results;
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item, parent, false);

        return new NearbyStoreAdapter.StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {
        Result store = mResultsList.get(position);
        holder.updateInfo(store);
    }

    @Override
    public int getItemCount() {
        return mResultsList.size();
    }

    public class StoreViewHolder extends RecyclerView.ViewHolder {

        TextView storeName;
        TextView storeLat;
        TextView storeLng;

        public StoreViewHolder(View itemView) {
            super(itemView);
            storeName = (TextView) itemView.findViewById(R.id.store_name);
            storeLat = (TextView) itemView.findViewById(R.id.store_lat);
            storeLng = (TextView) itemView.findViewById(R.id.store_lng);
        }

        public void updateInfo(Result result) {
            storeName.setText(result.getName());
            storeLat.setText(Double.toString(result.getGeometry().getLocation().getLat()));
            storeLng.setText(Double.toString(result.getGeometry().getLocation().getLng()));
        }
    }
}
