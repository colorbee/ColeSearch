package clud.colorbee.cole.test;

/**
 * @author heh
 * @date 2021/1/26
 */
public class MyThread extends Thread {
    @Override
    public void run(){
        super.run();
        System.out.println("执行子线程...");
    }
}
 class Test {
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();
        System.out.println("主线程...");
    }
}