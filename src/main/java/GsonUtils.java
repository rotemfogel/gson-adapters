import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.time.format.DateTimeFormatter;

public class GsonUtils {
    final public static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    final public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(GsonUtils.DATE_FORMAT);

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(User.class, new UserAdapter())
            .setDateFormat(DATE_FORMAT)
            .create();

    public static String toJson(Object o) {
        return gson.toJson(o);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(json, classOfT);
    }
}
