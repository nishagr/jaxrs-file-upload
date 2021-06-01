package com.gupshup.producer_consumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class App {

	public static void main(String[] args) {
		String[] array = new String[] { "Hello", "World" };
		for (String i : array)
			System.out.print(" | " + i);
		System.out.println(" |");
//        BlockingQueue<Integer> blockingQueue = new LinkedBlockingDeque<>(4);
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//
//        Runnable producerTask = () -> {
//            try {
//                int value = 0;
//                while (true) {
//                    blockingQueue.put(value);
//
//                    System.out.println("Produced " + value);
//
//                    value++;
//
//                    Thread.sleep(500);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        };
//
//        Runnable consumerTask = () -> {
//            try {
//                while (true) {
//                    int value = blockingQueue.take();
//
//                    System.out.println("Consume " + value);
//
//                    Thread.sleep(2000);
//                }
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        };
//
//        executor.execute(producerTask);
//        executor.execute(consumerTask);
//
//        executor.shutdown();
	}
}