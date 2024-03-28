package com.augmentaa.sparkev.utils;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;

public class CurveBottomDrawable extends Drawable {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();

    public CurveBottomDrawable(int color) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
    }

    @Override
    public void draw( Canvas canvas) {

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xffff0000);

        path.reset();
        Rect bounds = getBounds();

        float horizontalOffset = bounds.width() * .8f;
        float top = -bounds.height() * .8f;
        float bottom = bounds.height();

        RectF ovalRect = new RectF(-horizontalOffset, top, bounds.width() + horizontalOffset, bottom);
        path.lineTo(ovalRect.left, top);
        path.arcTo(ovalRect, 0, 180, false);
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);

        canvas.drawPath(path, paint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

}