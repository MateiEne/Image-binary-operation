package packWork.operations;

import packWork.image.Pixel;

public class OROperation extends GenericOperation {
    @Override
    protected Pixel combinePixels(Pixel p1, Pixel p2) {
        return new Pixel(
                (byte) (p1.R | p2.R),
                (byte) (p1.G | p2.G),
                (byte) (p1.B | p2.B)
        );
    }
}
