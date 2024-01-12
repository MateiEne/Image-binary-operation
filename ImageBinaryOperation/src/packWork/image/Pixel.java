package packWork.image;

public class Pixel {
    // instance initializer block
    {
        // initialize the pixel with white color value
        R = (byte) 0xFF;
        G = (byte) 0xFF;
        B = (byte) 0xFF;
    }

    public byte R;
    public byte G;
    public byte B;

    public Pixel() {}

    public Pixel(byte R, byte G, byte B) {
        this.R = R;
        this.G = G;
        this.B = B;
    }
}
