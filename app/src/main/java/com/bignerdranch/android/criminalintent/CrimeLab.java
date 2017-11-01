package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.criminalintent.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent.database.CrimeCUrsorWrapper;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private CrimeLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public static CrimeLab get(Context context){
        if (sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes(){
        List<Crime> crimes = new ArrayList<Crime>();
        CrimeCUrsorWrapper cursor = queryCrimes(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }
    public Crime getCrime(UUID id) {

        CrimeCUrsorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }
    public boolean deleteCrime(Crime crime){
        return mDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " = ?", new String[] {crime.getId().toString()}) > 0;
    };
    public void addCrime(Crime c) {
        ContentValues cv = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, cv);
    }
    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues cv = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, cv, CrimeTable.Cols.UUID+" = ?", new String[] {uuidString});
    }
    private CrimeCUrsorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new CrimeCUrsorWrapper(cursor);
    }
    private static ContentValues getContentValues(Crime crime){
        ContentValues cv = new ContentValues();
        cv.put(CrimeTable.Cols.UUID, crime.getId().toString());
        cv.put(CrimeTable.Cols.TITLE, crime.getTitle());
        cv.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        cv.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return cv;
    }
}
