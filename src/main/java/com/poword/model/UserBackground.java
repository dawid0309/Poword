package com.poword.model;

import com.poword.helper.ConfigHelper;
import lombok.Data;

@Data
public class UserBackground {

    private String focusArea;
    private int age;
    private String occupation;
    private String education;
    private String stylePreference;

    private static UserBackground instance;

    public UserBackground(String focusArea, int age, String occupation, String education, String stylePreference) {
        this.focusArea = focusArea;
        this.age = age;
        this.occupation = occupation;
        this.education = education;
        this.stylePreference = stylePreference;
    }

    public static UserBackground getInstance() {
        if (instance == null) {
            instance = ConfigHelper.getUserBackground();
        }
        return instance;
    }

    @Override
    public String toString() {
        return "{" +
            "关注的领域='" + focusArea + '\'' +
            ", 年龄=" + age +
            ", 职业='" + occupation + '\'' +
            ", 学历='" + education + '\'' +
            ", 语言风格偏好='" + stylePreference + '\'' +
            '}';
    }
}
