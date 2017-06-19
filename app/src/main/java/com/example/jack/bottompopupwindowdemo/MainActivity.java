package com.example.jack.bottompopupwindowdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomPopupWindowView bottomPopupWindowView;
    private TextView text;
    private View bottomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text=(TextView)findViewById(R.id.text);
        bottomView=LayoutInflater.from(this).inflate(R.layout.layout_bottom_view,null);
        text.setOnClickListener(this);
        bottomPopupWindowView=(BottomPopupWindowView)findViewById(R.id.bottom_popup);
        bottomPopupWindowView.setOnClickListener(this);
        bottomPopupWindowView.setBaseView(bottomView);
        bottomPopupWindowView.setContextView(LayoutInflater.from(this).inflate(R.layout.layout_content_view,null));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bottom_popup:
//                Toast.makeText(MainActivity.this,"bottom_popup",Toast.LENGTH_SHORT).show();
                bottomPopupWindowView.dismssPopupView(bottomPopupWindowView);
                break;
            case R.id.text:
//                Toast.makeText(MainActivity.this,"text",Toast.LENGTH_SHORT).show();
                bottomPopupWindowView.showPopouView(bottomPopupWindowView);
                break;
        }
    }

}
