package fr.wildcodeschool.gson.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Iterator;

public class Gson {
    private static final String TAG = Gson.class.getName();
    /**
     * Entry point of the Json parsing
     * @param in InputStream: JSON file to parse
     */
    public <T> T fromJson(InputStream in, Class<T> c) {
        T object = null;

        try {
            BufferedReader streamReader =
                    new BufferedReader(new InputStreamReader(in, "UTF-8"));

            StringBuilder builder = new StringBuilder();
            //noinspection StatementWithEmptyBody
            for (String s; (s = streamReader.readLine()) != null; builder.append(s)) ;
            String stringify = builder.toString();

            // Start populate
            object = c.newInstance();
            if (!readJson(stringify, object)) {
                Log.e(TAG, "Gson parser encountered problem!!!");
            }

        } catch (IOException|JSONException|InstantiationException|IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * Starting point of the parsing.
     * @param pJsonStringify String: Stringify JSON.
     * @param pClass Object: output class to populate.
     * @return boolean: true/false to inform if the parser encountered problem.
     * @throws JSONException: Thrown to indicate a problem with the JSON API.
     */
    private boolean readJson(String pJsonStringify, Object pClass) throws JSONException {
        return pJsonStringify.startsWith("{") ?
                parseObject(new JSONObject(pJsonStringify), pClass) :
                parseArray( new JSONArray(pJsonStringify),  pClass) ;
    }

    /**
     * Parse the content of JSONObject into output class.
     * @param pJsonObject JSONObject: JSON node to inflate.
     * @param pClass Object: output class to populate.
     * @return boolean: true/false to inform if the parser encountered problem.
     * @throws JSONException: Thrown to indicate a problem with the JSON API.
     */
    private boolean parseObject(JSONObject pJsonObject, Object pClass) throws JSONException {
        boolean retValue = true;

        Iterator<String> it = pJsonObject.keys();
        while (it.hasNext()) try {
            String key = it.next();
            Field field = pClass.getClass().getDeclaredField(key);
            field.setAccessible(true);

            Object obj = pJsonObject.get(key);
            if (obj instanceof JSONArray) {
                Object instance = Array.newInstance( field.getType().getComponentType(), ((JSONArray) obj).length() );
                retValue &= parseArray((JSONArray) obj, instance);
                field.set(pClass, instance);
                continue;
            }
            if (obj instanceof JSONObject) {
                Object instance = field.getType().newInstance();
                retValue &= parseObject((JSONObject) obj, instance);
                field.set(pClass, instance);
                continue;
            }
            field.set(pClass, pJsonObject.get(key));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            retValue = false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            retValue = false;
        } catch (InstantiationException e) {
            e.printStackTrace();
            retValue = false;
        }
        return retValue;
    }

    /**
     * PaObject obj = pJsonArray.get(i);rse the content of JSONArray into output class.
     * @param pJsonArray JSONArray: JSON node to inflate.
     * @param pClass Object: output class to populate.
     * @return boolean: true/false to inform if the parser encountered problem.
     * @throws JSONException: Thrown to indicate a problem with the JSON API.
     */
    private boolean parseArray(JSONArray pJsonArray, Object pClass) throws JSONException {
        boolean retValue = true;

        int length = Array.getLength(pClass);
        //classe des éléments du tableau
        Class<?> componentType = pClass.getClass().getComponentType();

        for (int i=0; i < length; i++) {
            Object obj = pJsonArray.get( i );
            try {
                if(obj instanceof  JSONObject) {
                    Object newInstance = componentType.newInstance();
                    retValue &= parseObject( (JSONObject) obj, newInstance );
                    Array.set( pClass, i, newInstance );
                    continue;
                }
                if(obj instanceof JSONArray) {
                    Object newInstance = Array.newInstance( componentType, ((JSONArray) obj).length() );
                    retValue &= parseArray( (JSONArray) obj, newInstance );
                    Array.set( pClass, i, newInstance );
                    continue;
                }
                Array.set( pClass, i, obj );
            } catch (IllegalAccessException e2) {
                retValue = false;
            } catch (InstantiationException e3) {
                retValue = false;
            }

        }

        return retValue;
    }
}
