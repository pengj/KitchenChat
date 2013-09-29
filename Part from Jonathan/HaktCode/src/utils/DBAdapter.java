package utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
	private static final String TABLE = "searchWordTable";
	private static final String AUTO_ID_COL = "_id";
	private static final String WORD_COL = "word";

	private DbHelper dbHelper;
	private SQLiteDatabase db;

	private static final String CREATE_TABLE = "create table " + TABLE + " "
			+ "(" + AUTO_ID_COL + " integer primary key autoincrement, "
			+ WORD_COL + " text not null);";

	public DBAdapter(Context context) {
		if (dbHelper == null) {
			dbHelper = new DbHelper(context);
		}
	}

	// open database connection
	public DBAdapter open(boolean readOnly) {
		if (db == null || !db.isOpen()) {
			// determines if database is to be read-only or not
			if (readOnly) {
				db = dbHelper.getReadableDatabase();
			} else {
				db = dbHelper.getWritableDatabase();
			}
		}
		return this;
	}

	// close database connection
	public void close() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

	// returns a list of all words from database
	public ArrayList<String> getWords() {

		ArrayList<String> words = new ArrayList<String>();
		open(true);

		// cursor will iterate through TABLE and get words from WORD_COL
		Cursor cursor = db.query(TABLE, new String[] { WORD_COL }, null, null,
				null, null, AUTO_ID_COL + " DESC");

		if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
			do {
				// saves word from database in the list words
				String word = cursor.getString(cursor.getColumnIndex(WORD_COL));

				words.add(word);
			} while (cursor.moveToNext());
		}
		close();
		return words;

	}

	// method for adding word to database
	public boolean insertWord(String word) {
		boolean success = false;
		boolean wordExists = false;

		// get all the current words in the database
		List<String> words = getWords();

		// iterate through words in list
		for (String s : words) {
			if (s.equalsIgnoreCase(word)) {
				// if the word in question exists, boolean is set to true
				wordExists = true;
			}
		}

		// if word exists, delete it
		// this is to prevent multiple entries of the same word
		// (and, at the same time, make the word appear on top of the list
		// as it's the newest search)
		if (wordExists)
			deleteWord(word);

		ContentValues values = new ContentValues(1);
		values.put(WORD_COL, word);

		// Open is set to false, so that the database is writable
		open(false);
		success = db.insert(TABLE, WORD_COL, values) > 0;
		close();
		return success;
	}

	// method for deleting a specific word from the database
	public boolean deleteWord(String word) {
		open(false);

		// the db.delete method requires input in form of an array,
		// thus making an array for only one word
		String[] args = { word };

		// delete from TABLE, where the word (in WORD_COL) equals words in args
		boolean deleted = db.delete(TABLE, WORD_COL + "=?", args) > 0;

		close();

		return deleted;
	}

	public void removeAll() {
		open(false);
		db.delete(TABLE, null, null);
		close();
	}

	private static class DbHelper extends SQLiteOpenHelper {

		private static final String DATABASE_NAME = "words_db";

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
}