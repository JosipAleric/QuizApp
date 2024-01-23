package ba.sum.fpmoz.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ResultsActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        int correctAnswersCount = getIntent().getIntExtra("CORRECT_ANSWERS_COUNT", 0);
        int numberOfQuestions = getIntent().getIntExtra("NUMBER_OF_QUESTIONS", 0);
        String message = "Uspješno ste završili igru i osvojili ukupno " + correctAnswersCount + " bodova";

        this.mAuth = FirebaseAuth.getInstance();
        TextView pointsTextView = findViewById(R.id.points);
        TextView questionsTextView = findViewById(R.id.totalQuestions);
        TextView resultsMessage = findViewById(R.id.resultsMessage);
        Button playAgainBtn = findViewById(R.id.playAgainButton);
        Button logoutBtn = findViewById(R.id.logoutButton);

        pointsTextView.setText(String.valueOf(correctAnswersCount));
        questionsTextView.setText(String.valueOf(numberOfQuestions));
        resultsMessage.setText(message);

        playAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, QuizActivity.class);
                startActivity(intent);
                finish();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}