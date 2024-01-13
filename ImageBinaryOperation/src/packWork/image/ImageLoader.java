package packWork.image;

import packWork.exceptions.InvalidArgumentException;

public interface ImageLoader {
    ImageData loadImage(String filename) throws InvalidArgumentException;
}
