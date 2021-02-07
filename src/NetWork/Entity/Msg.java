package NetWork.Entity;

public class Msg {
    public static final int MATCHING_SUCCESS = 1;
    public static final int BATTLE_FINISHED = 2;
    public static final int ACCESS_DENIED = -1;
    public static final int URBLACK = 3;
    public static final int URWHITE = 4;
    public static final int UROUND = 5;
    private int action;
    private String data;

    public Msg(int action, String data) {
        this.action = action;
        this.data = data;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
