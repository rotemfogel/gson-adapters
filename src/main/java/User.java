import com.google.common.base.CaseFormat;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
@Slf4j
public class User {

    @SerializedName(value = "user_id")
    private Integer userId;

    @SerializedName(value = "user_name")
    private String userName;

    @SerializedName(value = "account_credit")
    private Double accountCredit;

    @SerializedName(value = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @SerializedName(value = "is_employee")
    private Boolean isEmployee;

    @SerializedName(value = "data")
    private Map<String, String> data;

    public static void main(String[] args) {
        final int userId = 1234567890;
        User user = new User();
        user.setUserName("rotemfo");
        final LocalDateTime dob = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        user.setDateOfBirth(dob);
        user.setData(new HashMap<>() {{
            put("user_id", Integer.toString(userId));
        }});
        assert (user.getUserId() == 0);
        String userJson = GsonUtils.toJson(user);
        log.info(userJson);
        User generatedUser = GsonUtils.fromJson(userJson, User.class);
        log.info("{}", generatedUser);
        assert (generatedUser.getUserId() == userId);
        // ---------------------------------------------------------------
        String manualJson = "{\"a\":1, \"user_name\":\"rotemfo\",\"date_of_birth\":\"2021-03-14T10:27:49\",\"data\":\"user_id\\u003d1234567890,account_credit\\u003d0.3b,is_employee\\u003dtrue,user_name\\u003dyoni\"}";
        generatedUser = GsonUtils.fromJson(manualJson, User.class);
        log.info("{}", generatedUser);
        assert (generatedUser.getUserId() == userId);
        generatedUser.setUserName(null);
        String generatedJson = GsonUtils.toJson(generatedUser);
        User yoni = GsonUtils.fromJson(generatedJson, User.class);
        log.info("{}", yoni);
        assert (yoni.getUserName().equals("yoni"));
    }

    public void setFromData(List<String> keys) {
        keys.forEach(key -> {
            try {
                String methodName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, key);
                Method getMethod = this.getClass().getMethod(String.format("get%s", methodName));
                if (data.containsKey(key)) {
                    Object result = getMethod.invoke(this);
                    if (result == null) {
                        Class<?> clazz = getMethod.getReturnType();
                        Method setMethod = this.getClass().getMethod(String.format("set%s", methodName), clazz);
                        String dataValue = data.get(key);
                        Object value;
                        if (Integer.class.equals(clazz)) {
                            value = Integer.parseInt(dataValue);
                        } else if (Double.class.equals(clazz)) {
                            value = Double.parseDouble(dataValue);
                        } else if (Long.class.equals(clazz)) {
                            value = Long.parseLong(dataValue);
                        } else if (Boolean.class.equals(clazz)) {
                            value = Boolean.valueOf(dataValue);
                        } else {
                            value = dataValue;
                        }
                        setMethod.invoke(this, value);
                    }
                }
            } catch (Exception e) {
                log.error("and error occurred parsing {}", this);
            }
        });
    }
}
