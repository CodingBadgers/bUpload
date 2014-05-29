package uk.codingbadgers.bUpload.handlers;

import uk.codingbadgers.bUpload.image.UploadedImage;

import java.util.ArrayList;
import java.util.List;

public class HistoryHandler {

    public static List<UploadedImage> m_uploadHistory = new ArrayList<UploadedImage>();

    /**
     * Add an image to our upload history.
     *
     * @param newImage the new image
     */
    public static void addUploadedImage(UploadedImage newImage) {
        synchronized (m_uploadHistory) {

            for (UploadedImage image : m_uploadHistory) {
                if (image.getName().equals(newImage.getName()) && image.getImageID() == newImage.getImageID()) {
                    image.addSource(newImage.getSource());
                    return;
                }
            }

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
