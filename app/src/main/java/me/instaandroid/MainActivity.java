package me.instaandroid;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView iv_instaLogin;
    TextView tv_userDetail;
    private InstagramApp mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setView();
        setInstaListner();

    }

    private void setInstaListner() {
        if (mApp!=null)
        {
            mApp.resetAccessToken();
            mApp=null;
        }

        mApp = new InstagramApp(this, ApplicationData.CLIENT_ID,
                ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        mApp.setListener(listener);

    }


    InstagramApp.OAuthAuthenticationListener listener = new InstagramApp.OAuthAuthenticationListener() {

        @Override
        public void onSuccess() {
            tv_userDetail.setText("User Name ::"+mApp.getUserName()+"\n Name :: "+mApp.getName()+"\n Id :: "+mApp.getId());
        }

        @Override
        public void onFail(String error) {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
        }
    };

    private void setView() {
        iv_instaLogin=findViewById(R.id.iv_instaLogin);
        tv_userDetail=findViewById(R.id.tv_userDetail);

        iv_instaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mApp.hasAccessToken()) {
                   final AlertDialog.Builder builder = new AlertDialog.Builder(
                            MainActivity.this);
                    builder.setMessage("Are you sure you want to disconnect from Instagram?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            setInstaListner();
                                            mApp.resetAccessToken();
                                            tv_userDetail.setText("");
                                        }
                                    })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    final AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    mApp.authorize();
                }

            }
        });
    }
}
