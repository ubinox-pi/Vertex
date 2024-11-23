package com.vertex.io;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Bird {
    public int x, y, width, height;
    private Bitmap bird;
    private int screenX, screenY;

    public Bird(Context context,int  screenX, int screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
        bird = BitmapFactory.decodeResource(context.getResources(), R.drawable.bird);
        width = bird.getWidth() / 4;
        height = bird.getHeight() / 4;
        bird = Bitmap.createScaledBitmap(bird, width, height, false);
        x = screenX / 4;
        y = screenY / 2;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bird, x, y, paint);
    }

    public void update() {
        y += 10;  // Falling effect
    }

    public void flap() {
        y -= 30;  // Jumping effect
    }
}

