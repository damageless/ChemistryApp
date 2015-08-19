package edu.weber.chemistryapp.controllers;

import android.content.Context;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import de.greenrobot.event.EventBus;
import edu.weber.chemistryapp.ElementView;
import edu.weber.chemistryapp.ElementLinkView;
import edu.weber.chemistryapp.models.Transition;
import edu.weber.chemistryapp.models.animations.Animation;

/**
 * Created by agessel on 6/6/15.
 */
public class TapStructureController extends AnimationController {
    private List<ElementView> mSelectedStructure;


    public TapStructureController(Context context, ViewGroup view, Animation animation) {
        super(context, view, animation);
    }

    private List<ElementView> selectLinkedElements(ElementView elementView, boolean selected) {
        List<ElementView> links = getLinkedElements(new CopyOnWriteArrayList<ElementView>(), elementView);
        for (ElementView linked : links) {
            linked.setSelected(selected);
        }

        return links;
    }

    public List<ElementView> getLinkedElements(List<ElementView> linkedElements, ElementView elementView) {
        List<ElementView> elements = new ArrayList<>();
        int size = linkedElements.size();

        for (ElementLinkView link : mElementLinkViews) {
            if (link.link1.equals(elementView) && !linkedElements.contains(link.link2)) {
                elements.add(link.link1);
                elements.add(link.link2);

                linkedElements.add(link.link2);
            }

            if (link.link2.equals(elementView) && !linkedElements.contains(link.link1)) {
                elements.add(link.link1);
                elements.add(link.link2);

                linkedElements.add(link.link1);
            }
        }

        if (linkedElements.size() != size) {
            for (ElementView link : linkedElements) {
                elements.addAll(getLinkedElements(linkedElements, link));
            }
        }

        return elements;
    }

    public class OnTransitionRequestedEvent {
        public UUID mGroupId1;
        public UUID mGroupId2;

        public OnTransitionRequestedEvent(UUID element1, UUID element2) {
            this.mGroupId1 = element1;
            this.mGroupId2 = element1;
        }
    }

    @Override
    public ElementView.Listener getElementListener() {
        return new ElementView.Listener() {
            @Override
            public void onAtomMoved(float x, float y) {

            }

            @Override
            public void onAtomClicked(final ElementView elementView) {
                if (mSelectedStructure != null && !mSelectedStructure.contains(elementView)) {
                    EventBus.getDefault().post(new OnTransitionRequestedEvent(elementView.getElement().mGroupId, mSelectedStructure.get(0).getElement().mGroupId));

                    selectLinkedElements(elementView, false);

                    for (ElementView linked : mSelectedStructure) {
                        selectLinkedElements(linked, false);
                    }

                    mSelectedStructure = null;
                } else {
                    mSelectedStructure = selectLinkedElements(elementView, true);
                }
            }
        };
    }
}
