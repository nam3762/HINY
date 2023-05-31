package com.example.hiny;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class CheckActivity extends Activity {
    private ImageButton homebtn;
    Integer[] questionNumber = new Integer[4];
    Integer realQuestion = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_check);
        homebtn = (ImageButton) findViewById(R.id.home);
        Button answer1 = findViewById(R.id.btn1);
        Button answer2 = findViewById(R.id.btn2);
        Button answer3 = findViewById(R.id.btn3);
        Button answer4 = findViewById(R.id.btn4);
        String test = "test";
        Question[] questions = new Question[24];
        questions[0] = new Question("당신은 어떤 증세가 있으십니까?", null, null);
        questions[1] = new Question("나이가 어떻게 되십니까?", "감기 증세", null);
        questions[2] = new Question("나이가 어떻게 되십니까?", "통증 혹은 염증", null);
        questions[3] = new Question("육류 혹은 어류가 포함된 식사를 하셨습니까?", "소화불량", null);
        questions[4] = new Question("타박상으로 인한 통증을 느끼십니까?", "근골격계", "제일 쿨파프");
        questions[5] = new Question(null, "12세 이하", "어린이 부루펜");
        questions[6] = new Question("기침 가래 증상이 있으십니까?", "13세 이상", null);
        questions[7] = new Question(null, "기침 가래 증상 있음", "판콜에이");
        questions[8] = new Question(null, "기침 가래 증상 없음", "판피린티");
        questions[9] = new Question(null, "1세 이하", "어린이용 타이레놀 현탁액");
        questions[10] = new Question("알약을 드실 수 있으십니까?", "2세 ~ 6세", null);
        questions[11] = new Question(null, "알약 섭취 가능", "어린이용 타이레놀 80mg");
        questions[12] = new Question(null, "알약 섭취 불가능", "어린이용 타이레놀 현탁액");
        questions[13] = new Question("알약을 드실 수 있으십니까?", "6세 ~ 12세", null);
        questions[14] = new Question(null, "알약 섭취 가능", "어린이용 타이레놀 160mg");
        questions[15] = new Question(null, "13세 이상", "타이레놀 500mg");
        questions[16] = new Question("현재 복통이 심하십니까?", "육류 / 어류 포함 식사", null);
        questions[17] = new Question("현재 복통이 심하십니까?","육류 / 어류 미포함 식사",  null);
        questions[18] = new Question(null, "복통 있음", "닥터 베아제");
        questions[19] = new Question(null, "복통 없음", "훼스탈 골드");
        questions[20] = new Question(null, "복통 있음", "훼스탈 플러스");
        questions[21] = new Question(null, "복통 없음", "베아제");
        questions[22] = new Question(null, "타박상", "제일 쿨파프");
        questions[23] = new Question(null, "근육통", "신신파스 아레스");

        setButtonLabels(questions, getNumberList(0));



        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonLabels(questions, getNumberList(questionNumber[0]));
            }
        });
        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonLabels(questions, getNumberList(questionNumber[1]));
            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonLabels(questions, getNumberList(questionNumber[2]));
            }
        });
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonLabels(questions, getNumberList(questionNumber[3]));
            }
        });

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("EXIT",true);
                setResult(RESULT_OK,intent);
                finish();
            }
        });


    }
    public List<Integer> getNumberList(int input) {
        List<Integer> numberList = new ArrayList<>();
        realQuestion = input;
        if (input == 0) {
            numberList.add(1);
            numberList.add(2);
            numberList.add(3);
            numberList.add(4);
        } else if (input == 1) {
            numberList.add(5);
            numberList.add(6);
        } else if (input == 6) {
            numberList.add(7);
            numberList.add(8);
        } else if (input == 2) {
            numberList.add(9);
            numberList.add(10);
            numberList.add(13);
            numberList.add(15);
        } else if (input == 10) {
            numberList.add(11);
            numberList.add(12);
        } else if (input == 13) {
            numberList.add(14);
            numberList.add(12);
        } else if (input == 3) {
            numberList.add(16);
            numberList.add(17);
        } else if (input == 16) {
            numberList.add(18);
            numberList.add(19);
        } else if (input == 17) {
            numberList.add(20);
            numberList.add(21);
        } else if (input == 4) {
            numberList.add(22);
            numberList.add(23);
        }
        else {
            numberList.add(-1);
        }

        return numberList;
    }

    public void setButtonLabels(Question[] questions, List<Integer> index) {
        Button answer1 = findViewById(R.id.btn1);
        Button answer2 = findViewById(R.id.btn2);
        Button answer3 = findViewById(R.id.btn3);
        Button answer4 = findViewById(R.id.btn4);
        TextView question_view = findViewById(R.id.lovejunhyun);
        if (index.size() != 1) {
            answer1.setText(questions[index.get(0)].getAnswer());
            answer2.setText(questions[index.get(1)].getAnswer());
            questionNumber[0] = index.get(0);
            questionNumber[1] = index.get(1);
            if (index.size() > 2) {
                answer3.setText(questions[index.get(2)].getAnswer());
                answer4.setText(questions[index.get(3)].getAnswer());
                answer3.setVisibility(View.VISIBLE);
                answer4.setVisibility(View.VISIBLE);
                questionNumber[2] = index.get(2);
                questionNumber[3] = index.get(3);
            }
            else {
                answer3.setVisibility(View.GONE);
                answer4.setVisibility(View.GONE);
            }
        }
        else {
            answer1.setVisibility(View.GONE);
            answer2.setVisibility(View.GONE);
            answer3.setVisibility(View.GONE);
            answer4.setVisibility(View.GONE);
        }
        if (questions[realQuestion].getQuestion() != null) {
            question_view.setText(questions[realQuestion].getQuestion());
        }
        else {
            question_view.setText(questions[realQuestion].getDrug());
        }
    }
}
