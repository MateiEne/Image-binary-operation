package packWork;

import packWork.BMPHandler.BMPImageLoader;
import packWork.BMPHandler.BMPImageSaver;
import packWork.arguments.ArgumentOperation;
import packWork.arguments.Arguments;
import packWork.exceptions.InvalidArgumentException;
import packWork.image.ImageData;
import packWork.image.ImageLoader;
import packWork.image.ImageSaver;
import packWork.image.MemoryImageLoader;
import packWork.operations.ANDOperation;
import packWork.operations.OROperation;
import packWork.operations.Operation;
import packWork.operations.XOROperation;
import packWork.prodConReadFile.DataBuffer;
import packWork.prodConReadFile.FileConsumer;
import packWork.prodConReadFile.FileReaderProducer;

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
            ImageData combinedImage = operation.execute(imageDataList.toArray(ImageData[]::new)); // convert list to varargs

            ImageSaver imageSaver = new BMPImageSaver();
            imageSaver.saveImage(arguments.outputImagePath, combinedImage);

        } catch (InvalidArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void combineImagesProducerConsumer(Arguments arguments) {

        MemoryImageLoader imageLoader = new BMPImageLoader();

        List<ImageData> imageDataList = new ArrayList<>();

        List<FileConsumer> fileConsumers = new ArrayList<>();

        for (String s : arguments.inputImagesPath) {
            DataBuffer dataBuffer = new DataBuffer();

            FileReaderProducer producer = new FileReaderProducer(s, 10000, dataBuffer);
            FileConsumer consumer = new FileConsumer(dataBuffer);

            fileConsumers.add(consumer);

            producer.start();
            consumer.start();
        }

        // wait for all the threads that are reading the files to finish
        while (true) {
            Thread t = firstThreadNotDead(fileConsumers);
            if (t == null) {
                // all have finished
                break;
            }

            // wait for this thread to finish
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // all threads have finished => retrieve the files
        fileConsumers.forEach(fc -> {
            try {
                imageDataList.add(imageLoader.loadImage(fc.getData()));
            } catch (InvalidArgumentException e) {
                System.out.println(e.getMessage());
            }
        });

        try {
            Operation operation = getOperation(arguments.operation);
            ImageData combinedImage = operation.execute(imageDataList.toArray(ImageData[]::new)); // convert list to varargs

            ImageSaver imageSaver = new BMPImageSaver();
            imageSaver.saveImage(arguments.outputImagePath, combinedImage);

        } catch (InvalidArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private Thread firstThreadNotDead(List<FileConsumer> threads) {
        for (Thread t : threads) {
            if (t.isAlive()) {
                return t;
            }
        }

        return null;
    }


    private Operation getOperation(ArgumentOperation operation) {
        return switch (operation) {
            case AND -> new ANDOperation();
            case OR -> new OROperation();
            case XOR -> new XOROperation();
        };
    }
}
