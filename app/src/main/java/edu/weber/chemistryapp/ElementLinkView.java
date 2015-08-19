package edu.weber.chemistryapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.UUID;

import edu.weber.chemistryapp.persistence.Exclude;

/**
 * Created by agessel on 5/21/15.
 */
public class ElementLinkView extends View {
    Paint mPaint = new Paint();

    public ElementView link1;
    public ElementView link2;

    public ElementLinkView(Context context, ElementView link1, ElementView link2) {
        super(context);
        this.link1 = link1;
        this.link2 = link2;

        this.link1.addListener(new ElementView.Listener() {
            @Override
            public void onAtomMoved(float x, float y) {
                invalidate();
            }

            @Override
            public void onAtomClicked(ElementView elementView) {

            }
        });

        this.link2.addListener(new ElementView.Listener() {
            @Override
            public void onAtomMoved(float x, float y) {
                invalidate();
            }

            @Override
            public void onAtomClicked(ElementView elementView) {

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(10);

        float startX;
        float endX;

        float startY;
        float endY;

        if (link1.getX() > link2.getX()) {
            startX = link1.getX();
            endX = link2.getX() + link2.getWidth();
        } else {
            startX = link1.getX() + link1.getWidth();
            endX = link2.getX();
        }

        if (link1.getY() > link2.getY()) {
            startY = link1.getY();
            endY = link2.getY() + link2.getHeight();
        } else {
            startY = link1.getY() + link1.getHeight();
            endY = link2.getY();
        }

        canvas.drawLine(startX, startY, endX, endY, mPaint);
    }
}