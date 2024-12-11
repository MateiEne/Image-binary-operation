package packTest;

import packWork.ImageCombiner;
import packWork.arguments.ArgumentOperation;
import packWork.arguments.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        // images/test.bmp images/sch.bmp images/snowboard2.bmp images/ski2.bmp -op xor -o images/result.bmp
        Arguments arguments = readArgumentsFromKeyboard();

        //arguments = new Arguments(Arrays.asList("images/test.bmp", "images/sch.bmp"), "images/and6.bmp", ArgumentOperation.AND);
        // incepe procesul de combinare a imaginilor
        ImageCombiner imageCombiner = new ImageCombiner();
        imageCombiner.combineImagesProducerConsumer(arguments);
    }

    static Arguments readArgumentsFromKeyboard() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Introduceti numarul de imagini ce vor fi combinate: ");
        int numOfFiles = sc.nextInt();

        List<String> pathFiles = new ArrayList<>(numOfFiles);
        for (int i = 0; i < numOfFiles; i++) {
            System.out.println("Imaginea calea imaginii numarul " + i + " (trebuie sa fie de forma src/images/nume_imagine.bmp): ");
            pathFiles.add(sc.next());
        }

        ArgumentOperation argumentOperation;
        for (; ; ) {
            System.out.println("Introduceti operatia (AND, OR, XOR):");
            String operation = sc.next();
            argumentOperation = ArgumentOperation.valueOfLabel(operation);

            if (operation != null) {
                break;
            }

            System.out.println("Operatia nu este valida");
        }

        System.out.println("Introduceti calea imaginii rezultate (trebuie sa fie de forma scr/images/nume_imagine.bmp): ");
        String outputFile = sc.next();

        return new Arguments(pathFiles, outputFile, argumentOperation);
    }
}