package OS_PROJECT;

class ReadWriteLock {

    // Ortak kaynağa aynı anda ya "yazar" ya da "okuyucu grubu" girebilsin diye
    private Semaphore S = new Semaphore(1); 

    // readerCount'ı korumak için ek bir semafor (mutex)
    private Semaphore mutex = new Semaphore(1);

    // Şu anda içeride kaç okuyucu var?
    private int readerCount = 0;

    // Okuyucu içeri girmek istediğinde çağrılacak
    public void readLock() {
        // readerCount değiştirileceği için koruyoruz
        mutex.acquire();
        readerCount++;
        // İlk okuyucu içeri girerken yazarı kilitler
        if (readerCount == 1) {
            S.acquire(); // İlk okuyucu yazarı ve diğer grubu kilitler
        }
        mutex.release();
        // Buradan sonra okuyucu güvenle okuyabilir
    }

    // Okuyucu işini bitirdiğinde çağrılacak
    public void readUnLock() {
        mutex.acquire();
        readerCount--;
        // Son okuyucu çıkıyorsa kilidi bırakıp yazarlara yol aç
        if (readerCount == 0) {
            S.release();
        }
        mutex.release();
    }

    // Yazar içeri girmek istediğinde çağrılacak
    public void writeLock() {
        // S'yi alan, hem okuyucuları hem diğer yazarları bloklar
        S.acquire();
    }

    // Yazar işini bitirdiğinde çağrılacak
    public void writeUnLock() {
        S.release();
    }
}
