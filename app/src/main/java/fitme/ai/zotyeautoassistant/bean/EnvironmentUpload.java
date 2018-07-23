package fitme.ai.zotyeautoassistant.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by zzy on 2018/1/4.
 */

public class EnvironmentUpload {
    private String code;
    private String message;
    private Map<String, List<AccountGet.AccountInfo>> data;

    public EnvironmentUpload() {

    }

    public EnvironmentUpload(String code, String message, Map<String, List<AccountGet.AccountInfo>> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, List<AccountGet.AccountInfo>> getData() {
        return data;
    }

    public void setData(Map<String, List<AccountGet.AccountInfo>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EnvironmentUpload{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
