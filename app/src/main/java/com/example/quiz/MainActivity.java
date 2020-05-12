package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz.data.AnwserListAsyncRespone;
import com.example.quiz.data.QuestionBank;
import com.example.quiz.model.Question;
import com.example.quiz.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String MESSAGE_ID = "messages_pref";
    private TextView questionTextView;
    private TextView questionCounterTextView;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private int currentQuestionIndex = 0;
    List<Question> questionList;
    private int score = 0;
    private int bestScore;
    private TextView scoreTextView;
    private TextView bestScoreTextView;
    private Button saveButton;
    private Prefs prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new Prefs(MainActivity.this);

        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        questionCounterTextView = findViewById(R.id.counter_text);
        questionTextView = findViewById(R.id.question_textview);
        scoreTextView = findViewById(R.id.textView_currentScore);
        bestScoreTextView = findViewById(R.id.textView_bestScore);
        saveButton = findViewById(R.id.button_save);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);


        bestScoreTextView.setText(MessageFormat.format("Best Score: {0}", prefs.getHighScore()));
        scoreTextView.setText(MessageFormat.format("Your score: {0}", prefs.getScore()));

        currentQuestionIndex = prefs.getState();
        score = prefs.getScore();

        questionList = new QuestionBank().getQuestions(new AnwserListAsyncRespone() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounterTextView.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex, questionArrayList.size()));

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev_button:
                if (currentQuestionIndex != 0) {
                    currentQuestionIndex = (currentQuestionIndex-1) % questionList.size();
                    updateQuestion();
                }
                break;
            case R.id.next_button:
                currentQuestionIndex = (currentQuestionIndex+1) % questionList.size();
                updateQuestion();
                break;
            case R.id.true_button:
                checkAnswer(true);
                updateQuestion();
                break;
            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                break;
            case R.id.button_save:
                saveScore();
                break;
        }
    }

    private void saveScore() {
        String toastMessageId = "";
        if (score > prefs.getHighScore()) {
            prefs.saveHighestScore(score);
            toastMessageId = "New highest score!";
            String bestScore = "Best Score: " + score;
            bestScoreTextView.setText(bestScore);
            updateQuestion();
        } else toastMessageId = "Score is too low";
        Toast.makeText(MainActivity.this, toastMessageId, Toast.LENGTH_SHORT).show();
    }

    private void updateQuestion(){
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextView.setText(question);
        questionCounterTextView.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex, questionList.size()));
    }

    private void checkAnswer(boolean userChoice) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnwserTrue();
        if (userChoice == answerIsTrue) {
            fadeView();
            addToScore();
        } else {
            shakeAnimationWrong();
        }
    }

    private void addToScore() {
        score++;
        String newCurrentScore = "Your score: " + score;
        scoreTextView.setText(newCurrentScore);
    }

    private void shakeAnimationWrong() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                currentQuestionIndex = (currentQuestionIndex+1) % questionList.size();
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeView() {

        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0F, 0.0F);

        alphaAnimation.setDuration(200);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                currentQuestionIndex = (currentQuestionIndex+1) % questionList.size();
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onPause() {
        prefs.setState(currentQuestionIndex);
        prefs.setScore(score);
        super.onPause();
    }
}
