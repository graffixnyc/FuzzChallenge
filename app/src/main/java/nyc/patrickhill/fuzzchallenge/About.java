package nyc.patrickhill.fuzzchallenge;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ImageView imgMe = (ImageView)findViewById(R.id.ivMe);
        TextView link = (TextView) findViewById(R.id.pi);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        imgMe.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.youtube.com/watch?v=OVHLHFTKolQ"));
                startActivity(intent);
            }
        });
    }
}
