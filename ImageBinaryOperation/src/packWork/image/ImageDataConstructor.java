package packWork.image;

public class ImageDataConstructor {
    // the coordinates of the next pixel to be added in the imageData
    int row, col;
    // the image to be constructed
    private ImageData imageData;
    // flag to know if the image is available
    private boolean imageConstructed;

    // the thread that constructs the image
    private Thread constructingThread;

    // flag to know if the constructing process is in progress
    private boolean constructionInProgress;

    public ImageDataConstructor() {
        imageConstructed = false;
        constructionInProgress = false;
        constructingThread = null;
    }

    public synchronized void initImage(int width, int height) {
        // if there is an image in the process to be constructed, wait until that image has finished
        while (constructionInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // begin the process of constructing the image
        imageConstructed = false;
        constructionInProgress = true;
        constructingThread = Thread.currentThread();
        imageData = new ImageData(width, height);
        row = height - 1;
        col = 0;
    }

    public synchronized void addData(byte[] pixels) {
        // if the process to construct an image hasn't begun => return
        // if a different thread than the one that begun the construction wants to add data => return
        // (only the thread that started the process of construction can add data)
        if (!constructionInProgress || Thread.currentThread() != constructingThread) {
            return;
        }


        for (int i = 0; i < pixels.length; i += 3) {
            byte B = pixels[i];
            byte G = pixels[i + 1];
            byte R = pixels[i + 2];

            imageData.setPixel(row, col, new Pixel(R, G, B));
            col++;
            if (col == imageData.width) {
                // a new row completed => advance to the next row
                col = 0;
                row--;
                if (row == -1) {
                    // all the pixels from the image have been added => signal that the image has been constucted
                    imageConstructed = true;
                    constructingThread = null;
                    constructionInProgress = false;

                    // notify all the thread that the image is ready or that a new image can be constructed
                    notifyAll();
                    return;
                }
            }
        }
    }

    public synchronized ImageData getImageData() {
        while (!imageConstructed) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // optional consume the image
        imageConstructed = false;
        notifyAll();

        return imageData;
    }
}
