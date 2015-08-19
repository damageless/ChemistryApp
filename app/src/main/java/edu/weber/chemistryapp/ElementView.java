package edu.weber.chemistryapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.weber.chemistryapp.models.elements.Charge;
import edu.weber.chemistryapp.models.elements.Element;

/**
 * Created by agessel on 5/21/15.
 */
public class ElementView extends TextView {
    private static final int CLICK_LENGTH = 250;

    private boolean mTapped = false;
    private boolean mSelected = false;

    private Element mElement;
    public boolean mHasCharge = false;

    private List<Listener> mListeners = new CopyOnWriteArrayList<>();


    public ElementView(Context context, Element element) {
        super(context);
        mElement = element;
        setText(mElement.mName);

        setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setTextSize(30);
        setX(element.x);
        setY(element.y);
    }

    private void createView(Context context) {
//        if (mLinkedElements != null) {
//            mLinkViews = new LinkedList<>();
//            for (ElementView elementView : mLinkedElements) {
//                mLinkViews.add(new ElementLinkView(context, this, elementView));
//            }
//        }
    }

    public Element getElement() {
        mElement.x = getX();
        mElement.y = getY();
        return mElement;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (mSelected) {
            setTextColor(Color.GREEN);
        } else if (mTapped) {
            setTextColor(Color.RED);
        } else {
            setTextColor(Color.BLACK);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mTapped = true;
        invalidate();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE: {
                if (canMove()) {
                    ElementView.this.setX(event.getRawX());
                    ElementView.this.setY(event.getRawY() - getHeight());

                    if (mListeners.size() > 0) {
                        for (Listener listener : mListeners) {
                            listener.onAtomMoved(event.getRawX(), event.getRawY());
                        }
                    }
//
//                        for (ElementLinkView link : mLinkViews) {
//                            link.invalidate();
//                        }
                }
            }
            break;
            case MotionEvent.ACTION_UP:
                mTapped = false;

                if ((event.getEventTime() - event.getDownTime() < CLICK_LENGTH || !canMove()) && mListeners.size() != 0) {
                    for (Listener listener : mListeners) {
                        listener.onAtomClicked(ElementView.this);
                    }
                }

        }

        return true;
    }

    private boolean canMove() {
        return mElement.mCanMove;
    }

    public void addListener(Listener listener) {
        if (this.mListeners == null) {
            this.mListeners = new CopyOnWriteArrayList<>();
        }
        this.mListeners.add(listener);
    }

    public void removeListener(Listener listener) {
        this.mListeners.remove(listener);
    }

    public interface Listener {
        void onAtomMoved(float x, float y);
        void onAtomClicked(ElementView elementView);
    }
}