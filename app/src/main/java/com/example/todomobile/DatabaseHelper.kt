import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TodoDatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "todos"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TASK = "task"
        private const val COLUMN_COMPLETED = "completed"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TASK TEXT,
                $COLUMN_COMPLETED INTEGER DEFAULT 0
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addTask(task: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK, task)
            put(COLUMN_COMPLETED, 0)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun getAllTasks(): List<TodoItem> {
        val taskList = mutableListOf<TodoItem>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val task = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK))
                val completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1
                taskList.add(TodoItem(id, task, completed))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return taskList
    }

    fun updateTask(todoItem: TodoItem) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK, todoItem.task)
            put(COLUMN_COMPLETED, if (todoItem.completed) 1 else 0)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(todoItem.id.toString()))
        db.close()
    }

    fun deleteTask(id: Long) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}

data class TodoItem(val id: Long, val task: String, var completed: Boolean)