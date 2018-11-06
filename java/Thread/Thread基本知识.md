
# 多线程知识:

## 线程基本概念

    进程 : 每个进程都有独立的代码和数据空间（进程上下文），进程间的切换会有较大的开销，一个进程包含1--n个线程。（进程是资源分配的最小单位）
    线程 : 线程：同一类线程共享代码和数据空间，每个线程有独立的运行栈和程序计数器(PC)，线程切换开销小。（线程是cpu调度的最小单位）


    线程的5个阶段 :

            1 新建状态
                使用 new 关键字和 Thread 类或其子类建立一个线程对象后，该线程对象就处于新建状态。它保持这个状态直到程序 start() 这个线程。
            2 就绪
                当线程对象调用了start()方法之后，该线程就进入就绪状态。就绪状态的线程处于就绪队列中，要等待JVM里线程调度器的调度
            3 运行
                如果就绪状态的线程获取 CPU 资源，就可以执行 run()，此时线程便处于运行状态。处于运行状态的线程最为复杂，它可以变为阻塞状态、就绪状态和死亡状态。
            4 阻塞
                如果一个线程执行了sleep（睡眠）、suspend（挂起）等方法，失去所占用资源之后，该线程就从运行状态进入阻塞状态。在睡眠时间已到或获得设备资源后可以重新进入就绪状态。可以分为三种：

                                等待阻塞：运行状态中的线程执行 wait() 方法，使线程进入到等待阻塞状态。
                                同步阻塞：线程在获取 synchronized 同步锁失败(因为同步锁被其他线程占用)。
                                其他阻塞：通过调用线程的 sleep() 或 join() 发出了 I/O 请求时，线程就会进入到阻塞状态。当sleep() 状态超时，join() 等待线程终止或超时，或者 I/O 处理完毕，线程重新转入就绪状态。
            5 销毁
                一个运行状态的线程完成任务或者其他终止条件发生时，该线程就切换到终止状态。



## 多线程创建方法

### 1 继承Thread类.重写run方法

``` java
public class ThreadTest extends Thread{
    
    @Override
    public void run() {

        for(int i = 0; i< 100; i++){
            System.out.println(getName() +" : "+getId() +"----"+ i);

        }
    }
}
```
* 测试类
``` java
public static void main(String[] args) {

        ThreadTest thread = new ThreadTest();

        // 修改线程名字
        thread.setName("线程一");

        // 启动线程

        thread.start();


        ThreadTest thread2 = new ThreadTest();

        // 修改线程名字
        thread2.setName("线程二");

        // 启动线程

        thread2.start();
 }
 ```
 
## 2 实现Runnable接口,多个Thread类共享一个Runnable对象

``` jav
public class RunnableTest implements  Runnable {

    int num;

    public RunnableTest(int num) {
        this.num = num;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {

            //链式编程
            System.out.println(Thread.currentThread().getName() + ":" + i + num);
        }
    }

}
```
* 测试
``` java
        // 共享一个Runnable对象
        Runnable run = new  RunnableTest(100);
        Thread thread3 = new Thread(run);
        Thread thread4 = new Thread(run);

        thread3.setName("线程三");
        thread4.setName("线程四");

        thread4.start();
        thread3.start();
```

## synchronized 的使用

``` java
/**
 * 同步方法:使用关键字synchronized修饰的方法，一旦被一个线程访问，则整个方法全部锁住，其他线程则无法访问
 * <p>
 * synchronized
 * 注意：
 *          非静态同步方法的锁对象是this
 *          静态的同步方法的锁对象是当前类的字节码对象
 * 同步代码块：
 * synchronized(锁对象){
 * <p>
 * }
 * <p>
 * 注意：锁对象需要被所有的线程所共享
 * 使用同步代码块解决线程安全问题
 *
 * @Author ：feixue
 * @Data : 18:12 2018/11/6
 */

public class SynchronizedTest implements Runnable {

    static int tickets = 100;// 火车票数量
    Object obj = new Object();

    @Override
    public void run() {
        // 出售火车票
        while (true) {
            method();
        }
    }

    // 同步方法的锁对象默认为 this
    private synchronized void method() {
        if (tickets > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":" + tickets--);
        }
    }

}
```
* 测试类
``` java
  SynchronizedTest tt = new SynchronizedTest();

        Thread t = new Thread(tt);
        t.setName("窗口1");
        Thread t2 = new Thread(tt);
        t2.setName("窗口2");
        Thread t3 = new Thread(tt);
        t3.setName("窗口3");

        //启动线程对象
        t.start();
        t2.start();
        t3.start();
```


