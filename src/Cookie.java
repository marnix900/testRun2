import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class Cookie {
    int cookie;
    Cookie(int cookie) {
        this.cookie = cookie;
    }

    public int getCookie() {
        return this.cookie;
    }
    public MouseListener addCookie(int cookie) {
        this.cookie = cookie+1;
        return null;
    }
}
