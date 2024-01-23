package ba.sum.fpmoz.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ba.sum.fpmoz.quizapp.models.Question;

public class QuizActivity extends AppCompatActivity {
    private FirebaseDatabase db;
    private List<Question> allQuestions;
    private List<Question> randomQuestions;
    private int currentQuestionIndex;
    private int correctAnswersCount;
    private TextView questionTitle;
    private TextView questionProgress;
    private RadioGroup answerOptions;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        this.db = FirebaseDatabase.getInstance();
        questionTitle = findViewById(R.id.questionTitle);
        answerOptions = findViewById(R.id.answerOptions);
        nextButton = findViewById(R.id.nextButton);
        questionProgress = findViewById(R.id.questionProgress);

        DatabaseReference questionsDbRef = this.db.getReference("questions");

        allQuestions = new ArrayList<>();
        randomQuestions = new ArrayList<>();

        questionsDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if there is at least one question
                if (dataSnapshot.exists()) {
                    for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                        // Convert each question snapshot to a Question object
                        Question question = questionSnapshot.getValue(Question.class);
                        allQuestions.add(question);
                    }
                    Collections.shuffle(allQuestions);
                    randomQuestions = allQuestions.subList(0, Math.min(5, allQuestions.size()));
                    showQuestion(randomQuestions, currentQuestionIndex);
                } else {
                    Log.d("QuizActivity", "No questions found in the database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QuizActivity", "Error fetching data: " + databaseError.getMessage());
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if a radio button is selected
                if (answerOptions.getCheckedRadioButtonId() != -1) {
                    RadioButton selectedRadioButton = findViewById(answerOptions.getCheckedRadioButtonId());

                    String selectedAnswer = selectedRadioButton.getText().toString().substring(3);

                    // Get the correct answer for the current question
                    String correctAnswer = randomQuestions.get(currentQuestionIndex).getCorrect_ans();

                    boolean isCorrect = selectedAnswer.equals(correctAnswer);
                    if (isCorrect) {
                        correctAnswersCount++;
                    }
                    currentQuestionIndex++;
                    if (currentQuestionIndex < randomQuestions.size()) {
                        showQuestion(randomQuestions, currentQuestionIndex);
                    } else {
                        Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
                        intent.putExtra("CORRECT_ANSWERS_COUNT", correctAnswersCount);
                        intent.putExtra("NUMBER_OF_QUESTIONS", randomQuestions.size());
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Izaberite jedan od ponuđenih odgovora", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showQuestion(List<Question> questions, int index) {
        questionTitle.setText(questions.get(index).getTitle());
        questionProgress.setText(getString(R.string.question_progress, index + 1, questions.size()));
        answerOptions.clearCheck();

        List<String> options = questions.get(index).getOptions();
        for (int i = 0; i < options.size(); i++) {
            RadioButton radioButton = (RadioButton) answerOptions.getChildAt(i);
            String prefix = String.valueOf((char) ('A' + i));
            radioButton.setText(prefix + ") " + options.get(i));
        }
    }

}
