package kg.kyrgyzcoder.primedoc02.ui.chat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kg.kyrgyzcoder.primedoc02.R

import kg.kyrgyzcoder.primedoc02.ui.chat.model.Chat
import kotlinx.android.synthetic.main.row_chat_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatListRecyclerViewAdapter(
    options: FirestoreRecyclerOptions<Chat>, private val listener: ChatListClickListener
) : FirestoreRecyclerAdapter<Chat, ChatListRecyclerViewAdapter.ViewHolder>(options) {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(chat: Chat) {
            val last = if (chat.lastMessage.length > 15)
                chat.lastMessage.take(15) + "..."
            else
                chat.lastMessage
            itemView.lastMessageTextView.text = last
            Log.d("ViewHolder", "onBind (line 24): ")
            val dateLast = chat.lastMessageTime.toDate()
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ROOT)
            val lastMessageTimeStr = sdf.format(dateLast)
            itemView.textViewDateChatList.text = lastMessageTimeStr
            setPhoneNumber(chat.clientId)
            itemView.senderNameTextView.text = if (chat.lastMessageSenderId == chat.clientId)
                itemView.context.getString(R.string.admin) + ":"
            else
                itemView.context.getString(R.string.you)

            itemView.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }

        private fun setPhoneNumber(clientId: String?) {
            FirebaseFirestore.getInstance().collection("users").document(clientId!!).get()
                .addOnSuccessListener {
                    val phoneNumber = it.getString("userPhone")
                    itemView.textViewUserName.text = phoneNumber
                    listener.loaded()
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_chat_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Chat) {
        holder.onBind(model)
    }

    interface ChatListClickListener {
        fun onClick(position: Int)
        fun loaded()
    }
}