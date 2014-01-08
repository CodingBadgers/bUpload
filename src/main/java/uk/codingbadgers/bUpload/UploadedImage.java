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
package uk.codingbadgers.bUpload;

/**
 * Represents a saved image.
 */
public class UploadedImage {
	private final String m_name;
	private final String m_url;
	private final int m_imageID;
	private final boolean m_localFile;

	/**
	 * Instantiates a new uploaded image.
	 * 
	 * @param name the name
	 * @param url the url of the image
	 * @param image the image
	 * @param localFile the local file
	 */
	public UploadedImage(String name, String url, Screenshot image, boolean localFile) {
		m_name = name;
		m_url = url;
		m_imageID = image.imageID;
		m_localFile = localFile;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * Gets the url.
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return m_url;
	}

	/**
	 * Gets the image id.
	 * 
	 * @return the image id
	 */
	public int getImageID() {
		return m_imageID;
	}

	/**
	 * Checks if is local.
	 * 
	 * @return true, if is local
	 */
	public boolean isLocal() {
		return m_localFile;
	}
}
