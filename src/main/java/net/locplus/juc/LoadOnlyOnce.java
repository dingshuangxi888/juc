package net.locplus.juc;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 2014/5/6.
 */
public class LoadOnlyOnce {

    private ConcurrentHashMap<String, FutureTask<Object>> caches = new ConcurrentHashMap<String, FutureTask<Object>>();

    public Object getClient(String key) throws ExecutionException, InterruptedException {
        if (caches.containsKey(key)) {
            return caches.get(key);
        } else {
            FutureTask<Object> valueTask = new FutureTask<Object>(
                    new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            return "abc";
                        }
                    }
            );
            FutureTask<Object> current = caches.putIfAbsent(key, valueTask);
            if (current == null) {
                valueTask.run();
            } else {
                valueTask = current;
            }
            return valueTask.get();
        }
    }
}
