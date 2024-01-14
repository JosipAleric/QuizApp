package ba.sum.fpmoz.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ba.sum.fpmoz.quizapp.models.User;

public class RegisterActivity extends AppCompatActivity {

    FirebaseDatabase db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.db = FirebaseDatabase.getInstance();
        this.mAuth = FirebaseAuth.getInstance();

        EditText registerFirstName = findViewById(R.id.registerFirstName);
        EditText registerLastName = findViewById(R.id.registerLastName);
        EditText registerEmail = findViewById(R.id.registerEmail);
        EditText registerPassword = findViewById(R.id.registerPassword);
        Button registerButton = findViewById(R.id.registerButton);

        DatabaseReference usersDbRef = this.db.getReference("users");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = registerFirstName.getText().toString();
                String lastName = registerLastName.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();

                if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Sva polja su obvezna", Toast.LENGTH_LONG).show();
                } else {
                    User user = new User(firstName, lastName, email, password);
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Uspješna registracija", Toast.LENGTH_LONG).show();
                                DatabaseReference newUserRef = usersDbRef.push();
                                newUserRef.setValue(user).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Neuspješno spremanje podataka, pokušajte ponovo kasnije", Toast.LENGTH_LONG).show();
                                    }
                                });
                                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            Intent intent = new Intent(RegisterActivity.this, StartQuizActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else{
                                            Toast.makeText(getApplicationContext(), "Neuspješna prijava u sustav, pokušajte ponovo kasnije", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else{
                                Toast.makeText(getApplicationContext(), "Neuspješna registracija, provjerite unesene podatke", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}