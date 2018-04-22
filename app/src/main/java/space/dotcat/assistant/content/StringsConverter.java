package space.dotcat.assistant.content;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;

public class StringsConverter {

    @TypeConverter
    public String fromListToString(List<String> stringList) {
        StringBuilder stringBuilder = new StringBuilder();

        if(stringList == null)
            return "";

        for(String s : stringList) {
            stringBuilder.append(s);
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }

    @TypeConverter
    public List<String> fromStringToList(@NonNull String string) {
        String [] array = string.split(" ");

        return Arrays.asList(array);
    }

}
