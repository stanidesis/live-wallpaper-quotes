package com.stanleyidesis.quotograph.api.db;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;

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
 * PlaylistCategory.java
 * @author Stanley Idesis
 *
 * From Quotograph
 * https://github.com/stanidesis/quotograph
 *
 * Please report any issues
 * https://github.com/stanidesis/quotograph/issues
 *
 * Date: 10/03/2015
 */
public class PlaylistCategory extends SugarRecord implements Comparable<PlaylistCategory> {
    public Playlist playlist;
    public Category category;

    public PlaylistCategory() {}

    public PlaylistCategory(Playlist playlist, Category category) {
        this.playlist = playlist;
        this.category = category;
    }

    public static List<PlaylistCategory> forPlaylist(Playlist playlist) {
        return Select.from(PlaylistCategory.class).where(Condition.prop(NamingHelper.toSQLNameDefault("playlist")).eq(playlist.getId())).list();
    }

    public static PlaylistCategory find(Playlist playlist, Category category) {
        return Select.from(PlaylistCategory.class).where(Condition.prop(NamingHelper.toSQLNameDefault("playlist")).eq(playlist.getId()),
                Condition.prop(NamingHelper.toSQLNameDefault("category")).eq(category.getId())).first();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PlaylistCategory) {
            return playlist.getId() == ((PlaylistCategory) o).playlist.getId()
                    && category.getId() == ((PlaylistCategory) o).category.getId();
        }
        return super.equals(o);
    }

    @Override
    public int compareTo(PlaylistCategory playlistCategory) {
        return category.name.compareToIgnoreCase(playlistCategory.category.name);
    }
}
