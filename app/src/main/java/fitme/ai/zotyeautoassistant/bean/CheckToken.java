package fitme.ai.zotyeautoassistant.bean;

/**
 * Created by zzy on 2018/1/25.
 */

public class CheckToken {
    private String status;//接口状态
    private boolean is_valid; //是否有效
    private long expires;//过期时间,UnixTime

    public CheckToken() {
    }

    public CheckToken(String status, boolean is_valid, long expires) {
        this.status = status;
        this.is_valid = is_valid;
        this.expires = expires;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean is_valid() {
        return is_valid;
    }

    public void setIs_valid(boolean is_valid) {
        this.is_valid = is_valid;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    @Override
    public String toString() {
        return "CheckToken{" +
                "status='" + status + '\'' +
                ", is_valid=" + is_valid +
                ", expires=" + expires +
                '}';
    }
}
