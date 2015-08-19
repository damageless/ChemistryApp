package edu.weber.chemistryapp;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by adamgessel on 8/9/15.
 */
public class ChargeView extends TextView {
    private List<Listener> mListeners = new CopyOnWriteArrayList<>();

    private boolean mCanMove = false;


    public ChargeView(Context context) {
        super(context);
        setText("+");
        setTextColor(getResources().getColor(android.R.color.black));
        setBackgroundResource(R.drawable.circle_background);
        setHeight(75);
        setWidth(75);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.CENTER);
    }


    public void setLinkedElementView(final ElementView linkedElementView) {
        setX(linkedElementView.getX() + linkedElementView.getWidth());
        setY(linkedElementView.getY() - 75);

        linkedElementView.addListener(new ElementView.Listener() {
            @Override
            public void onAtomMoved(float x, float y) {
                setX(x);
                setY(y - getHeight() - 125);
            }

            @Override
            public void onAtomClicked(ElementView elementView) {

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        invalidate();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE: {
                if (canMove()) {
                    ChargeView.this.setX(event.getRawX());
                    ChargeView.this.setY(event.getRawY() - getHeight());

                    if (mListeners.size() > 0) {
                        for (Listener listener : mListeners) {
                            listener.onChargeMoved(event.getRawX(), event.getRawY());
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

                if (mListeners.size() != 0) {
                    for (Listener listener : mListeners) {
                        listener.onChargeDoneMoving(event.getRawX(), event.getRawY());
                    }
                }

        }

        return true;
    }

    private boolean canMove() {
        return mCanMove;
    }


    public interface Listener {
        void onChargeMoved(float x, float y);
        void onChargeDoneMoving(float x, float y);
    }
}
