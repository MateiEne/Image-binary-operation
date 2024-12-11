package packWork.prodConReadFile;

import packWork.exceptions.UnexpectedException;

import java.util.ArrayList;
import java.util.List;

public class FileConsumer extends Thread {
    private final DataBuffer dataBuffer;

    private List<Byte> data;

    public FileConsumer(DataBuffer dataBuffer) {
        this.dataBuffer = dataBuffer;
        data = new ArrayList<>();
    }

    @Override
    public void run() {
        while (true) {
            byte[] chunk;
            try {
                chunk = dataBuffer.getData();
            } catch (UnexpectedException e) {
                System.out.println(e.getMessage());
                data = null;
                break;
            }

            if (dataBuffer.isProducerClosed()) {
                data = null;
                return;
            }

            if (dataBuffer.hasFinished() || chunk == null) {
                break;
            }

            for (byte b : chunk) {
                data.add(b);
            }
            System.out.println("Consumer: Read " + chunk.length + " bytes");
        }
    }

    public List<Byte> getData() {
        return data;
    }
}
