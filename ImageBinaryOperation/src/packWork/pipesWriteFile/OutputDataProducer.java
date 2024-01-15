package packWork.pipesWriteFile;

import java.io.DataOutputStream;
import java.io.IOException;

public class OutputDataProducer extends Thread {

    private static final int NUM_OF_CHUNKS = 4;

    private final DataOutputStream dataOutputStream;

    private final byte[] image;
    private int startChunkIndex;

    public OutputDataProducer(DataOutputStream dataOutputStream, byte[] image) {
        this.dataOutputStream = dataOutputStream;
        this.image = image;
        startChunkIndex = 0;
    }

    @Override
    public void run() {
        for (int i = 0; i < NUM_OF_CHUNKS; i++) {
            try {
                int chunkLength = getChunkLength();
                dataOutputStream.write(image, startChunkIndex, chunkLength);
                dataOutputStream.flush();
                startChunkIndex += chunkLength;

                System.out.println("OutputDataProducer a pus: " + chunkLength + " bytes");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // add a delay to simulate some time processing
            try {
                sleep(2000);
            } catch (InterruptedException ignored) {
            }
        }

        // close the stream to signal the consumer that no more data are coming
        try {
            dataOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getChunkLength() {
        int normalChunkLength = image.length / NUM_OF_CHUNKS;

        // If there is a remainder in the above division, the final chunk is bigger
        int remainder = image.length % NUM_OF_CHUNKS;
        if (startChunkIndex + normalChunkLength + remainder == image.length) {
            // this is the last chunk
            return normalChunkLength + remainder;
        }

        return normalChunkLength;
    }
}
