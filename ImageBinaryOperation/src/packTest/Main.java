package packTest;

import packWork.ImageCombiner;
import packWork.arguments.ArgumentOperation;
import packWork.arguments.Arguments;
import packWork.arguments.ArgumentsExtractor;
import packWork.BMPHandler.BMPImageLoader;
import packWork.BMPHandler.BMPImageSaver;
import packWork.image.ImageData;
import packWork.image.ImageLoader;
import packWork.image.ImageSaver;
import packWork.operations.ANDOperation;
import packWork.operations.OROperation;
import packWork.operations.Operation;
import packWork.operations.XOROperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws Exception {

        // images/test.bmp images/sch.bmp images/snowboard2.bmp images/ski2.bmp -op xor -o images/result.bmp
        Arguments arguments;
        if (args.length == 0) {
            arguments = readArgumentsFromKeyboard();
        } else {
            try {
                arguments = ArgumentsExtractor.extractArguments(args);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        }

        System.out.println(arguments);
        ImageCombiner imageCombiner = new ImageCombiner();
        imageCombiner.combineImages(arguments);


        /*
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
         */
    }

    static Arguments readArgumentsFromKeyboard() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Introduceti numarul de imagini ce vor fi combinate: ");
        int numOfFiles = sc.nextInt();

        List<String> pathFiles = new ArrayList<>(numOfFiles);
        for (int i = 0; i < numOfFiles; i++) {
            System.out.println("Imaginea numarul " + i + ": ");
            pathFiles.add(sc.next());
        }

        ArgumentOperation argumentOperation;
        for (;;) {
            System.out.println("Introduceti operatia (AND, OR, XOR):");
            String operation = sc.next();
            argumentOperation = ArgumentOperation.valueOfLabel(operation);

            if (operation != null) {
                break;
            }

            System.out.println("Operatia nu este valida");
        }

        System.out.println("Introduceti numele imaginii rezultate: ");
        String outputFile = sc.next();

        return new Arguments(pathFiles, outputFile, argumentOperation);
    }
}