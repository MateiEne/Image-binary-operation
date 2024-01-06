package packWork;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BMPFileWriter {
    public static void writeImage(String filename, Image image) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();

        // Calculate image size
        int rowLength = width * 3;
        int padding = (4 - (rowLength % 4)) % 4;
        int imageDataSize = (rowLength + padding) * height;

        // BMP file size
        int fileSize = 54 + imageDataSize;

        // Create byte array for the BMP file
        byte[] bmpData = new byte[fileSize];

        // BMP header - See BMP file format specifications for details
        bmpData[0] = 'B';
        bmpData[1] = 'M';

        // File size
        bmpData[2] = (byte) fileSize;
        bmpData[3] = (byte) (fileSize >> 8);
        bmpData[4] = (byte) (fileSize >> 16);
        bmpData[5] = (byte) (fileSize >> 24);

        // Image offset
        int imageDataOffset = 54;
        bmpData[10] = (byte) imageDataOffset;
        bmpData[11] = (byte) (imageDataOffset >> 8);
        bmpData[12] = (byte) (imageDataOffset >> 16);
        bmpData[13] = (byte) (imageDataOffset >> 24);

        // DIB header
        bmpData[14] = 40; // DIB Header size
        bmpData[18] = (byte) width;
        bmpData[19] = (byte) (width >> 8);
        bmpData[20] = (byte) (width >> 16);
        bmpData[21] = (byte) (width >> 24);
        bmpData[22] = (byte) height;
        bmpData[23] = (byte) (height >> 8);
        bmpData[24] = (byte) (height >> 16);
        bmpData[25] = (byte) (height >> 24);
        bmpData[26] = 1; // Number of color planes
        bmpData[28] = 24; // Bits per pixel

        // Write pixel data to the byte array
        Pixel[][] pixelMatrix = image.getPixels();

        int pixelIndex = imageDataOffset;
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                Pixel pixel = pixelMatrix[y][x];
                bmpData[pixelIndex++] = pixel.B;
                bmpData[pixelIndex++] = pixel.G;
                bmpData[pixelIndex++] = pixel.R;
            }

            for (int p = 0; p < padding; p++) {
                bmpData[pixelIndex++] = 0;
            }
        }

        // write byte array to file
        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(bmpData);
        fos.close();
    }
}
