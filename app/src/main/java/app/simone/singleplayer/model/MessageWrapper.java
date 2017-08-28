package app.simone.singleplayer.model;

import android.os.Message;

/**
 * Created by nicola on 28/08/2017.
 */

public class MessageWrapper {

    public static Message withArg1(int what, int arg1) {
        Message m = new Message();
        m.what = what;
        m.arg1 = arg1;
        return m;
    }

    public static Message withArg2(int what, int arg2) {
        Message m = new Message();
        m.what = what;
        m.arg2 = arg2;
        return m;
    }
}
