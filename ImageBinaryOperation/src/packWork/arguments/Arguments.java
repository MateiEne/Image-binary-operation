package packWork.arguments;

import java.util.List;

public class Arguments {
    public List<String> inputImagesPath;

    public String outputImagePath;

    public ArgumentOperation operation;

    public Arguments(List<String> inputImagesPath, String outputImagePath, ArgumentOperation operation) {
        this.inputImagesPath = inputImagesPath;
        this.outputImagePath = outputImagePath;
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "OutputPath: " + outputImagePath + "\nOperation: " + operation + "\nInputImages: " + inputImagesPath;
    }
}
