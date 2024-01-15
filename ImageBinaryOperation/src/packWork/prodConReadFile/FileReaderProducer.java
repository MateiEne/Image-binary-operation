package packWork.prodConReadFile;

import packWork.exceptions.UnexpectedException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReaderProducer extends Thread {

    private final String filename;
    private final int sizeOfChunk;

    private final DataBuffer dataBuffer;

    public FileReaderProducer(String filename, int sizeOfChunk, DataBuffer dataBuffer) {
        this.filename = filename;
        this.sizeOfChunk = sizeOfChunk;
        this.dataBuffer = dataBuffer;
    }

    @Override
    public void run() {
        try (FileInputStream fileInputStream = new FileInputStream(filename)) {
            byte[] buffer = new byte[sizeOfChunk];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byte[] data = new byte[bytesRead];
                System.arraycopy(buffer, 0, data, 0, bytesRead);

                dataBuffer.putData(data);
                System.out.println("Producer: Produced: " + bytesRead + " bytes from " + filename);

                Thread.sleep(100); // Simulate some processing time
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nu s-a gasit fisierul: " + filename);
            signalProducerClosed();
            return;
        } catch (IOException e) {
            System.out.println("Fisierul " + filename + " este corupt");
            signalProducerClosed();
            return;
        } catch (InterruptedException e) {
            System.out.println("Eroare la citirea fisierului " + filename);
            signalProducerClosed();
            return;
        } catch (UnexpectedException e) {
            System.out.println(e.getMessage());
            signalProducerClosed();
            return;
        }

        // Signal the end of file
        try {
            dataBuffer.finish();
        } catch (UnexpectedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void signalProducerClosed() {
        try {
            dataBuffer.signalProducerClosed();
        } catch (UnexpectedException e) {
            System.out.println(e.getMessage());
        }
    }
}
