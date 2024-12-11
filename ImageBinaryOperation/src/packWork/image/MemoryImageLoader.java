package packWork.image;

import packWork.exceptions.InvalidArgumentException;

import java.util.List;

public interface MemoryImageLoader {
    ImageData loadImage(List<Byte> image) throws InvalidArgumentException;
}
