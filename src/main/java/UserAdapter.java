import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

public class UserAdapter extends TypeAdapter<User> {
    @Override
    public void write(JsonWriter jsonWriter, User user) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("user_id");
        jsonWriter.value(user.getUserId());
        jsonWriter.name("user_name");
        jsonWriter.value(user.getUserName());
        jsonWriter.name("date_of_birth");
        jsonWriter.value(GsonUtils.DATE_FORMATTER.format(user.getDateOfBirth()));
        jsonWriter.name("data");
        jsonWriter.value(Joiner.on(",").withKeyValueSeparator("=").join(user.getData()));
        jsonWriter.endObject();
    }

    @Override
    public User read(JsonReader jsonReader) throws IOException {
        final User user = new User();
        jsonReader.beginObject();
        String fieldname = null;

        while (jsonReader.hasNext()) {
            JsonToken token = jsonReader.peek();

            if (token.equals(JsonToken.NAME)) {
                //get the current token
                fieldname = jsonReader.nextName();
            } else if ("user_id".equals(fieldname)) {
                //move to next token
                jsonReader.peek();
                user.setUserId(jsonReader.nextInt());
            } else if ("user_name".equals(fieldname)) {
                //move to next token
                jsonReader.peek();
                user.setUserName(jsonReader.nextString());
            } else if ("date_of_birth".equals(fieldname)) {
                jsonReader.peek();
                user.setDateOfBirth(LocalDateTime.from(GsonUtils.DATE_FORMATTER.parse(jsonReader.nextString())));
            } else if ("data".equals(fieldname)) {
                //move to next token
                jsonReader.peek();
                Map<String, String> data = Splitter.on(",").withKeyValueSeparator("=").split(jsonReader.nextString());
                user.setData(data);
                user.setFromData(Arrays.asList("user_id", "user_name", "is_employee", "account_credit"));
            } else {
                jsonReader.peek();
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        return user;
    }
}
