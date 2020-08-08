package com.saadi.findmatch;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.paginate.Paginate;
import com.saadi.findmatch.adapter.FindMatchAdapter;
import com.saadi.findmatch.model.FindMatchResponse;
import com.saadi.findmatch.model.MyMatchModel;
import com.saadi.findmatch.utils.Constants;
import com.saadi.findmatch.utils.CustomLoadingListItemCreator;
import com.saadi.findmatch.utils.Databases;
import com.saadi.findmatch.utils.HelperMethod;
import com.saadi.findmatch.utils.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kailash Suthar.
 */

public class FindMatchActivity extends AppCompatActivity implements Paginate.Callbacks {

    private CoordinatorLayout actionView;
    private RecyclerView recyclerView;
    private FindMatchAdapter recyclerAdapter;
    private ArrayList<MyMatchModel> matchesList;

    //set pagination
    private boolean loading = false;
    private int page = 0, totalSize;
    private Handler handler;
    private Paginate paginate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_match);

        initView();
    }

    /**
     * Initialize view and object
     */
    private void initView() {
        actionView = findViewById(R.id.actionView);
        recyclerView = findViewById(R.id.recyclerView);

        handler = new Handler();
        setupPagination();
    }

    /**
     * Set up pagination for loading matches
     */
    private void setupPagination() {
        if (paginate != null) {
            paginate.unbind();
        }
        handler.removeCallbacks(callback);
        loading = false;
        page = 0;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(layoutManager);
        matchesList = new ArrayList<>();
        recyclerAdapter = new FindMatchAdapter(getBaseContext(), matchesList);
        recyclerView.setAdapter(recyclerAdapter);

        paginate = Paginate.with(recyclerView, this)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(new CustomLoadingListItemCreator(recyclerView))
                .build();

        callGetMyMatches();

    }


    private void callGetMyMatches() {
        if (checkOfflineData()) return;
        if (HelperMethod.isConnected(getBaseContext())) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL.GET_MATCHES_LIST, response -> {
                Log.e("response ", response);
                try {
                    FindMatchResponse data = new Gson().fromJson(response, FindMatchResponse.class);
                    totalSize = data.getMatchesList().size();
                    matchesList.addAll(data.getMatchesList());
                    insetMatchesIntoDatabase();
                    recyclerAdapter.updateList(matchesList);
                    setData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, error -> {
                paginate.setHasMoreDataToLoad(false);
                if (error instanceof TimeoutError) {
                    handleErrors(Constants.Strings.CONNECTION_TIMEOUT);
                } else if (error instanceof NoConnectionError) {
                    handleErrors(Constants.Strings.NO_INTERNET);
                } else {
                    Toast.makeText(getBaseContext(), Constants.Strings.SEVER_UNREACHABLE, Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            stringRequest.setShouldCache(false);
            VolleySingleton.getInstance(this).cancelPendingRequests(Constants.RequestTags.RT_FIND_MATCH);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.RetryPolicy.TIMEOUT_MS, Constants.RetryPolicy.MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest, Constants.RequestTags.RT_FIND_MATCH);
        } else {
            paginate.setHasMoreDataToLoad(false);
            handleErrors(Constants.Strings.NO_INTERNET);
        }
    }

    private void setData() {
        if (totalSize < 20) {
            paginate.setHasMoreDataToLoad(false);
            loading = true;
        } else {
            loading = false;
        }
        page = matchesList.size();

    }

    @Override
    public synchronized void onLoadMore() {
        // Fake asynchronous loading that will generate page of random data after some delay
        handler.postDelayed(callback, 2000);
    }

    @Override
    public synchronized boolean isLoading() {
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return totalSize < 20;
    }

    private Runnable callback = () -> {
        if (!loading) {
            loading = true;
            callGetMyMatches();
        }
    };

    public void setInitialData() {
        page = 0;
        matchesList = new ArrayList<>();
        paginate.setHasMoreDataToLoad(true);
        callGetMyMatches();
    }

    private boolean checkOfflineData() {
        if (new Databases(getBaseContext()).getMatchData().getCount() > 0) {
            matchesList = getOfflineMatches();
            setData();
            return true;
        }
        return false;
    }

    /**
     * Insert matches list from API to offline databases
     */
    private void insetMatchesIntoDatabase() {
        for (MyMatchModel match : matchesList) {
            String mobileNumber = match.getPhone().replaceAll("[^0-9]", "").trim();
            Databases databases = new Databases(getBaseContext());
            if (databases.checkUniqueMatch(mobileNumber)) {
                databases.insertMatchData(match.getPhone(), match.getNameModel().getTitle(),
                        match.getNameModel().getFirst(), match.getNameModel().getLast(), match.getDobModel().getAge(),
                        match.getGender(), match.getLocationModel().getCity(), match.getLocationModel().getCountry(),
                        match.getPictureModel().getMedium(), match.getStatus());
                Log.e("Inserted ", mobileNumber);
            }
        }
    }

    /**
     * Retrieve offline marches list from database to show
     */
    private ArrayList<MyMatchModel> getOfflineMatches() {
        ArrayList<MyMatchModel> myMatches = new ArrayList<>();
        Databases databases = new Databases(getBaseContext());
        Cursor cursor = databases.getMatchData();
        if (cursor.moveToFirst()) {
            do {
                MyMatchModel match = new MyMatchModel();
                MyMatchModel.NameModel fullName = new MyMatchModel.NameModel();
                MyMatchModel.LocationModel location = new MyMatchModel.LocationModel();
                MyMatchModel.PictureModel picture = new MyMatchModel.PictureModel();
                MyMatchModel.DOBModel age = new MyMatchModel.DOBModel();
                match.setPhone(cursor.getString(0));
                fullName.setTitle(cursor.getString(1));
                fullName.setFirst(cursor.getString(2));
                fullName.setLast(cursor.getString(3));
                match.setNameModel(fullName);
                age.setAge(cursor.getString(4));
                match.setDobModel(age);
                match.setGender(cursor.getString(5));
                location.setCity(cursor.getString(6));
                location.setCountry(cursor.getString(7));
                match.setLocationModel(location);
                picture.setMedium(cursor.getString(8));
                match.setPictureModel(picture);
                match.setStatus(cursor.getString(9));
                matchesList.add(match);
            } while (cursor.moveToNext());
        }
        Log.e("Matches Count ", String.valueOf(matchesList.size()));
        return myMatches;
    }


    private void handleErrors(String message) {
        Snackbar snackbar = Snackbar.make(actionView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", view -> {
                    setInitialData();
                });
        snackbar.setActionTextColor(Color.YELLOW);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.BLACK);
        ((TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text)).setTextColor(Color.WHITE);
        snackbar.show();
    }
}