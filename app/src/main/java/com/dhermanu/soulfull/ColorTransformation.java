package com.dhermanu.soulfull; /**
 * Created by dhermanu on 9/11/16.
 */
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.ColorInt;

import com.squareup.picasso.Transformation;

/**
 * -----------------------
 * ColorFadeTransform
 * -----------------------
 *
 * @author Tom Redman
 *         2016-04-12
 *
 * @description Will apply a colored fade to an image loaded with Picasso.
 * The color will fade from the bottom of the image upward to the percentage
 * specificed in fadeHeightPercentage.
 *
 * @usage Picasso.with(context).load(imageUrl).transform(new ColorFadeTransform(Color.RED, 25)).into(imageView);
 */

public class ColorTransformation implements Transformation {

    private int fadeColor = 0xFF000000;
    private int fadeColorAlpha = 0x000000;
    private int fadeHeightPercentage = 50;

    public ColorTransformation() {
        this(Color.BLACK, 50);
    }

    public ColorTransformation(@ColorInt int fadeColor, int fadeHeightPercentage) {
        int red = (fadeColor >> 16) & 0xFF;
        int green = (fadeColor >> 8) & 0xFF;
        int blue = fadeColor & 0xFF;
        this.fadeColor = Color.argb(255, red, green, blue);
        this.fadeColorAlpha = Color.argb(0, red, green, blue);
        this.fadeHeightPercentage = fadeHeightPercentage;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap resultBitmap = Bitmap.createBitmap(source).copy(Bitmap.Config.ARGB_8888, true);
        source.recycle();
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, null);
        int gradientHeight = (int)(resultBitmap.getHeight() / (100.0f / (double)fadeHeightPercentage));
        Paint paint = new Paint();
        LinearGradient linearGradient = new LinearGradient(0, resultBitmap.getHeight() - gradientHeight, 0, resultBitmap.getHeight(), fadeColorAlpha, fadeColor, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        canvas.drawRect(0, resultBitmap.getHeight() - gradientHeight, resultBitmap.getWidth(), resultBitmap.getHeight(), paint);
        return resultBitmap;
    }

    @Override
    public String key() {
        return "ColorFadeTransform";
    }
}