package packWork.operations;

import packWork.image.ImageData;

public interface Operation {
    ImageData execute(ImageData image1, ImageData image2);

    ImageData execute(ImageData... images);
}
