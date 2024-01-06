package packWork;

import java.io.IOException;

public interface ImageLoader {
    ImageData loadImage(String filename) throws IOException;
}
