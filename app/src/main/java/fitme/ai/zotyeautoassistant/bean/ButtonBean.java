package fitme.ai.zotyeautoassistant.bean;

/**
 * Created by zzy on 2017/11/2.
 */

public class ButtonBean {
   private String button_name; //按钮名称
   private String command_code; //命令码

    public ButtonBean() {

    }

    public ButtonBean(String button_name, String command_code) {
        this.button_name = button_name;
        this.command_code = command_code;
    }

    public String getButton_name() {
        return button_name;
    }

    public void setButton_name(String button_name) {
        this.button_name = button_name;
    }



    public String getCommand_code() {
        return command_code;
    }

    public void setCommand_code(String command_code) {
        this.command_code = command_code;
    }

    @Override
    public String toString() {
        return "ButtonBean{" +
                "button_name='" + button_name + '\'' +
                ", command_code='" + command_code + '\'' +
                '}';
    }
}
