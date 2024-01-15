package packWork.prodConReadFile;

import packWork.exceptions.InvalidArgumentException;
import packWork.exceptions.UnexpectedException;

public class DataBuffer {
    private boolean hasFinished = false;
    private byte[] data;
    private boolean available = false;

    private boolean producerClosed = false;

    public synchronized byte[] getData() throws UnexpectedException {
        if (producerClosed || hasFinished) {
            return null;
        }

        while (!available) {
            try {
                // Asteapta producatorul sa puna o valoare
                wait();
                if (producerClosed || hasFinished) {
                    return null;
                }
            } catch (InterruptedException e) {
                throw new UnexpectedException("system failed");
            }
        }

        available = false;
        notifyAll();
        return data;
    }

    public synchronized void putData(byte[] data) throws UnexpectedException {
        if (producerClosed || hasFinished) {
            return;
        }


        while (available) {
            try {
                wait();
                // Asteapta consumatorul sa preia valoarea
            } catch (InterruptedException e) {
                throw new UnexpectedException("system failed");
            }
        }

        this.data = data;
        available = true;
        notifyAll();
    }

    public synchronized void finish() throws UnexpectedException {
        while (available) {
            try {
                wait();
                // Asteapta consumatorul sa preia valoarea
            } catch (InterruptedException e) {
                throw new UnexpectedException("system failed");
            }
        }

        this.hasFinished = true;
        notifyAll();
    }

    public synchronized void signalProducerClosed() throws UnexpectedException {
        while (available) {
            try {
                wait();
                // Asteapta consumatorul sa preia valoarea
            } catch (InterruptedException e) {
                throw new UnexpectedException("system failed");
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
