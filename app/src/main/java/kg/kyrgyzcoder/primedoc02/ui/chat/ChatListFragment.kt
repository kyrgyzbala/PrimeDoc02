package kg.kyrgyzcoder.primedoc02.ui.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kg.kyrgyzcoder.primedoc02.R
import kg.kyrgyzcoder.primedoc02.ui.chat.adapter.ChatListRecyclerViewAdapter
import kg.kyrgyzcoder.primedoc02.ui.chat.model.Chat
import kg.kyrgyzcoder.primedoc02.util.EXTRA_CHAT_REF
import kotlinx.android.synthetic.main.fragment_chat_list.*


class ChatListFragment : Fragment(), ChatListRecyclerViewAdapter.ChatListClickListener {

    private lateinit var adapter: ChatListRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null)
            initRecyclerView(user)
    }

    override fun onStart() {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null)
            initRecyclerView(user)
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    private fun initRecyclerView(user: FirebaseUser) {

        Log.d("ChatChatListFragment", "initRecyclerView (line 48): ${user.uid}")
        val db = FirebaseFirestore.getInstance()
        val query = db.collection("PrimeDocChat").whereEqualTo("adminId", user.uid)
            .orderBy("lastMessageTime", Query.Direction.DESCENDING)
        val options: FirestoreRecyclerOptions<Chat> =
            FirestoreRecyclerOptions.Builder<Chat>().setQuery(query, Chat::class.java).build()

        adapter = ChatListRecyclerViewAdapter(options, this)
        Log.d("ChatChatListFragment", "initRecyclerView (line 56): ")
        recyclerViewChatChat.adapter = adapter
        adapter.startListening()

    }

    override fun onClick(position: Int) {
        val ref = adapter.snapshots.getSnapshot(position).reference.path
        val bundle = Bundle()
        bundle.putString(EXTRA_CHAT_REF, ref)
        findNavController().navigate(R.id.action_chatListFragment_to_chatActualFragment, bundle)
    }

    override fun loaded() {
        prBarChatList.visibility = View.GONE
    }


}