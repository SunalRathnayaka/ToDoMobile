import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todomobile.R

class TodoAdapter(
    private val tasks: MutableList<TodoItem>,
    private val onTaskUpdate: (TodoItem) -> Unit,
    private val onTaskDelete: (Long) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.taskCheckBox)
        val textView: TextView = itemView.findViewById(R.id.taskTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val task = tasks[position]
        holder.textView.text = task.task
        holder.checkBox.isChecked = task.completed

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            task.completed = isChecked
            onTaskUpdate(task)
        }

        holder.deleteButton.setOnClickListener {
            onTaskDelete(task.id)
            tasks.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount() = tasks.size

    fun updateTasks(newTasks: List<TodoItem>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}