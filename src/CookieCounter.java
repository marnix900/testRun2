
public class CookieCounter {
    private int cookie;
    private int increment = 1;
    CookieCounter(int cookie) {
        this.cookie = cookie;
    }

    public int getCookie() {
        return this.cookie;
    }

    public void setCookie(int cookie) {
        this.cookie = cookie;
    }

    public int getIncrement() {
        return this.increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public void addCookie() {
        this.cookie = this.cookie + increment;
    }
}
