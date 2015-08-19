package edu.weber.chemistryapp.loader;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import edu.weber.chemistryapp.models.animations.Animation;
import edu.weber.chemistryapp.models.animations.Animations;
import edu.weber.chemistryapp.persistence.Exclude;

/**
 * Created by agessel on 6/7/15.
 */
public class Serializer {
    public String serialize(List<Animation> animations) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();
        return gson.toJson(new Animations(animations));
    }
}
