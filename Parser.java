import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * This class is thread safe.
 */
public class Parser {
    private File file;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    public void setFile(File f) {
        writeLock.lock();
        try {
            file = f;
        } finally {
            writeLock.unlock();
        }
    }

    public File getFile() {
        readLock.lock();
        try {
            return file;
        } finally {
            readLock.unlock();
        }
    }

    public String getContent() throws IOException {
        readLock.lock();
        try {
            FileInputStream i = new FileInputStream(file);
            String output = "";
            int data;
            while ((data = i.read()) > 0) {
                output += (char) data;
            }
            return output;
        } finally {
            readLock.unlock();
        }

    }

    public String getContentWithoutUnicode() throws IOException {
        readLock.lock();
        try {
            FileInputStream i = new FileInputStream(file);
            String output = "";
            int data;
            while ((data = i.read()) > 0) {
                if (data < 0x80) {
                    output += (char) data;
                }
            }
            return output;

        } finally {
            readLock.unlock();
        }

    }

    public void saveContent(String content) throws IOException {
        writeLock.lock();
        try {
            FileOutputStream o = new FileOutputStream(file);
            for (int i = 0; i < content.length(); i += 1) {
                o.write(content.charAt(i));
            }
        } finally {
            writeLock.unlock();
        }

    }
}
