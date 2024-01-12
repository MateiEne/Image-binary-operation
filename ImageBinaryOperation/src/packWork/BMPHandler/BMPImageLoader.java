package packWork.BMPHandler;

import packWork.image.ImageData;
import packWork.image.ImageLoader;
import packWork.image.Pixel;
import packWork.exceptions.InvalidArgumentException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BMPImageLoader implements ImageLoader {

    @Override
    public ImageData loadImage(String filename) throws InvalidArgumentException {
        FileInputStream fileInputStream;

        try {
            fileInputStream = new FileInputStream(filename);
        } catch (FileNotFoundException ex) {
            throw new InvalidArgumentException("Nu s-a putut citit fisierul: " + filename);
        }

        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            // Read BMP header
            byte[] header = new byte[54]; // BMP header is 54
            bufferedInputStream.read(header, 0, 54);// bytes

            // Verify if it's a BMP file
            if (header[0] != 'B' || header[1] != 'M') {
                throw new InvalidArgumentException("Fisierul " + filename + " nu este o imagine BMP");
            }

            // Extract image data
            int width = (header[21] & 0xff) << 24 | (header[20] & 0xff) << 16 | (header[19] & 0xff) << 8 | (header[18] & 0xff);
            int height = (header[25] & 0xff) << 24 | (header[24] & 0xff) << 16 | (header[23] & 0xff) << 8 | (header[22] & 0xff);

            int rowLength = width * 3; // Each pixel has 3 bytes (24 bits)
            // Adjust row length to make it multiple of 4 bytes (BMP padding)
            int padding = 0;
            while ((rowLength + padding) % 4 != 0) {
                padding++;
            }

            int imageDataSize = (rowLength + padding) * height;

            byte[] imageData = new byte[imageDataSize];
            bufferedInputStream.read(imageData, 0, imageDataSize);

            // convert imageData to Pixel matrix
            Pixel[][] pixels = new Pixel[height][width];
            int imageDataIndex = 0;
            for (int row = height - 1; row >= 0; row--) {
                for (int col = 0; col < width; col++) {
                    byte B = imageData[imageDataIndex];
                    byte G = imageData[imageDataIndex + 1];
                    byte R = imageData[imageDataIndex + 2];
                    pixels[row][col] = new Pixel(R, G, B);
                    imageDataIndex += 3;
                }
                imageDataIndex += padding;
            }

            // Close streams
            bufferedInputStream.close();
            return new ImageData(width, height, pixels);
        } catch (IOException ex) {
            throw new InvalidArgumentException("Fisierul " + filename + " este corupt");
        }

    }
}
