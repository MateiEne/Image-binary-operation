package packWork.image;

import packWork.exceptions.InvalidArgumentException;
import packWork.image.ImageData;

public interface ImageLoader {
    ImageData loadImage(String filename) throws InvalidArgumentException;
}
