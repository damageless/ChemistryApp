package edu.weber.chemistryapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

/**
 * Created by agessel on 6/5/15.
 */
public class Transition {
    @SerializedName("Name")
    public String mName;
    @SerializedName("TransitionTo")
    public String mTransitionTo;
    @SerializedName("ForElements")
    public List<String> mElements;

    public Transition(String mName, String mTransitionTo) {
        this.mName = mName;
        this.mTransitionTo = mTransitionTo;
    }

    public Transition(String mTransitionTo) {
        this.mTransitionTo = mTransitionTo;
    }
}
