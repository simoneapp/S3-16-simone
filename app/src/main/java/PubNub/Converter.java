package PubNub;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Giacomo on 01/07/2017.
 */

public class Converter {

    private JSONObject myObj;

    public Converter(JSONObject obj){
        myObj=obj;
    }

    public String getStringValue(String key) throws JSONException {
        return myObj.getString(key);
    }
}
