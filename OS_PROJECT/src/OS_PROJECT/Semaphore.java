package OS_PROJECT;
//Basit counting semaphore implementasyonu
class Semaphore {

 private int value;

 public Semaphore(int initial) {
     this.value = initial;
 }

 // P() / wait() / down()
 public synchronized void acquire() {
     while (value == 0) {
         try {
             wait(); // Kaynak yoksa bekle
         } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
             // İstersek burada return ile çıkılabilir
         }
     }
     value--; // Kaynağı al
 }

 // V() / signal() / up()
 public synchronized void release() {
     value++;       // Kaynağı bırak
     notify();      // Bekleyen bir thread’i uyandır
 }
}



