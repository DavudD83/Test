package space.dotcat.assistant.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HierarchyTypeAdapterFactory<T> implements TypeAdapterFactory {

    private Class<T> mBaseClass;

    private String mTypeFieldName;

    private HashMap<String, Class<?>> mNameToSubtype = new LinkedHashMap<>();

    private HierarchyTypeAdapterFactory(Class<T> baseClass, String typeFieldName) {
        if (baseClass == null || typeFieldName == null) {
            throw new NullPointerException("Base class or type field name can not be null");
        }

        mBaseClass = baseClass;

        mTypeFieldName = typeFieldName;
    }

    public static <T> HierarchyTypeAdapterFactory<T> of(Class<T> baseClass, String typeFieldName) {
        return new HierarchyTypeAdapterFactory<T>(baseClass, typeFieldName);
    }

    public HierarchyTypeAdapterFactory<T> addSubtype(Class<? extends T> subtype, String className) {
        if (subtype == null) {
            throw new NullPointerException("Subtype that you have passed to addSubtype is null");
        }

        if (mNameToSubtype.containsKey(className)) {
            throw new IllegalArgumentException("You are trying to add subtype " + className + " that you have already added." );
        }

        mNameToSubtype.put(className, subtype);

        return this;
    }

    public HierarchyTypeAdapterFactory<T> addSubtype(Class<? extends T> subtype) {
        String className = subtype.getSimpleName();

        return addSubtype(subtype, className);
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType() != mBaseClass) {
            return null;
        }

        final HashMap<String, TypeAdapter<?>> mNameToAdapter = new LinkedHashMap<>();
        final HashMap<Class<?>, TypeAdapter<?>> mClassToAdapter = new LinkedHashMap<>();

        //put adapter for base class in order to be able to parse response even if unknown subtype came to us
        mNameToAdapter.put(mBaseClass.getSimpleName(), gson.getDelegateAdapter(this, TypeToken.get(mBaseClass)));

        for(Map.Entry<String, Class<?>> entry : mNameToSubtype.entrySet()) {
            TypeAdapter<?> typeAdapter = gson.getDelegateAdapter(this, TypeToken.get(entry.getValue()));

            mNameToAdapter.put(entry.getKey(), typeAdapter);
            mClassToAdapter.put(entry.getValue(), typeAdapter);
        }

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                Class<T> tClass = (Class<T>) value.getClass();

                TypeAdapter<T> typeAdapterForType = (TypeAdapter<T>) mClassToAdapter.get(tClass);

                if (typeAdapterForType == null) {
                    throw new JsonParseException("Can not deserialize class " + tClass.getName() + "Did you forget to add this subtype?");
                }

                JsonElement json = typeAdapterForType.toJsonTree(value);

                Streams.write(json, out);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                JsonElement json = Streams.parse(in);

                JsonObject jsonObject = json.getAsJsonObject();

                JsonElement typeFromJson = jsonObject.get(mTypeFieldName);

                if (typeFromJson == null) {
                    throw new JsonParseException("Can not parse " + mBaseClass + " subtype because you forgot to add type field name");
                }

                String className = typeFromJson.getAsString();

                TypeAdapter<T> typeAdapterForType = (TypeAdapter<T>) mNameToAdapter.get(className);

                if (typeAdapterForType == null) {
//                    throw new JsonParseException("cannot deserialize " + mBaseClass + " subtype named "
//                            + className + "; did you forget to register a subtype?");

                    typeAdapterForType = (TypeAdapter<T>) mNameToAdapter.get(mBaseClass.getSimpleName());

                    return typeAdapterForType.fromJsonTree(json);
                }

                return typeAdapterForType.fromJsonTree(json);
            }
        }.nullSafe();
    }
}
