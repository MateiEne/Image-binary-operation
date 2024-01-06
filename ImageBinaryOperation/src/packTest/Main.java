package packTest;

import packWork.BMPHandler.BMPImageLoader;
import packWork.BMPHandler.BMPImageSaver;
import packWork.ImageData;
import packWork.ImageLoader;
import packWork.ImageSaver;
import packWork.Pixel;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws Exception {
        ImageLoader imageLoader = new BMPImageLoader();

        ImageData snowboarderImage = imageLoader.loadImage("images/test.bmp");
        ImageData skierImage = imageLoader.loadImage("images/sch.bmp");

        ImageData combinedImage = combineImages(snowboarderImage, skierImage);

        ImageSaver imageSaver = new BMPImageSaver();

        imageSaver.saveImage("images/combined.bmp", combinedImage);
    }

    private static ImageData combineImages(ImageData image1, ImageData image2) {
        // Combine image1 and image2 into a new image
        ImageData combinedImage = new ImageData(Math.max(image1.getWidth(), image2.getWidth()), Math.max(image1.getHeight(), image2.getHeight()));

        for (int i = 0; i < combinedImage.getHeight(); i++) {
            for (int j = 0; j < combinedImage.getWidth(); j++) {
                Pixel p1 = image1.getPixel(j, i);
                Pixel p2 = image2.getPixel(j, i);

                if (p1 != null && p2 != null) {
                    Pixel result = new Pixel(
                            (byte) (p1.R & p2.R),
                            (byte) (p1.G & p2.G),
                            (byte) (p1.B & p2.B)
                    );

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
}