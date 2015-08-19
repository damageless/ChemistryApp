package edu.weber.chemistryapp.loader;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.weber.chemistryapp.ElementView;
import edu.weber.chemistryapp.Utils;
import edu.weber.chemistryapp.models.animations.Animation;
import edu.weber.chemistryapp.models.animations.Animations;
import edu.weber.chemistryapp.models.animations.TapMultipleElements;
import edu.weber.chemistryapp.models.Transition;

/**
 * Created by agessel on 6/5/15.
 */
public class Parser {
    /*
    Tap Structure Json:
    {
  "Element": [
    {
      "Name": "H₃C",
      "OriginX": 250,
      "OriginY": 100,
      "Links": [
        {
          "Name": "C\nH₂",
          "OriginX": 50,
          "OriginY": 400
        }
      ]
    }
  ],
  "Type": "TapStructure",
  "Name": "Structure1"
}




    TapMultiple Json:
    {
  "Element": [
    {
      "Name": "H₃C",
      "X": 250,
      "Y": 300,
      "Links": [
        {
          "Name": "C\nH₂",
          "X": 250,
          "Y": 300
        }
      ]
    }
  ],
  "Combinations": [
    [
      {
        "TransitionName": "Transiton1",
        "Elements": [
            "H₃C",
            "C\nH₂"
        ],
        "TransitionTo": "Transition2"
      }
    ]
  ],
  "Type": "TapMultipleElements",
  "Name": "Transition3",
  "NumTaps": 2
}


     */


    String tapStructureJson = "{\n" +
            "  \"Structures\": [\n" +
            "    {\n" +
            "      \"Name\": \"H₃C\",\n" +
            "      \"OriginX\": 250,\n" +
            "      \"OriginY\": 100,\n" +
            "      \"Links\": [\n" +
            "        {\n" +
            "          \"Name\": \"C\\nH₂\",\n" +
            "          \"OriginX\": 50,\n" +
            "          \"OriginY\": 400\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"Type\": \"TapStructure\",\n" +
            "  \"Name\": \"Structure1\"\n" +
            "}";

    String tapMultipleJson = "{\n" +
            "  \"Animations\": [\n" +
            "    {\n" +
            "\t  \"Transition\": {\n" +
            "\t  \t\"Name\": \"InitialAnimation\",\n" +
            "\t  \t\"TransitionTo\": \"FirstTransition\"\n" +
            "\t  },\n" +
            "\t  \"Structures\": [\n" +
            "\t    {\n" +
            "\t      \"Id\": \"23\",\n" +
            "\t      \"Name\": \"H₃C\",\n" +
            "\t      \"X\": 250,\n" +
            "\t      \"Y\": 300,\n" +
            "\t      \"Links\": [\n" +
            "\t        {\n" +
            "\t          \"Id\": \"33\",\n" +
            "\t          \"Name\": \"C\\nH₂\",\n" +
            "\t          \"X\": 250,\n" +
            "\t          \"Y\": 600\n" +
            "\t        },\n" +
            "\t         {\n" +
            "\t          \"Id\": \"53\",\n" +
            "\t          \"Name\": \"O₂\",\n" +
            "\t          \"X\": 450,\n" +
            "\t          \"Y\": 900\n" +
            "\t        }\n" +
            "\t      ]\n" +
            "\t    }\n" +
            "\t  ],\n" +
            "\t  \"AvailableTransitions\": [\n" +
            "\t     {\n" +
            "\t        \"Name\": \"Transition1\",\n" +
            "\t        \"ForElements\": [\n" +
            "\t            \"23\",\n" +
            "\t            \"33\"\n" +
            "\t        ],\n" +
            "\t        \"TransitionTo\": \"Transition2\"\n" +
            "\t      },\n" +
            "\t      {\n" +
            "\t        \"Name\": \"Transition2\",\n" +
            "\t        \"ForElements\": [\n" +
            "\t            \"33\",\n" +
            "\t            \"53\"\n" +
            "\t        ],\n" +
            "\t        \"TransitionTo\": \"Transition2\"\n" +
            "\t      }\n" +
            "\t  ],\n" +
            "\t  \"Type\": \"TapMultipleElements\",\n" +
            "\t  \"Name\": \"Transition3\",\n" +
            "\t  \"NumTaps\": 2\n" +
            "    },\n" +
            "    \n" +
            "    \n" +
            "    \n" +
            "    {\n" +
            "\t  \"Transition\": {\n" +
            "\t  \t\"Name\": \"Transition2\",\n" +
            "\t  \t\"TransitionTo\": \"InitialAnimation\"\n" +
            "\t  },\n" +
            "\t  \"Structures\": [\n" +
            "\t    {\n" +
            "\t      \"Id\": \"13\",\n" +
            "\t      \"Name\": \"ADAM\",\n" +
            "\t      \"X\": 350,\n" +
            "\t      \"Y\": 100,\n" +
            "\t      \"Links\": [\n" +
            "\t        {\n" +
            "\t          \"Id\": \"20\",\n" +
            "\t          \"Name\": \"Raquel\",\n" +
            "\t          \"X\": 250,\n" +
            "\t          \"Y\": 800\n" +
            "\t        },\n" +
            "\t         {\n" +
            "\t          \"Id\": \"21\",\n" +
            "\t          \"Name\": \"Brielle\",\n" +
            "\t          \"X\": 450,\n" +
            "\t          \"Y\": 1200\n" +
            "\t        }\n" +
            "\t      ]\n" +
            "\t    }\n" +
            "\t  ],\n" +
            "\t  \"AvailableTransitions\": [\n" +
            "\t     {\n" +
            "\t        \"Name\": \"Transition1\",\n" +
            "\t        \"ForElements\": [\n" +
            "\t            \"13\",\n" +
            "\t            \"20\"\n" +
            "\t        ],\n" +
            "\t        \"TransitionTo\": \"AdamRaquelTransition\"\n" +
            "\t      },\n" +
            "\t      {\n" +
            "\t        \"Name\": \"Transition2\",\n" +
            "\t        \"ForElements\": [\n" +
            "\t            \"21\",\n" +
            "\t            \"20\"\n" +
            "\t        ],\n" +
            "\t        \"TransitionTo\": \"InitialAnimation\"\n" +
            "\t      }\n" +
            "\t  ],\n" +
            "\t  \"Type\": \"TapMultipleElements\",\n" +
            "\t  \"Name\": \"Transition3\",\n" +
            "\t  \"NumTaps\": 2\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public List<Animation> parseAnimations(String animationsJson) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();

        if (!Utils.isNullOrEmpty(animationsJson)) {
            return gson.fromJson(animationsJson, Animations.class).animations;
        }

        return new ArrayList<>();
    }
}
