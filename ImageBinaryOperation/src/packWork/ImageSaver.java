package packWork;

import java.io.IOException;

public interface ImageSaver {
    void saveImage(String filename, ImageData image) throws IOException;
}
