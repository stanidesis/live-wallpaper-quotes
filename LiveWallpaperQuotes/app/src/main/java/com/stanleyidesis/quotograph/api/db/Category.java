package com.stanleyidesis.quotograph.api.db;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.Random;

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
 * Category.java
 * @author Stanley Idesis
 *
 * From Quotograph
 * https://github.com/stanidesis/quotograph
 *
 * Please report any issues
 * https://github.com/stanidesis/quotograph/issues
 *
 * Date: 07/11/2015
 */
public class Category extends SugarRecord {
    public enum Source {
        DEFAULT,
        BRAINY_QUOTE;
    }

    public String name;
    public Source source;

    public Category() {}

    public Category(String name, Source source) {
        this.name = name;
        this.source = source;
    }

    public static boolean hasCategory(String name, Source source) {
        final long count = Select.from(Category.class)
                .where(Condition.prop(NamingHelper.toSQLNameDefault("name")).eq(name),
                        Condition.prop(NamingHelper.toSQLNameDefault("source")).eq(source)).count();
        return count > 0;
    }

    public static Category random() {
        final long count = Select.from(Category.class).count();
        final int offset = new Random().nextInt((int) count);
        return Category.findWithQuery(Category.class, "Select * from Category LIMIT 1 OFFSET " + offset, (String []) null).get(0);
    }

    public static Category findWithName(String name) {
        return Select.from(Category.class).where(Condition.prop(NamingHelper.toSQLNameDefault("name")).eq(name)).first();
    }
}
