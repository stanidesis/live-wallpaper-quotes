package com.stanleyidesis.quotograph.api.event;

import com.stanleyidesis.quotograph.api.LWQError;

/**
 * Copyright (c) 2016 Stanley Idesis
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
 * WallpaperEvent.java
 * @author Stanley Idesis
 *
 * From Quotograph
 * https://github.com/stanidesis/quotograph
 *
 * Please report any issues
 * https://github.com/stanidesis/quotograph/issues
 *
 * Date: 09/27/2015
 */
public class WallpaperEvent extends FailableEvent {

    public enum Status {
        GENERATING_NEW_WALLPAPER,
        GENERATED_NEW_WALLPAPER,
        RETRIEVING_WALLPAPER,
        RETRIEVED_WALLPAPER,
        RENDERED_WALLPAPER;
    }

    public static WallpaperEvent withStatus(Status status) {
        return new WallpaperEvent(status);
    }

    public static WallpaperEvent failedWithStatus(Status status, LWQError error) {
        final WallpaperEvent wallpaperEvent = new WallpaperEvent(status);
        wallpaperEvent.error = error;
        return wallpaperEvent;
    }

    public Status getStatus() {
        return status;
    }

    Status status;

    WallpaperEvent(Status status) {
        this.status = status;
    }

}
