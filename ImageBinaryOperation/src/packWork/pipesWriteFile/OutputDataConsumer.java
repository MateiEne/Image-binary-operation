package packWork.pipesWriteFile;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class OutputDataConsumer extends Thread {

    private static final int MAX_BYTES_READ = 1024 * 1024 * 300; // 300Mb

    private final DataInputStream dataInputStream;
    private final String filename;

    public OutputDataConsumer(DataInputStream dataInputStream, String filename) {
        this.dataInputStream = dataInputStream;
        this.filename = filename;
    }

    @Override
    public void run() {
        FileOutputStream fileOutputStream;

        try {
            fileOutputStream = new FileOutputStream(filename);
        } catch (FileNotFoundException | SecurityException e) {
            System.out.println("Nu s-a putut crea fisierul: " + filename);
            return;
        }

        byte[] data = new byte[MAX_BYTES_READ];
        while (true) {
            try {
                int dataRead = dataInputStream.read(data);
                if (dataRead == -1) {
                    // no more data
                    break;
                }

                System.out.println("OutputDataConsumer a primit " + dataRead + " bytes");
                fileOutputStream.write(data, 0, dataRead);
                fileOutputStream.flush();


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        try {
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
