package ru.goodibunakov.tipcalculator;

import android.icu.text.NumberFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //переменные для форматирования денег и процентов
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat = NumberFormat.getPercentInstance();

    private double billAmount = 0.0; //Сумма счета, введенная пользователем
    private double percent = 0.15;   //Исходный процент чаевых
    private TextView amountTextView; //Для отформатированной суммы счета
    private TextView percentTextView;//Для вывода процента чаевых
    private TextView tipTextView;    //Для вывода вычисленных чаевых
    private TextView totalTextView;  //Для вычисленной общей суммы

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        percentTextView = (TextView) findViewById(R.id.percentTextView);
        amountTextView = (TextView) findViewById(R.id.amountTextView);
        tipTextView = (TextView) findViewById(R.id.tipTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);

        //заполняем нулями для начала
        tipTextView.setText(currencyFormat.format(0));
        totalTextView.setText(currencyFormat.format(0));

        //назначение слушателей
        EditText amountEditText = (EditText) findViewById(R.id.amountEditText);
        amountEditText.addTextChangedListener(amountEditTextWatcher);
        SeekBar percentSeekBar = (SeekBar) findViewById(R.id.percentSeekBar);
        percentSeekBar.setOnSeekBarChangeListener(seekBarListener);
    }

    //вычисление и вывод чаевых и общей суммы
    private void calculate(){
        //форматирование процентов и вывод в текстовое поле
        percentTextView.setText(percentFormat.format(percent));

        //вычисление чаевых и общей суммы
        double tip = billAmount * percent;
        double total = billAmount + tip;

        //вывод чаевых и общей суммы в формате денежной величины
        tipTextView.setText(currencyFormat.format(tip));
        totalTextView.setText(currencyFormat.format(total));
    }

    //объект слушателя событий SeekBar-а
    private final OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            percent = progress / 100.0;
            calculate();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    //объект слушателя событий для EditText
    private final TextWatcher amountEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //получить счет и вывести в формате денежной суммы
            try {
                billAmount = Double.parseDouble(s.toString()) / 100;
                amountTextView.setText(currencyFormat.format(billAmount));
            }
                //если пусто или ввели не число
                catch (NumberFormatException e){
                    amountTextView.setText("");
                    billAmount = 0.0;
            }
            calculate();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
}