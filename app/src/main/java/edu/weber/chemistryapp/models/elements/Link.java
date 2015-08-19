package edu.weber.chemistryapp.models.elements;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by agessel on 7/9/15.
 */
public class Link {
    @SerializedName("Link1")
    public UUID link1;
    @SerializedName("Link2")
    public UUID link2;

    public Link(UUID link1, UUID link2) {
        this.link1 = link1;
        this.link2 = link2;
    }
}
