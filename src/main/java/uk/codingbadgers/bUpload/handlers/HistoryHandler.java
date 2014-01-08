package uk.codingbadgers.bUpload.handlers;

import java.util.ArrayList;
import java.util.List;

import uk.codingbadgers.bUpload.UploadedImage;

public class HistoryHandler {

	public static List<UploadedImage> m_uploadHistory = new ArrayList<UploadedImage>();

	/**
	 * Add an image to our upload history.
	 * 
	 * @param newImage the new image
	 */
	public static void addUploadedImage(UploadedImage newImage) {
		synchronized (m_uploadHistory) {
			m_uploadHistory.add(newImage);
		}
	}

	/**
	 * Gets the uploaded image.
	 * 
	 * @param index the index
	 * @return the uploaded image
	 */
	public static UploadedImage getUploadedImage(int index) {
		synchronized (m_uploadHistory) {
			if (index >= m_uploadHistory.size()) {
				return null;
			}

			return m_uploadHistory.get(index);
		}
	}

	/**
	 * Upload history size.
	 * 
	 * @return the int
	 */
	public static int uploadHistorySize() {
		synchronized (m_uploadHistory) {
			return m_uploadHistory.size();
		}
	}
}
