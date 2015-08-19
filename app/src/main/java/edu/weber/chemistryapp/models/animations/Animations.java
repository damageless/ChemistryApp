package edu.weber.chemistryapp.models.animations;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by agessel on 6/30/15.
 */
public class Animations {
    @SerializedName("Animations")
    public List<Animation> animations;

    public Animations(List<Animation> animations) {
        this.animations = animations;
    }
}
