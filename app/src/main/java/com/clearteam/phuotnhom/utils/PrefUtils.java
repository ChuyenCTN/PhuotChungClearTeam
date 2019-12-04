package com.clearteam.phuotnhom.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.clearteam.phuotnhom.ui.setting.model.InfoSOS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.annotations.NotNull;


public class PrefUtils {
    private static final String SHARE_NAME = "APP_TEMP_DT";

    private static final String TAG = "PrefUtils: ";

    private static PrefUtils INSTANCE;

    private static final String CURRENT_USER_KEY = "current_user_key";
    private static final String CURRENT_LIST_PEST_KEY = "current_list_pest_key";
    private static final String CURRENT_USER_KEY_V2 = "current_user_key_v2";
    private static final String SOS_MODEL = "sos_model";
    private static final String CURRENT_LOGIN_MODEL = "current_login_model";

    private static final String CURRENT_MQTT_CONFIG = "CURRENT_MQTT_CONFIG";

    private static final String LANGUAGE = "LANGUAGE";

    private PrefUtils() {

    }

    public static PrefUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PrefUtils();
        }

        return INSTANCE;
    }

    private static SharedPreferences getSharedPreferences(@NotNull Context context) {
        return context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
    }

    public static void storeApiKey(@NotNull Context context, String apiKey) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("API_KEY", apiKey);
        editor.apply();
    }

    public static String getApiKey(@NotNull Context context) {
        return getSharedPreferences(context).getString("API_KEY", null);
    }

//    public static void saveUser(@NotNull Context context, User user) {
//        if (user == null) {
//            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
//            editor.putString(CURRENT_USER_KEY, null);
//            editor.apply();
//            return;
//        }
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            String userString = objectMapper.writeValueAsString(user);
//            byte[] encodeValue = Base64.encode(userString.getBytes(), Base64.DEFAULT);
//            // save User to string.
//            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
//            editor.putString(CURRENT_USER_KEY, new String(encodeValue));
//            editor.apply();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

//    public static String getUser(Context context) {
//        String sUser = getSharedPreferences(context).getString(CURRENT_USER_KEY, null);
//        try {
//            byte[] decodeValue = Base64.decode(sUser, Base64.DEFAULT);
//
//            return new String(decodeValue);
//        } catch (Exception ex) {
//            return "";
//        }
//    }

//    public static User getCurrentUser(Context context) {
//        String sUser = getUser(context);
//        ObjectMapper objectMapper = new ObjectMapper();
//        User user = null;
//        try {
//            user = objectMapper.readValue(sUser, User.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return user;
//    }

//    public static void clearAll(Context context) {
//        try {
//            BaseRemote.releaseInstance();
//            getSharedPreferences(context).edit().clear().apply();
//        } catch (Exception ex) {
//            AppLogger.d(TAG + ex.getMessage());
//        }
//    }

//    public static void saveMqttConfig(@NotNull Context context, MqttModel mqttModel) {
//        if (mqttModel == null) {
//            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
//            editor.putString(CURRENT_MQTT_CONFIG, null);
//            editor.apply();
//            return;
//        }
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            String userString = objectMapper.writeValueAsString(mqttModel);
//            // save User to string.
//            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
//            editor.putString(CURRENT_MQTT_CONFIG, userString);
//            editor.apply();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


    public static void saveInfoSOS(@NotNull Context context, InfoSOS infoSOS) {
        if (infoSOS == null) {
            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(SOS_MODEL, null);
            editor.apply();
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String userString = objectMapper.writeValueAsString(infoSOS);
            // save User to string.
            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(SOS_MODEL, userString);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static InfoSOS getInfoSOS(Context context) {
        String sUser = getSharedPreferences(context).getString(SOS_MODEL, null);
        ObjectMapper objectMapper = new ObjectMapper();
        InfoSOS infoSOS = null;
        try {
            infoSOS = objectMapper.readValue(sUser, InfoSOS.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return infoSOS;
    }


//    public static void saveListPestType(@NotNull Context context, List<PestTypeModel> user) {
//        if (user == null) {
//            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
//            editor.putString(CURRENT_LIST_PEST_KEY, null);
//            editor.apply();
//            return;
//        }
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            String userString = objectMapper.writeValueAsString(user);
//            byte[] encodeValue = Base64.encode(userString.getBytes(), Base64.DEFAULT);
//            // save User to string.
//            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
//            editor.putString(CURRENT_LIST_PEST_KEY, new String(encodeValue));
//            editor.apply();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public static String getListPestType(Context context) {
//        String sUser = getSharedPreferences(context).getString(CURRENT_LIST_PEST_KEY, null);
//        try {
//            byte[] decodeValue = Base64.decode(sUser, Base64.DEFAULT);
//
//            return new String(decodeValue);
//        } catch (Exception ex) {
//            return "";
//        }
//    }
//
//    public static List<PestTypeModel> getCurrentListPestType(Context context) {
//        String sUser = getListPestType(context);
//        Gson gson = new Gson();
//        List<PestTypeModel> listPestType = null;
//        try {
//            Type type = new TypeToken<List<PestTypeModel>>() {
//            }.getType();
//            listPestType = gson.fromJson(sUser, type);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return listPestType;
//    }
}
