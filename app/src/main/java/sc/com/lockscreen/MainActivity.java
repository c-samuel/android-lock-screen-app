package sc.com.lockscreen;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    Context context = MainActivity.this;

    private ComponentName deviceAdmin;
    public static final int REQUEST_ENABLE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lockScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void lockScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            deviceAdmin = new ComponentName(context, AdminReceiver.class);
            if (dpm.isAdminActive(deviceAdmin)) {
                Log.i(TAG, "Screen is locked");
                dpm.lockNow();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    this.finishAffinity();
                } else {
                    this.finish();
                    System.exit(0);
                }
            } else {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                        deviceAdmin);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "Request admin right for lock screen");
                startActivityForResult(intent, REQUEST_ENABLE);
            }
        } else {
            Toast.makeText(context, "Your version is too old! The function is not availabe.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i("DeviceAdminSample", "Admin enabled!");
                    lockScreen();
                } else {
                    Log.i("DeviceAdminSample", "Admin enable FAILED!");
                }
                return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
