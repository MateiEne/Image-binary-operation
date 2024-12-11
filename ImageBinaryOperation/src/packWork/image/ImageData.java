package packWork.image;

// clasa ce contine informatiile despre o imagine
public class ImageData {
    protected int width;
    protected int height;
    protected Pixel[][] pixels;

    public ImageData() {
        width = 0;
        height = 0;

        pixels = new Pixel[height][width];
    }

    public ImageData(int width, int height) {
        this.width = width;
        this.height = height;

        pixels = new Pixel[height][width];
    }

    public ImageData(int width, int height, Pixel[][] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Pixel getPixel(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return pixels[y][x];
        }

        return null;
    }

    public void setPixel(int x, int y, Pixel pixel) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            pixels[y][x] = pixel;
        }
    }

    public Pixel[][] getPixels() {
        return pixels;
    }
}
