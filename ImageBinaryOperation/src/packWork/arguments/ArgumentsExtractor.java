package packWork.arguments;

import packWork.exceptions.InvalidArgumentException;
import packWork.exceptions.MissingArgumentException;

import java.util.Arrays;
import java.util.Vector;

public class ArgumentsExtractor {

    private static final String operationMark;
    private static final String outputMark;

    // static initializer block
    static {
        operationMark = "-op";
        outputMark = "-o";
    }

    public static Arguments extractArguments(String[] args) throws MissingArgumentException, InvalidArgumentException {
        Vector<String> arguments = new Vector<>(Arrays.asList(args));

        if (!arguments.contains(operationMark)) {
            throw new MissingArgumentException("Nu s-a specificat operatia dorita");
        }

        if (!arguments.contains(outputMark)) {
            throw new MissingArgumentException("Nu s-a sepcificat numele fisierului de iesire");
        }

        // Get the operation
        ArgumentOperation operation;

        int indexOfOperatorMark = arguments.indexOf(operationMark);
        if (indexOfOperatorMark + 1 >= arguments.size()) {
            throw new MissingArgumentException("Nu s-a specificat operatia dorita");
        }

        String operator = arguments.get(indexOfOperatorMark + 1);
        operation = ArgumentOperation.valueOfLabel(operator);

        if (operation == null) {
            throw new InvalidArgumentException("Operatia '" + operator + "' nu este valida");
        }

        arguments.remove(indexOfOperatorMark);  // remove the mark
        arguments.remove(indexOfOperatorMark);  // remove the operation

        // get the output file
        int indexOfOutputFileMark = arguments.indexOf(outputMark);
        if (indexOfOutputFileMark + 1 >= arguments.size()) {
            throw new MissingArgumentException("Nu s-a specificat numele fisierului de iesire");
        }

        String outputFile = arguments.get(indexOfOutputFileMark + 1);

        arguments.remove(indexOfOutputFileMark);    // remove the mark
        arguments.remove(indexOfOutputFileMark);    // remove the outputFile

        // the rest of the arguments are the input images
        if (arguments.size() < 2) {
            throw new InvalidArgumentException("Nu s-au specificat suficiente imagini");
        }

        return new Arguments(arguments, outputFile, operation);
    }
}
