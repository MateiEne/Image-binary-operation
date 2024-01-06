package packTest;

import packWork.BMPFileWriter;
import packWork.BMPImage;
import packWork.Image;

import java.io.IOException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException {
        BMPImage image = new BMPImage("images/test.bmp");

        image.loadImage();

        System.out.println("Image width: " + image.getWidth());
        System.out.println("Image height: " + image.getHeight());

        BMPFileWriter.writeImage("images/test2.bmp", image);
    }
}