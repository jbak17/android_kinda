package io.bsconsulting.cosc370.persistence;

import androidx.room.TypeConverter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

public class DateListConverter {

        @TypeConverter
        public String fromDateList(List<LocalDateTime> timeList) {
            if (timeList == null) {
                return (null);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<List<LocalDateTime>>() {}.getType();
            return gson.toJson(timeList, type);
        }

        @TypeConverter
        public List<LocalDateTime> toDateList(String timeString) {
            if (timeString == null) {
                return (null);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<List<LocalDateTime>>() {}.getType();
            return gson.fromJson(timeString, type);
        }
}
