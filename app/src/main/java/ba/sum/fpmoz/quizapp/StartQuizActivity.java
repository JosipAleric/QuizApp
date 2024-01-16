package ba.sum.fpmoz.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartQuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

//        Button startQuizBtn = findViewById(R.id.startQuizButton);
//
//        startQuizBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(StartQuizActivity.this, QuizActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }
}