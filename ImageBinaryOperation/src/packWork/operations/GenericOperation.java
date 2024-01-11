package packWork.operations;

import packWork.ImageData;
import packWork.Pixel;

public abstract class GenericOperation implements Operation {
    protected abstract Pixel combinePixels(Pixel p1, Pixel p2);

    @Override
    public ImageData execute(ImageData image1, ImageData image2) {
        ImageData combinedImage = new ImageData(Math.max(image1.getWidth(), image2.getWidth()), Math.max(image1.getHeight(), image2.getHeight()));

        for (int i = 0; i < combinedImage.getHeight(); i++) {
            for (int j = 0; j < combinedImage.getWidth(); j++) {
                Pixel p1 = image1.getPixel(j, i);
                Pixel p2 = image2.getPixel(j, i);

                if (p1 != null && p2 != null) {
                    Pixel result = combinePixels(p1, p2);

                    combinedImage.setPixel(j, i, result);
                } else if (p1 != null) {
                    combinedImage.setPixel(j, i, p1);
                } else if (p2 != null) {
                    combinedImage.setPixel(j, i, p2);
                } else {
                    combinedImage.setPixel(j, i, new Pixel((byte) 0, (byte) 0, (byte) 0));
                }
            }
        }

        return combinedImage;
    }

    @Override
    public ImageData execute(ImageData... images) {
        if (images.length == 0) {
            return new ImageData(0, 0);
        }

        if (images.length == 1) {
            return images[0];
        }

        ImageData result = execute(images[0], images[1]);

        for (int i = 2; i < images.length; i++) {
            result = execute(result, images[i]);
        }

        return result;
    }
}
