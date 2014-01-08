package uk.codingbadgers.bUpload.handlers.upload;

import uk.codingbadgers.bUpload.Screenshot;

public abstract class UploadHandler implements Runnable {

    private final Screenshot screenshot;
    
    public UploadHandler(Screenshot screen) {
        this.screenshot = screen;
    }
    
    public final void run() {
        run(screenshot);
    }
    
    protected abstract boolean run(Screenshot screenshot);
    
}
