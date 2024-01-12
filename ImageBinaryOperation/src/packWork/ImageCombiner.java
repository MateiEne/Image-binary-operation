package packWork;

import packWork.BMPHandler.BMPImageLoader;
import packWork.BMPHandler.BMPImageSaver;
import packWork.arguments.ArgumentOperation;
import packWork.arguments.Arguments;
import packWork.exceptions.InvalidArgumentException;
import packWork.image.ImageData;
import packWork.image.ImageLoader;
import packWork.image.ImageSaver;
import packWork.operations.ANDOperation;
import packWork.operations.OROperation;
import packWork.operations.Operation;
import packWork.operations.XOROperation;

import java.util.ArrayList;
import java.util.List;

public class ImageCombiner {

    public void combineImages(Arguments arguments) {
        try {
            ImageLoader imageLoader = new BMPImageLoader();

            List<ImageData> imageDataList = new ArrayList<>();
            for (String s : arguments.inputImagesPath) {
                ImageData imageData = imageLoader.loadImage(s);
                imageDataList.add(imageData);
            }

            Operation operation = getOperation(arguments.operation);
            ImageData combinedImage = operation.execute(imageDataList.toArray(ImageData[]::new));

            ImageSaver imageSaver = new BMPImageSaver();
            imageSaver.saveImage(arguments.outputImagePath, combinedImage);

        } catch (InvalidArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private Operation getOperation(ArgumentOperation operation) {
        return switch (operation) {
            case AND -> new ANDOperation();
            case OR -> new OROperation();
            case XOR -> new XOROperation();
        };
    }
}
