package messages;

/**
 * Created by nicola on 01/07/2017.
 */

public abstract class FbOperationCompletedMsg<T> implements IMessage {

    protected boolean success;
    protected T data;
    protected String errorMessage;


    public FbOperationCompletedMsg(T data){
        this.success = true;
        this.data = data;
    }

    public FbOperationCompletedMsg(String errorMessage) {
        this.success = false;
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_OPERATION_COMPLETED_MSG;
    }
}