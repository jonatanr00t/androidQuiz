package com.jonatan.geoquizj;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;


    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceanos, true),
            new Question(R.string.question_mediterraneo, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_america, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;

    private boolean mIsCheater;


    private void updateQuestion() {
//        Log.d(TAG, "Updating question text for question #" + mCurrentIndex,
//                new Exception());
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    //método que checkea la respuesta, crea y muestra el Toast correspondiente
    private void checkAnswer(boolean eligioTrue) {
        boolean esTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        int color = 0;

        if(mIsCheater){
            messageResId = R.string.judgment_toast;
            color = Color.BLACK;
        }else {
            if (eligioTrue == esTrue) {
                messageResId = R.string.correct_toast;
                color = Color.GREEN;
            } else {
                messageResId = R.string.incorrect_toast;
                color = Color.RED;
            }
        }

        Toast toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);

        View view = toast.getView();
        view.setBackgroundColor(color);
        view.setSoundEffectsEnabled(true);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Se llamó a onCreate(Bundle)");
        setContentView(R.layout.activity_quiz);

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CheatActivity;
                // Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                // startActivity(i);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
//                startActivity(i);
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        updateQuestion();

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex > 0) {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                    mIsCheater = false;
                    updateQuestion();
                }
            }
        });

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Aqui se observa una particular manera de controlar que el resultCode sea OK y que el data
//                no haya venido en null para luego asignarle valor a mIsCheater
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Se llamó a onStart()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Se llamó a onPause()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Se llamó a onResume()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Se llamó a onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Se llamó a onDestroy()");
    }
}
