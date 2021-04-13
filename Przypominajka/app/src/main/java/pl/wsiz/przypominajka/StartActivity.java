package pl.wsiz.przypominajka;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    Animation start1anim, start2anim;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Objects.requireNonNull(getSupportActionBar()).hide();

        txt = (TextView) findViewById(R.id.startTextAnim);
        start1anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start1anim);
        txt.startAnimation(start1anim);

        start1anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                start2anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start2anim);
                txt.startAnimation(start2anim);
                start2anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
