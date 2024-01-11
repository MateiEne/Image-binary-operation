package packWork.operations;

import packWork.ImageData;

import java.util.List;

public interface Operation {
    ImageData execute(ImageData image1, ImageData image2);

    ImageData execute(ImageData... images);
}
