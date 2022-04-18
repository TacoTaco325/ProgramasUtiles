package com.example.qrprueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class generar extends AppCompatActivity {

    EditText txtqr;
    ImageView img;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar);

        txtqr = findViewById(R.id.txtqr);
        img = findViewById(R.id.imageView);
        btn = findViewById(R.id.btnGenerar);

        txtqr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                MultiFormatWriter mtf = new MultiFormatWriter();

                try {
                    BitMatrix bm = mtf.encode(txtqr.getText().toString(), BarcodeFormat.QR_CODE,500,500);
                    BarcodeEncoder be = new BarcodeEncoder();
                    Bitmap bitmap = be.createBitmap(bm);
                    img.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void clia(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}