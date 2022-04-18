package com.example.correoprueba;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sql.DataSource;

public class MainActivity extends AppCompatActivity {

    ImageView img,imgmostrar;
    Button btnqr,btnmostrar,btnenviar,btneliminar,btndialog;
    Bitmap bitmap;
    String file_name;
    File new_file;
    private Multipart _multipart = new MimeMultipart();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
    String date = simpleDateFormat.format(new Date());
    String correo = "1cineplanet2@gmail.com";
    String contraseña = "1cineplanet";
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        img = findViewById(R.id.qr);
        btnqr = findViewById(R.id.btn);
        imgmostrar = findViewById(R.id.qr2);
        btnmostrar = findViewById(R.id.btn2);
        btnenviar = findViewById(R.id.btn3);
        btneliminar = findViewById(R.id.btn4);
        btndialog = findViewById(R.id.btn5);


        MultiFormatWriter mtf = new MultiFormatWriter();
        BitMatrix bm = null;
        try {
            bm = mtf.encode("1", BarcodeFormat.QR_CODE,500,500);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        BarcodeEncoder be = new BarcodeEncoder();
        bitmap = be.createBitmap(bm);
        img.setImageBitmap(bitmap);

        btnqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        btnmostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = BitmapFactory.decodeFile(new_file.getAbsolutePath());
                imgmostrar.setImageBitmap(bitmap);
            }
        });

        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                correo();

            }
        });

        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_file.delete();
            }
        });

        btndialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Correo");

                final EditText weightInput = new EditText(MainActivity.this);
                weightInput.setInputType(InputType.TYPE_CLASS_TEXT);
                dialog.setView(weightInput);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this,weightInput.getText().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog.create().show();
            }
        });
    }

    public void correo(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Properties properties =  new Properties();
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.auth","true");

        try {

            session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correo,contraseña);
                }
            });

            if (session != null){
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(correo));
                message.setSubject("CINEPLANET: Compra Realizada Exitosamente");
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("aldaircano325@gmail.com"));
                BodyPart Descripcion = new MimeBodyPart();
                BodyPart IMAGEN = new MimeBodyPart();

                Descripcion.setContent("hola","text/html");

                FileDataSource source = new FileDataSource(file_name);
                IMAGEN.setDataHandler(new DataHandler(source));
                IMAGEN.setFileName(file_name);
                _multipart.addBodyPart(IMAGEN);
                _multipart.addBodyPart(Descripcion);
                message.setContent(_multipart);
                Transport.send(message);
            }

        }catch (Exception e){
            System.out.println("ERRRRRRRRRRRRRRRRRRRRRRRRRORRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
        }
    }

    public void save(){
        FileOutputStream fileOutputStream = null;
        File file = getDisc();
        if (!file.exists() && !file.mkdirs()){
            Toast.makeText(this,"no", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = "Img"+date+".jpg";
        file_name = file.getAbsolutePath()+"/"+name;
        new_file = new File(file_name);
        try {
            fileOutputStream = new FileOutputStream(new_file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshGalery(new_file);

    }

    private void refreshGalery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }


    private File getDisc(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "imagedemo");
    }


}