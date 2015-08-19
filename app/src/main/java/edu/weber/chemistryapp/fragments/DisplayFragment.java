package edu.weber.chemistryapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.LinkedList;
import java.util.List;

import de.greenrobot.event.EventBus;
import edu.weber.chemistryapp.R;
import edu.weber.chemistryapp.Utils;
import edu.weber.chemistryapp.controllers.AnimationController;
import edu.weber.chemistryapp.controllers.TapMultipleElementsController;
import edu.weber.chemistryapp.controllers.TapStructureController;
import edu.weber.chemistryapp.loader.Parser;
import edu.weber.chemistryapp.models.animations.Animation;
import edu.weber.chemistryapp.models.Transition;
import edu.weber.chemistryapp.persistence.Preferences;

public class DisplayFragment extends Fragment {
    public static final String ARGS_SECTION_NAME = "args_section_name";

    private List<Animation> mAvailableAnimations = new LinkedList<>();
    AnimationController mController;

    private String mSectionName;

    public static DisplayFragment createInstance(String name) {
        DisplayFragment fragment = new DisplayFragment();

        Bundle args = new Bundle();
        args.putString(ARGS_SECTION_NAME, name);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.fragment_acid1_a, container, false);
        EventBus.getDefault().register(this);

        mSectionName = getArguments().getString(ARGS_SECTION_NAME);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Parser parser = new Parser();
        Preferences preferences = Preferences.createInstance(getActivity());

        if (!Utils.isNullOrEmpty(preferences.getAnimations())) {
            mAvailableAnimations = parser.parseAnimations(preferences.getAnimations());
        }

        doTransition(new Transition(mSectionName));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void doTransition(Transition transition) {
        Animation animation = findAnimationWithName(transition.mTransitionTo);

        if (animation == null) {
            return;
        }

        ((ViewGroup) getView()).removeAllViews();

        switch (animation.mType) {
            case "TapMultipleElements":
                mController = new TapMultipleElementsController(getActivity(), (ViewGroup) getView(), animation);
                break;
            case "TapStructure":
                mController = new TapStructureController(getActivity(), (ViewGroup) getView(), animation);
        }
    }

    private Animation findAnimationWithName(String name) {
        for (Animation animation : mAvailableAnimations) {
            if (animation.findTransitionWithName(name) != null) {
                return animation;
            }
        }

        return null;
    }

    public void onEvent(TapStructureController.OnTransitionRequestedEvent event) {
        for (Animation animation : mAvailableAnimations) {
            for (Transition transition : animation.mTransitions) {
                if (transition.mElements != null && transition.mElements.contains(event.mGroupId1.toString()) && transition.mElements.contains(event.mGroupId2.toString())) {
                    doTransition(transition);
                }
            }
        }
    }

    public void onEvent(TapMultipleElementsController.OnTransitionRequestedEvent event) {
        for (Animation animation : mAvailableAnimations) {
            for (Transition transition : animation.mTransitions) {
                if (transition.mElements != null && transition.mElements.contains(event.mElement1.toString()) && transition.mElements.contains(event.mElement2.toString())) {
                    doTransition(transition);
                }
            }
        }
    }

}
