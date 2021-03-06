package com.dhermanu.soulfull; /**
 * Created by dhermanu on 9/11/16.
 */
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

public class ShadeTransformation implements Transformation {

    /*reference: https://gist.github.com/r0adkll/6bbc77a58e397b50ea40
    * courtesy of: r0adkll*/

    /* The shade alpha of black to apply */
    private int mAlpha;

    /**
     * Integer Constructor
     *
     * @param alpha     the alpha shade to apply
     */
    public ShadeTransformation(int alpha){
        // Clamp the alpha value to 0..255
        mAlpha = Math.max(0, Math.min(255, alpha));
    }

    /**
     * Float Constructor
     *
     * @param percent   the alpha percentage from 0..1
     */
    public ShadeTransformation(float percent){
        // Clamp the float value to 0..1
        mAlpha = (int)((Math.max(0, Math.min(1, percent))) * 255);
    }

    /**
     * Transform the source bitmap into a new bitmap. If you create a new bitmap instance, you must
     * call {@link android.graphics.Bitmap#recycle()} on {@code source}. You may return the original
     * if no transformation is required.
     *
     * @param source
     */
    @Override
    public Bitmap transform(Bitmap source) {
        Paint paint = new Paint();
        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);

        // Create canvas and draw the source image
        Canvas canvas = new Canvas(output);
        canvas.drawBitmap(source, 0, 0, paint);

        // Setup the paint for painting the shade
        paint.setColor(Color.DKGRAY);
        paint.setAlpha(mAlpha);

        // Paint the shade
        canvas.drawPaint(paint);

        // Recycle and return
        source.recycle();
        return output;
    }

    /**
     * Returns a unique key for the transformation, used for caching purposes. If the transformation
     * has parameters (e.g. size, scale factor, etc) then these should be part of the key.
     */
    @Override
    public String key() {
        return "shade:" + mAlpha;
    }
}
