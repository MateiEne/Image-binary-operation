package packWork;

import packWork.BMPHandler.BMPImageLoader;
import packWork.BMPHandler.BMPImageSaver;
import packWork.arguments.ArgumentOperation;
import packWork.arguments.Arguments;
import packWork.exceptions.InvalidArgumentException;
import packWork.image.*;
import packWork.operations.ANDOperation;
import packWork.operations.OROperation;
import packWork.operations.Operation;
import packWork.operations.XOROperation;
import packWork.pipesWriteFile.OutputDataConsumer;
import packWork.pipesWriteFile.OutputDataProducer;
import packWork.prodConReadFile.DataBuffer;
import packWork.prodConReadFile.FileConsumer;
import packWork.prodConReadFile.FileReaderProducer;

import java.io.*;
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
            if (operation == null) {
                System.out.println("eroare la introducerea operatiei");
                return;
            }
            ImageData combinedImage = operation.execute(imageDataList.toArray(new ImageData[0])); // convert list to varargs

            ImageSaver imageSaver = new BMPImageSaver();
            imageSaver.saveImage(arguments.outputImagePath, combinedImage);

        } catch (InvalidArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void combineImagesProducerConsumer(Arguments arguments) {
        long startTime = System.currentTimeMillis();
        List<ImageData> imageDataList = readAllFilesInParallel(arguments.inputImagesPath);
        long stopTime = System.currentTimeMillis();

        System.out.println("TIMP DE EXECUTIE CITIREA FISIERELOR: " + (stopTime - startTime));

        Operation operation = getOperation(arguments.operation);
        if (operation == null) {
            System.out.println("eroare la introducerea operatiei");
            return;
        }

        startTime = System.currentTimeMillis();
        ImageData combinedImage = operation.execute(imageDataList.toArray(new ImageData[0])); // convert list to varargs
        stopTime = System.currentTimeMillis();

        System.out.println("TIMP DE EXECUTIE PROCESARE IMAGINE: " + (stopTime - startTime));

        saveImageAsync(arguments.outputImagePath, combinedImage);
    }

    private List<ImageData> readAllFilesInParallel(List<String> files) {
        List<FileConsumer> fileConsumers = new ArrayList<>();

        // generate file producer and file consumer threads for each file
        for (String filename : files) {
            DataBuffer dataBuffer = new DataBuffer();

            FileReaderProducer producer = new FileReaderProducer(filename, 50000, dataBuffer);
            FileConsumer consumer = new FileConsumer(dataBuffer);

            fileConsumers.add(consumer);

            producer.start();
            consumer.start();
        }

        // wait for all the threads that are reading the files to finish
        waitForThreadsToFinish(fileConsumers);


        // all threads have finished => retrieve the files
        MemoryImageLoader imageLoader = new BMPImageLoader();
        List<ImageData> result = new ArrayList<>();

        fileConsumers.forEach(fc -> {
            try {
                result.add(imageLoader.loadImage(fc.getData()));
            } catch (InvalidArgumentException e) {
                System.out.println(e.getMessage());
            }
        });

        return result;
    }

    private void waitForThreadsToFinish(List<? extends Thread> threads) {
        // wait for all the threads that are reading the files to finish
        while (true) {
            Thread t = firstThreadNotDead(threads);
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
    }

    private Thread firstThreadNotDead(List<? extends Thread> threads) {
        for (Thread t : threads) {
            if (t.isAlive()) {
                return t;
            }
        }

        return null;
    }

    private void saveImageAsync(String filename, ImageData image) {
        PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn;
        try {
            pipeIn = new PipedInputStream(pipeOut);
        } catch (IOException e) {
            System.out.println("system error");

            return;
        }

        DataOutputStream outputStream = new DataOutputStream(pipeOut);
        DataInputStream inputStream = new DataInputStream(pipeIn);

        MemoryImageSaver imageSaver = new BMPImageSaver();
        OutputDataProducer outputDataProducer = new OutputDataProducer(outputStream, imageSaver.saveImage(image));
        OutputDataConsumer outputDataConsumer = new OutputDataConsumer(inputStream, filename);

        outputDataProducer.start();

        long startTime = System.currentTimeMillis();
        outputDataConsumer.start();

        try {
            outputDataConsumer.join();
        } catch (InterruptedException e) {
            System.out.println("system error");
        }

        long stopTime = System.currentTimeMillis();

        System.out.println("TIMP DE EXECUTIE SALVARE IMAGINE: " + (stopTime - startTime));
    }

    private Operation getOperation(ArgumentOperation operation) {
        switch (operation) {
            case AND:
                return new ANDOperation();
            case OR:
                return new OROperation();
            case XOR:
                return new XOROperation();
        }

        return null;
    }
}
