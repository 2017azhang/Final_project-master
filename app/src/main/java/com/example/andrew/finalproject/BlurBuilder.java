package com.example.andrew.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by 2017azhang on 6/15/2016.
 */
public class BlurBuilder {
    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 7.5f;

    public static Bitmap blur(Context context, Bitmap image){
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap input = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap output = Bitmap.createBitmap(input);

        RenderScript script = RenderScript.create(context);
        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(script, Element.U8_4(script));
        Allocation in = Allocation.createFromBitmap(script, input);
        Allocation out = Allocation.createFromBitmap(script, output);
        intrinsicBlur.setRadius(BLUR_RADIUS);
        intrinsicBlur.setInput(in);
        intrinsicBlur.forEach(out);
        out.copyTo(output);

        return output;
    }
}
