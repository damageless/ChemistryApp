package edu.weber.chemistryapp.controllers;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import edu.weber.chemistryapp.ChargeView;
import edu.weber.chemistryapp.ElementLinkView;
import edu.weber.chemistryapp.ElementView;
import edu.weber.chemistryapp.models.animations.Animation;
import edu.weber.chemistryapp.models.elements.Element;
import edu.weber.chemistryapp.models.elements.Link;

/**
 * Created by agessel on 6/6/15.
 */
public abstract class AnimationController {
    public ViewGroup mView;
    private Context mContext;
    public Animation mAnimation;

    private List<ElementView> mElementViews = new LinkedList<>();
    public List<ElementLinkView> mElementLinkViews = new LinkedList<>();

    public AnimationController(Context context, ViewGroup mView, Animation animation) {
        this.mContext = context;
        this.mAnimation = animation;
        this.mView = mView;

        drawStructure();
    }

    public void onElementDrawn(ElementView elementView) {

    }

    public void drawStructure() {
        int i = 0;
        for (Element element : mAnimation.mElements) {
            ElementView elementView = new ElementView(mContext, element);
            elementView.setX(element.x);
            elementView.setY(element.y);
            elementView.addListener(getElementListener());
            mElementViews.add(elementView);
            mView.addView(mElementViews.get(i));

            if (elementView.getElement().mCharge != null) {
                ChargeView chargeView = new ChargeView(mContext);
                chargeView.setLinkedElementView(elementView);
                mView.addView(chargeView);
            }

            onElementDrawn(elementView);

            i++;
        }

        for (Link link : mAnimation.mLinks) {
            ElementLinkView linkView = new ElementLinkView(mContext, getElementViewByUUID(link.link1), getElementViewByUUID(link.link2));
            mElementLinkViews.add(linkView);
            mView.addView(linkView);
        }

        showDescription();
    }

    private void showDescription() {
        TextView descriptionTextView = new TextView(mContext);
        descriptionTextView.setText(mAnimation.mDescription);
        descriptionTextView.setX(100);
        descriptionTextView.setY(75);
        descriptionTextView.setTextColor(mContext.getResources().getColor(android.R.color.black));
        descriptionTextView.setTextSize(18);
        descriptionTextView.setSingleLine(false);
        descriptionTextView.setLines(5);
        descriptionTextView.setMaxWidth(900);
        mView.addView(descriptionTextView);
    }

    public ElementView getElementViewByUUID(UUID uuid) {
        for (ElementView elementView : mElementViews) {
            if (elementView.getElement().mElementId.equals(uuid)) {
                return elementView;
            }
        }

        return null;
    }

    public abstract ElementView.Listener getElementListener();
}
