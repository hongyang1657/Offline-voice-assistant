package fitme.ai.zotyeautoassistant.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by zzy on 2017/11/22.
 */

public class AccountGet {
    private String code;
    private String message;
    private Map<String, List<AccountInfo>> data;

    public AccountGet() {

    }

    public AccountGet(String code, String message, Map<String, List<AccountInfo>> data) {
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

    public Map<String, List<AccountInfo>> getData() {
        return data;
    }

    public void setData(Map<String, List<AccountInfo>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AccountGet{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public class AccountInfo {
        private String account;//账号
        private String password;//密码
        private long brand_id;//品牌id
        private String brand_name;//品牌名

        public AccountInfo() {

        }

        public AccountInfo(String acount, String password, long brand_id, String brand_name) {
            this.account = acount;
            this.password = password;
            this.brand_id = brand_id;
            this.brand_name = brand_name;
        }

        public String getAcount() {
            return account;
        }

        public void setAcount(String acount) {
            this.account = acount;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public long getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(long brand_id) {
            this.brand_id = brand_id;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }

        @Override
        public String toString() {
            return "AccountInfo{" +
                    "acount='" + account + '\'' +
                    ", password='" + password + '\'' +
                    ", brand_id=" + brand_id +
                    ", brand_name='" + brand_name + '\'' +
                    '}';
        }
    }
}
