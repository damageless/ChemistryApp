package edu.weber.chemistryapp.models.animations;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

import edu.weber.chemistryapp.models.Transition;
import edu.weber.chemistryapp.models.animations.Animation;

/**
 * Created by agessel on 6/5/15.
 */
public class TapMultipleElements extends Animation {
    @SerializedName("NumTaps")
    public int mNumberOfElements = 2;

    public TapMultipleElements(String type, String description, int numberOfElements) {
        super(type, description);
        this.mNumberOfElements = numberOfElements;
    }
}
