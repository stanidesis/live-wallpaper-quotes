package com.stanleyidesis.livewallpaperquotes.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.stanleyidesis.livewallpaperquotes.LWQApplication;
import com.stanleyidesis.livewallpaperquotes.R;
import com.stanleyidesis.livewallpaperquotes.api.Callback;
import com.stanleyidesis.livewallpaperquotes.api.drawing.LWQSurfaceHolderDrawScript;
import com.stanleyidesis.livewallpaperquotes.api.event.PreferenceUpdateEvent;
import com.stanleyidesis.livewallpaperquotes.api.event.WallpaperEvent;

import de.greenrobot.event.EventBus;

/**
 * Copyright (c) 2015 Stanley Idesis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 * LWQWallpaperActivity.java
 * @author Stanley Idesis
 *
 * From Live-Wallpaper-Quotes
 * https://github.com/stanidesis/live-wallpaper-quotes
 *
 * Please report any issues
 * https://github.com/stanidesis/live-wallpaper-quotes/issues
 *
 * Date: 09/06/2015
 */
public abstract class LWQWallpaperActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    enum SilkScreenState {
        OBSCURED(.7f),
        REVEALED(0f),
        HIDDEN(1f);

        float screenAlpha;

        SilkScreenState(float screenAlpha) {
            this.screenAlpha = screenAlpha;
        }
    }

    LWQSurfaceHolderDrawScript drawScript;
    SurfaceView surfaceView;
    SilkScreenState silkScreenState;
    View silkScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreenIfPossible();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        silkScreen = findViewById(R.id.view_screen_lwq_wallpaper);
        surfaceView = (SurfaceView) findViewById(R.id.surface_lwq_wallpaper);
        surfaceView.getHolder().addCallback(this);
        animateSilkScreen(SilkScreenState.HIDDEN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!LWQApplication.getWallpaperController().activeWallpaperLoaded()) {
            LWQApplication.getWallpaperController().retrieveActiveWallpaper();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (!LWQApplication.getWallpaperController().activeWallpaperLoaded()) {
            return;
        }
        draw();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}

    public void onEvent(WallpaperEvent wallpaperEvent) {
        if (wallpaperEvent.didFail()) {
            return;
        }
        if (wallpaperEvent.getStatus() == WallpaperEvent.Status.RETRIEVED_WALLPAPER) {
            draw();
        }
    }

    public void onEvent(PreferenceUpdateEvent preferenceUpdateEvent) {
        if (preferenceUpdateEvent.getPreferenceKeyId() == R.string.preference_key_blur ||
                preferenceUpdateEvent.getPreferenceKeyId() == R.string.preference_key_dim) {
            draw();
        }
    }

    void didFinishDrawing() {
        // Nothing for now
    }

    void switchToSilkScreen(SilkScreenState state) {
        silkScreen.setAlpha(state.screenAlpha);
        silkScreenState = state;
    }

    long animateSilkScreen(SilkScreenState state) {
        ObjectAnimator silkScreenAnimator = ObjectAnimator.ofFloat(silkScreen, "alpha", silkScreen.getAlpha(), state.screenAlpha);
        silkScreenAnimator.setDuration(300);
        silkScreenAnimator.setInterpolator(new LinearInterpolator());
        silkScreenAnimator.start();
        silkScreenState = state;
        return 300;
    }

    void fullScreenIfPossible() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    void draw() {
        if (drawScript == null) {
            drawScript = new LWQSurfaceHolderDrawScript(surfaceView.getHolder());
        } else {
            drawScript.setSurfaceHolder(surfaceView.getHolder());
        }
        drawScript.requestDraw(new Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                didFinishDrawing();
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {}
        });
    }
}
