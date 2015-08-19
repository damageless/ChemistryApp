package edu.weber.chemistryapp.loader;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import edu.weber.chemistryapp.persistence.Exclude;

/**
 * Created by agessel on 6/30/15.
 */
public class AnnotationExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}