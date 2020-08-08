package com.saadi.findmatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kailash Suthar.
 */

public class FindMatchResponse implements Serializable {
    private static final long serialVersionUID = -899926366348955411L;

    @SerializedName("results")
    private ArrayList<MyMatchModel> matchesList;

    public ArrayList<MyMatchModel> getMatchesList() {
        return matchesList;
    }

    public void setMatchesList(ArrayList<MyMatchModel> matchesList) {
        this.matchesList = matchesList;
    }
}