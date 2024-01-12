package packWork.image;

import packWork.exceptions.InvalidArgumentException;
import packWork.image.ImageData;

public interface ImageSaver {
    void saveImage(String filename, ImageData image) throws InvalidArgumentException;
}
