package fitme.ai.zotyeautoassistant.bean;

import java.io.Serializable;

/**
 * Created by yml on 2017/10/12.
 */

public class TokenInfo implements Serializable {

    private String status;
    // token
    private String token;
    // token失效时间
    private long expires;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                "status='" + status + '\'' +
                ", token=" + token +
                ", expires=" + expires +
                '}';
    }
}
