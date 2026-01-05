package OS_PROJECT;

public class ReadersWritersTest {

    // Paylaşılan veri ve kilit
    static class SharedResource {
        private int value = 0;
        private int version = 0;

        private ReadWriteLock lock = new ReadWriteLock();

        // Okuyucu fonksiyonu
        public void read(String readerName) {
            lock.readLock();
            try {
                // Okuma kısmı (kritik bölge)
                System.out.println(("\n")+readerName + " is reading: value = " + value + ", version = " + version);
                try {
                    Thread.sleep(200); // Okuma işlemi biraz zaman alsın
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                lock.readUnLock();
            }
        }

        // Yazıcı fonksiyonu
        public void write(String writerName) {
            lock.writeLock();
            try {
                // Yazma kısmı (kritik bölge)
                value++;
                version++;
                System.out.println(("\n")+">>> " + writerName + " is writing NEW value = " + value + ", version = " + version);
                try {
                    Thread.sleep(300); // Yazma işlemi biraz zaman alsın
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                lock.writeUnLock();
            }
        }
    }

    // Reader thread
    static class Reader implements Runnable {
        private SharedResource resource;
        private String name;

        public Reader(SharedResource resource, String name) {
            this.resource = resource;
            this.name = name;
        }

        @Override
        public void run() {
            // Test amaçlı birkaç kez okusun
            for (int i = 0; i < 5; i++) {
                resource.read(name);
                try {
                    Thread.sleep(150); // Arada biraz beklesin
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    // Writer thread
    static class Writer implements Runnable {
        private SharedResource resource;
        private String name;

        public Writer(SharedResource resource, String name) {
            this.resource = resource;
            this.name = name;
        }

        @Override
        public void run() {
            // Test amaçlı birkaç kez yazsın
            for (int i = 0; i < 5; i++) {
                resource.write(name);
                try {
                    Thread.sleep(500); // Yazmalar arasında biraz beklesin
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    // main: Programın başlangıç noktası
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        // 3 okuyucu, 2 yazıcı oluşturalım
        Thread r1 = new Thread(new Reader(resource, "Reader-1"));
        Thread r2 = new Thread(new Reader(resource, "Reader-2"));
        Thread r3 = new Thread(new Reader(resource, "Reader-3"));

        Thread w1 = new Thread(new Writer(resource, "Writer-1"));
        Thread w2 = new Thread(new Writer(resource, "Writer-2"));

        // Threadleri başlat
        r1.start();
        r2.start();
        r3.start();
        w1.start();
        w2.start();

        // Ana thread hepsinin bitmesini beklesin
        try {
            r1.join();
            r2.join();
            r3.join();
            w1.join();
            w2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n Test finished.");
    }
}