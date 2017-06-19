package actors;

import android.util.Log;

import akka.actor.UntypedActor;

/**
 * Created by sapi9 on 19/06/2017.
 */

public class ViewActor extends UntypedActor{
    @Override
    public void onReceive(Object o) throws Exception {
        Log.i("DAI", "DAI");
    }
}
