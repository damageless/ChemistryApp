package edu.weber.chemistryapp.controllers;

import android.content.Context;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import de.greenrobot.event.EventBus;
import edu.weber.chemistryapp.ElementView;
import edu.weber.chemistryapp.models.Transition;
import edu.weber.chemistryapp.models.animations.Animation;
import edu.weber.chemistryapp.models.animations.TapMultipleElements;
import edu.weber.chemistryapp.models.elements.Element;

/**
 * Created by agessel on 6/5/15.
 */
public class TapMultipleElementsController extends AnimationController {
    private List<Transition> mTransitions = new LinkedList<>();

    private List<SelectedItem> mSelectedItems = new LinkedList<>();

    public TapMultipleElementsController(Context context, ViewGroup view, Animation animation) {
        super(context, view, animation);
    }

    @Override
    public void onElementDrawn(ElementView elementView) {
        elementView.getElement().mCanMove = false;
    }

    @Override
    public ElementView.Listener getElementListener() {
        return new ElementView.Listener() {
            @Override
            public void onAtomMoved(float x, float y) {

            }

            @Override
            public void onAtomClicked(ElementView elementView) {
                mSelectedItems.add(new SelectedItem(elementView, elementView.getElement().mElementId));

                if (mSelectedItems.size() < 2) {
                    elementView.setSelected(true);
                    elementView.invalidate();
                } else {
                    for (SelectedItem selectedItem : mSelectedItems) {
                        selectedItem.elementView.setSelected(false);
                        selectedItem.elementView.invalidate();
                    }

                    EventBus.getDefault().post(new OnTransitionRequestedEvent(mSelectedItems.get(0).elementId, mSelectedItems.get(1).elementId));

                    mSelectedItems.clear();
                }
            }
        };
    }

    private Transition getTransition(List<UUID> elementIds) {
        for (Transition transition : mTransitions) {
            if (transition.mElements.containsAll(elementIds)) {
                return transition;
            }
        }

        return null;
    }

    private List<UUID> getSelectedElementIds(List<SelectedItem> selectedItems) {
        List<UUID> ids = new LinkedList<>();

        for (SelectedItem item : selectedItems) {
            ids.add(item.elementId);
        }

        return ids;
    }

    private class SelectedItem {
        ElementView elementView;
        UUID elementId;

        public SelectedItem(ElementView elementView, UUID elementId) {
            this.elementView = elementView;
            this.elementId = elementId;
        }
    }

    public class OnTransitionRequestedEvent {
        public UUID mElement1;
        public UUID mElement2;

        public OnTransitionRequestedEvent(UUID element1, UUID element2) {
            this.mElement1 = element1;
            this.mElement2 = element1;
        }
    }
}
