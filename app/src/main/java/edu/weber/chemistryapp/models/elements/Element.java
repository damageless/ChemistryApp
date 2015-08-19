package edu.weber.chemistryapp.models.elements;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

import edu.weber.chemistryapp.models.Transition;

/**
 * Created by agessel on 7/9/15.
 */
public class Element {
    @SerializedName("Transition")
    public Transition mTransition;
    @SerializedName("ElementId")
    public UUID mElementId = UUID.randomUUID();
    @SerializedName("Name")
    public String mName;
    @SerializedName("X")
    public float x;
    @SerializedName("Y")
    public float y;
    @SerializedName("CanMove")
    public boolean mCanMove = false;
    @SerializedName("Charge")
    public Charge mCharge;
    @SerializedName("GroupId")
    public UUID mGroupId = UUID.randomUUID();

    public Element(String mName, boolean canMove) {
        this.mName = mName;
        this.mCanMove = canMove;
    }
}
