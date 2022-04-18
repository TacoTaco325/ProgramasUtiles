package com.example.webservicedni;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class adminSqlLite extends SQLiteOpenHelper {
    public adminSqlLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase BDPersona) {
        BDPersona.execSQL("CREATE TABLE Persona( DNI int PRIMARY KEY , Nombres TEXT,  apPaterno TEXT, apMaterno TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
