package packWork.prodConReadFile;

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
            byte[] chunk = dataBuffer.getData();

            if (dataBuffer.isProducerClosed()) {
                data = null;
                return;
            }

            if (dataBuffer.hasFinished()) {
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
