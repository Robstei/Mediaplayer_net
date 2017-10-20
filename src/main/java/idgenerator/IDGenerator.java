package idgenerator;

import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {

    private static final AtomicInteger id = new AtomicInteger();

    public static long getNextID() {
        if (id.get() >= -1 && id.get() < 9999) {
            return id.getAndIncrement();
        } else {
            throw new IDOverFlowExeption();
        }
    }
}

