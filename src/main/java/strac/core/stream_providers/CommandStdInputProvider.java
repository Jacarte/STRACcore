package strac.core.stream_providers;

import strac.core.LogProvider;
import strac.core.TraceHelper;

import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CommandStdInputProvider implements TraceHelper.IStreamProvider {

    private static int MAX_TIME = 15; // 15 seconds

    public void setMaxTime(int time){
        MAX_TIME = time;
    }


    @Override
    public InputStream getStream(String filename) {
        try {
            Process p = Runtime.getRuntime().exec(new String[] {"bash", "-c", filename});

            final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    p.destroy();
                }
            }, MAX_TIME, TimeUnit.SECONDS);

            return p.getInputStream();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validate(String filename) {
        return true;
    }
}
