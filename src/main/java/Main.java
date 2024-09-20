import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1000);

        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                var path = generateRoute("RLRFR", 100);
                var countR = path.length() - path.replace("R", "").length();

                synchronized (sizeToFreq) {
                    sizeToFreq.put(countR, sizeToFreq.getOrDefault(countR, 0) + 1);
                }
                latch.countDown();
            }).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int maxCount = Collections.max(sizeToFreq.values());
        int mostFrequentKey = sizeToFreq.entrySet().stream()
                .filter(entry -> entry.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);

        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)%n", mostFrequentKey, maxCount);
        System.out.println("Другие размеры:");
        sizeToFreq.forEach((key, value) -> System.out.printf("- %d (%d раз)%n", key, value));
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
