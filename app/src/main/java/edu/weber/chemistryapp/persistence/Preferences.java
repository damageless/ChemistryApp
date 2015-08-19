package edu.weber.chemistryapp.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import edu.weber.chemistryapp.models.Section;
import edu.weber.chemistryapp.models.Transition;

/**
 * Created by agessel on 6/7/15.
 */
public class Preferences {
    private static final String DATABASE_NAME = "edu.weber.chemistryapp:Preferences:";

    private static final String ANIMATIONS_KEY = "animations_key";
    private static final String SECTIONS_KEY = "sections_key";


    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private static Preferences sInstance = null;


    public static Preferences createInstance(Context context) {
        if (sInstance != null) {
            return sInstance;
        }

        return new Preferences(context);
    }

    private Preferences(Context context) {
        mPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public String getAnimations() {
        return mPreferences.getString(ANIMATIONS_KEY, "");
    }

    public void setAnimations(String animations) {
        mEditor.putString(ANIMATIONS_KEY, animations).commit();
    }

    public List<Section> getSections() {
        Gson gson = new Gson();
        List<Section> sections = gson.fromJson(mPreferences.getString(SECTIONS_KEY, ""), new TypeToken<ArrayList<Section>>() {}.getType());

        if (sections == null) {
            return new ArrayList<>();
        }

        return sections;
    }

    public void setSections(List<Section> sections) {
        Gson gson = new Gson();
        mEditor.putString(SECTIONS_KEY, gson.toJson(sections)).commit();
    }
}
