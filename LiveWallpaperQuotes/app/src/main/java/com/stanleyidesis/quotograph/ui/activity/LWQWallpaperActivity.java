package com.stanleyidesis.quotograph.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.stanleyidesis.quotograph.R;
import com.stanleyidesis.quotograph.ui.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
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
public abstract class LWQWallpaperActivity extends AppCompatActivity implements ActivityStateFlags {

    enum BackgroundWallpaperState {
        CUSTOM(0f),
        OBSCURED(.7f),
        REVEALED(0f),
        HIDDEN(1f);

        float screenAlpha;

        BackgroundWallpaperState(float screenAlpha) {
            this.screenAlpha = screenAlpha;
        }
    }

    @Bind(R.id.view_screen_lwq_wallpaper) View silkScreen;
    BackgroundWallpaperState backgroundWallpaperState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.setupFullscreenIfPossible(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ButterKnife.bind(this);
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

    // Setup

    void switchToState(BackgroundWallpaperState backgroundWallpaperState) {
        silkScreen.setAlpha(backgroundWallpaperState.screenAlpha);
        this.backgroundWallpaperState = backgroundWallpaperState;
    }

    long animateState(BackgroundWallpaperState backgroundWallpaperState) {
        silkScreen.animate().alpha(backgroundWallpaperState.screenAlpha).setDuration(300l)
                .setInterpolator(new AccelerateDecelerateInterpolator()).start();
        this.backgroundWallpaperState = backgroundWallpaperState;
        return 300l;
    }

    void setBackgroundAlpha(float alpha) {
        silkScreen.setAlpha(alpha);
        backgroundWallpaperState = BackgroundWallpaperState.CUSTOM;
    }

}
