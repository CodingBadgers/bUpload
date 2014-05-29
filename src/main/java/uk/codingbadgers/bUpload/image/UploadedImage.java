/*
 *  bUpload - a minecraft mod which improves the existing screenshot functionality
 *  Copyright (C) 2013 TheCodingBadgers
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */
package uk.codingbadgers.bUpload.image;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a saved image.
 */
public class UploadedImage {
    private final String name;
    private final List<ImageSource> source;
    private final int imageID;

    /**
     * Instantiates a new uploaded image.
     *
     * @param name      the name
     * @param url       the url of the image
     * @param image     the image
     * @param localFile the local file
     */
    public UploadedImage(String name, String url, Screenshot image, ImageSource source) {
        this.name = name;
        this.imageID = image.imageID;
        this.source = new ArrayList<ImageSource>(Arrays.asList(source));
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the image id.
     *
     * @return the image id
     */
    public int getImageID() {
        return imageID;
    }

    public ImageSource getSource() {
        return source.get(0);
    }

    public List<ImageSource> getSources() {
        return ImmutableList.copyOf(source);
    }

    public void addSource(ImageSource source) {
        this.source.add(source);
    }
}
