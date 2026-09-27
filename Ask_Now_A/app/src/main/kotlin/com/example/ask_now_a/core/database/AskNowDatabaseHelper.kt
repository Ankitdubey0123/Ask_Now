package com.example.ask_now_a.core.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.ask_now_a.features.reels.model.ReelModel

class AskNowDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "asknow_offline.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_REELS = "cached_reels"
        private const val COL_ID = "id"
        private const val COL_TITLE = "title"
        private const val COL_DESC = "description"
        private const val COL_TAG = "category_tag"
        private const val COL_VIDEO_URL = "video_url"
        private const val COL_THUMB_URL = "thumbnail_url"
        private const val COL_TEACHER_ID = "teacher_id"
        private const val COL_TEACHER_NAME = "teacher_name"
        private const val COL_TEACHER_EMAIL = "teacher_email"
        private const val COL_LIKES = "likes_count"
        private const val COL_COMMENTS = "comments_count"
        private const val COL_VIEWS = "views_count"
        private const val COL_LIKED = "liked_by_current_user"
        private const val COL_CREATED_AT = "created_at"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSql = """
            CREATE TABLE $TABLE_REELS (
                $COL_ID INTEGER PRIMARY KEY,
                $COL_TITLE TEXT NOT NULL,
                $COL_DESC TEXT,
                $COL_TAG TEXT,
                $COL_VIDEO_URL TEXT NOT NULL,
                $COL_THUMB_URL TEXT,
                $COL_TEACHER_ID INTEGER,
                $COL_TEACHER_NAME TEXT,
                $COL_TEACHER_EMAIL TEXT,
                $COL_LIKES INTEGER,
                $COL_COMMENTS INTEGER,
                $COL_VIEWS INTEGER,
                $COL_LIKED INTEGER,
                $COL_CREATED_AT TEXT
            )
        """.trimIndent()
        db.execSQL(createTableSql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REELS")
        onCreate(db)
    }

    fun getAllCachedReels(): List<ReelModel> {
        val list = mutableListOf<ReelModel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_REELS ORDER BY $COL_ID DESC", null)

        cursor.use { c ->
            if (c.moveToFirst()) {
                do {
                    val reel = ReelModel(
                        id = c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                        title = c.getString(c.getColumnIndexOrThrow(COL_TITLE)) ?: "",
                        description = c.getString(c.getColumnIndexOrThrow(COL_DESC)),
                        categoryTag = c.getString(c.getColumnIndexOrThrow(COL_TAG)) ?: "#ProblemSolving",
                        videoUrl = c.getString(c.getColumnIndexOrThrow(COL_VIDEO_URL)) ?: "",
                        thumbnailUrl = c.getString(c.getColumnIndexOrThrow(COL_THUMB_URL)),
                        teacherId = c.getInt(c.getColumnIndexOrThrow(COL_TEACHER_ID)),
                        teacherName = c.getString(c.getColumnIndexOrThrow(COL_TEACHER_NAME)) ?: "Teacher",
                        teacherEmail = c.getString(c.getColumnIndexOrThrow(COL_TEACHER_EMAIL)) ?: "",
                        likesCount = c.getInt(c.getColumnIndexOrThrow(COL_LIKES)),
                        commentsCount = c.getInt(c.getColumnIndexOrThrow(COL_COMMENTS)),
                        viewsCount = c.getInt(c.getColumnIndexOrThrow(COL_VIEWS)),
                        likedByCurrentUser = c.getInt(c.getColumnIndexOrThrow(COL_LIKED)) == 1,
                        createdAt = c.getString(c.getColumnIndexOrThrow(COL_CREATED_AT))
                    )
                    list.add(reel)
                } while (c.moveToNext())
            }
        }
        return list
    }

    fun cacheReels(reels: List<ReelModel>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.delete(TABLE_REELS, null, null)
            for (reel in reels) {
                insertReelInternal(db, reel)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun cacheSingleReel(reel: ReelModel) {
        val db = writableDatabase
        insertReelInternal(db, reel)
    }

    private fun insertReelInternal(db: SQLiteDatabase, reel: ReelModel) {
        val values = ContentValues().apply {
            put(COL_ID, reel.id)
            put(COL_TITLE, reel.title)
            put(COL_DESC, reel.description)
            put(COL_TAG, reel.categoryTag)
            put(COL_VIDEO_URL, reel.videoUrl)
            put(COL_THUMB_URL, reel.thumbnailUrl)
            put(COL_TEACHER_ID, reel.teacherId)
            put(COL_TEACHER_NAME, reel.teacherName)
            put(COL_TEACHER_EMAIL, reel.teacherEmail)
            put(COL_LIKES, reel.likesCount)
            put(COL_COMMENTS, reel.commentsCount)
            put(COL_VIEWS, reel.viewsCount)
            put(COL_LIKED, if (reel.likedByCurrentUser) 1 else 0)
            put(COL_CREATED_AT, reel.createdAt)
        }
        db.insertWithOnConflict(TABLE_REELS, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }
}
