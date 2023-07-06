import java.util.concurrent.Semaphore;

public class Main {
    private static final boolean[] PARKING_PLACES = new boolean[4];
    final static Semaphore parking = new Semaphore(4);

       public static void main(String[] args) throws InterruptedException {
           for(int i = 1; i < 7; i++){
              new Thread (new Car(i)).start();
              Thread.sleep(500);
           }
        }

        public static class Car implements Runnable {
            private int carNumber;

            public Car(int carNumber) {
                this.carNumber = carNumber;
            }

            @Override
            public void run() {
                System.out.printf("Car #%d coming to parking place.\n", carNumber);
                try {
                    int parkingTime = 3 + (int) (Math.random()*4);
                    parking.acquire(); // занимаем парковку
                    int parkNumber = 0;
                    synchronized (PARKING_PLACES) {
                        for (int i = 0; i < PARKING_PLACES.length; i++) {
                            if (!PARKING_PLACES[i]){
                                PARKING_PLACES[i] = true;
                                parkNumber = i;
                                System.out.printf("Car #%d have parking on place %d.\n", carNumber, i);
                                break;
                            }
                        }
                    }
                    Thread.sleep(parkingTime);  // стоим
                    synchronized (PARKING_PLACES){
                        PARKING_PLACES[parkNumber] = false;
                    }
                    parking.release();
                    System.out.printf("Car #%d have left the parking.\n", carNumber);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
