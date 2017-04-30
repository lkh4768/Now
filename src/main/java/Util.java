import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by WES on 2017-04-30.
 */
public class Util {
    private Util(){}
    public static String Now()
    {
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dayTime.format(new Date(time));
    }
}
