package app.simone;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import app.simone.multiplayer.model.FacebookUser;

import static junit.framework.Assert.assertTrue;

/**
 * Created by nicola on 27/08/2017.
 */

public class FacebookUserTest {

    private String testUid = "1020304050";
    private String testName = "John Appleseed";
    private String testPicture = "http://facebook.com/picture.jpg";

    @Test
    public void testValidUserFromParameters() {
        //Creation from parameters
        FacebookUser usr = new FacebookUser(testUid, testName);
        assertTrue(testUid.equals(usr.getId()));
        assertTrue(testName.equals(usr.getName()));
    }

    @Test
    public void testNullUserFromParameters() {
        //Object with null parameters
        FacebookUser usr = new FacebookUser(null, null);
        assertTrue(usr.getId() == null);
        assertTrue(usr.getName() == null);
    }

    @Test
    public void testValidUserFromJson() {
        //Object from Json
        FacebookUser usr = new FacebookUser(getExampleValidUser());
        assertTrue(usr.getId().equals(testUid));
        assertTrue(usr.getName().equals(testName));
        assertTrue(usr.getPicture().equals(testPicture));
    }

    @Test
    public void testUserWithNullFieldsFromJson() {

        JsonObject obj = getExampleValidUser();
        obj.remove(FacebookUser.kNAME);

        FacebookUser usr = new FacebookUser(obj);
        assertTrue(usr.getId().equals(testUid));
        assertTrue(usr.getName() == null);
        assertTrue(usr.getPicture().equals(testPicture));
    }

    @Test
    public void testListFromJsonUsers() {
        JsonArray list = this.getListOfValidUsers(10);
        List<FacebookUser> usrList = FacebookUser.listFromJson(list);
        for(FacebookUser user : usrList) {
            assertTrue(user.getId().equals(testUid));
            assertTrue(user.getName().equals(testName));
            assertTrue(user.getPicture().equals(testPicture));
        }
    }

    @Test
    public void testUserToDictionaryConversion() {
        FacebookUser usr = new FacebookUser(getExampleValidUser());
        Map<String,String> dict = usr.toDictionary();
        assertTrue(dict.get(FacebookUser.kID).equals(usr.getId()));
        assertTrue(dict.get(FacebookUser.kNAME).equals(usr.getName()));
        assertTrue(dict.get(FacebookUser.kPICTURE).equals(usr.getPicture()));
    }


    private JsonObject getExampleValidUser() {
        JsonObject testObj = new JsonObject();
        testObj.addProperty(FacebookUser.kID, testUid);
        testObj.addProperty(FacebookUser.kNAME, testName);

        JsonObject innerObject = new JsonObject();
        innerObject.addProperty(FacebookUser.kPICTURE_URL, testPicture);

        JsonObject pictureObj = new JsonObject();
        pictureObj.add(FacebookUser.kDATA, innerObject);
        testObj.add(FacebookUser.kPICTURE, pictureObj);

        return testObj;
    }

    private JsonArray getListOfValidUsers(int quantity) {

        JsonArray objs = new JsonArray();

        for(int i = 0; i < quantity; i++) {
            objs.add(getExampleValidUser());
        }

        return objs;
    }
}
