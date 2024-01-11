package packTest;

import packWork.BMPHandler.BMPImageLoader;
import packWork.BMPHandler.BMPImageSaver;
import packWork.ImageData;
import packWork.ImageLoader;
import packWork.ImageSaver;
import packWork.operations.ANDOperation;
import packWork.operations.Operation;
import packWork.operations.OROperation;
import packWork.operations.XOROperation;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws Exception {
        ImageLoader imageLoader = new BMPImageLoader();

        ImageData snowboarderImage = imageLoader.loadImage("images/test.bmp");
        ImageData skierImage = imageLoader.loadImage("images/sch.bmp");
        ImageData snowboarderImage2 = imageLoader.loadImage("images/snowboard2.bmp");
        ImageData skierImage2 = imageLoader.loadImage("images/ski2.bmp");

        ImageSaver imageSaver = new BMPImageSaver();

        Operation operation = new ANDOperation();
        ImageData combinedImage = operation.execute(snowboarderImage, skierImage);
        imageSaver.saveImage("images/and.bmp", combinedImage);

        operation = new OROperation();
        combinedImage = operation.execute(snowboarderImage, skierImage);
        imageSaver.saveImage("images/or.bmp", combinedImage);

        operation = new XOROperation();
        combinedImage = operation.execute(snowboarderImage, skierImage);
        imageSaver.saveImage("images/xor.bmp", combinedImage);

        operation = new ANDOperation();
        combinedImage = operation.execute(snowboarderImage, snowboarderImage2, skierImage);
        imageSaver.saveImage("images/andAndAnd.bmp", combinedImage);
    }
}