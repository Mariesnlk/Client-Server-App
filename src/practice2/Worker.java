package practice2;


public class Worker extends Thread {

    private int id;
    private Data data;

    public Worker(int id, Data d) {
        // super("Worker " + id);
        this.id = id;
        data = d;
        this.start();
    }

    @Override
    public void run() {
        synchronized (data) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (data.getState() != id)
                        data.wait();

                    if (id == 1) {
                        data.Tic();
                    } else if (id == 2) {
                        data.Tak();
                    } else {
                        data.Toy();
                    }

                    data.notifyAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
