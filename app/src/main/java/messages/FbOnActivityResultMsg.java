package messages;

import android.content.Intent;

/**
 * Created by nicola on 06/07/2017.
 */

public class FbOnActivityResultMsg implements IMessage {

    private int requestCode;
    private int resultCode;
    private Intent data;

    public FbOnActivityResultMsg(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_ON_ACTIVITY_RESULT_MSG;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public Intent getData() {
        return data;
    }
}
