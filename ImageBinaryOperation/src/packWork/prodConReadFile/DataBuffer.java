package packWork.prodConReadFile;

public class DataBuffer {
    private boolean hasFinished = false;
    private byte[] data;
    private boolean available = false;

    private boolean producerClosed = false;

    public synchronized byte[] getData() {
        if (producerClosed) {
            return null;
        }

        while (!available) {
            try {
                if (producerClosed) {
                    return null;
                }
                wait();
                // Asteapta producatorul sa puna o valoare
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        available = false;
        notifyAll();
        return data;
    }

    public synchronized void putData(byte[] data) {
        while (available) {
            try {
                wait();
                // Asteapta consumatorul sa preia valoarea
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.data = data;
        available = true;
        notifyAll();
    }

    public synchronized void finish() {
        while (available) {
            try {
                wait();
                // Asteapta consumatorul sa preia valoarea
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.hasFinished = true;
        this.available = true;
        notifyAll();
    }

    public synchronized void signalProducerClosed() {
        while (available) {
            try {
                wait();
                // Asteapta consumatorul sa preia valoarea
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.producerClosed = true;
        notifyAll();
    }

    public boolean hasFinished() {
        return hasFinished;
    }

    public boolean isProducerClosed() {
        return producerClosed;
    }
}
