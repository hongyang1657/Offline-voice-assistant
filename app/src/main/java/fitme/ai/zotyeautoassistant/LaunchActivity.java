package fitme.ai.zotyeautoassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fitme.ai.zotyeautoassistant.utils.Constants;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.PermissionsUtils;

public class LaunchActivity extends Activity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        boolean isPermissionsPassed = PermissionsUtils.getInstance().chekPermissions(this, Constants.permissions,permissionsResult);
        L.i("权限是否全部通过："+isPermissionsPassed);
        if (isPermissionsPassed){
            Intent intent = new Intent(this.getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
            L.i("权限通过");
        }

        @Override
        public void forbitPermissons() {
            L.i("权限不通过");
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.getInstance().onRequestPermissionsResult(this,requestCode,Constants.permissions,grantResults);
    }
}
