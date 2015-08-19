package edu.weber.chemistryapp.models.animations;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.weber.chemistryapp.ElementView;
import edu.weber.chemistryapp.ElementLinkView;
import edu.weber.chemistryapp.models.Transition;
import edu.weber.chemistryapp.models.elements.Element;
import edu.weber.chemistryapp.models.elements.Link;

/**
 * Created by agessel on 6/5/15.
 */
public class Animation {
    @SerializedName("Description")
    public String mDescription;
    @SerializedName("Type")
    public final String mType;

    @SerializedName("Elements")
    public List<Element> mElements = new LinkedList<>();
    @SerializedName("Links")
    public List<Link> mLinks = new LinkedList<>();
    @SerializedName("Transitions")
    public List<Transition> mTransitions = new ArrayList<>();

    public Animation(String type, String description) {
        this.mDescription = description;
        this.mType = type;
    }

    public void addElementToStructure(Element element) {
        mElements.add(element);
    }

    public Transition findTransitionWithName(String name) {
        for (Transition transition : mTransitions) {
            if (transition.mName.equals(name)) {
                return transition;
            }
        }

        return null;
    }
}

