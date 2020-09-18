package kg.kyrgyzcoder.primedoc02.ui.chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kg.kyrgyzcoder.primedoc02.R
import kg.kyrgyzcoder.primedoc02.ui.chat.adapter.MessagesRecyclerViewAdapter
import kg.kyrgyzcoder.primedoc02.ui.chat.model.Message
import kg.kyrgyzcoder.primedoc02.util.*
import kotlinx.android.synthetic.main.fragment_chat_actual.*
import java.util.*


class ChatActualFragment : Fragment(), MessagesRecyclerViewAdapter.MessageClickListener {

    private lateinit var docRef: DocumentReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var adapter: MessagesRecyclerViewAdapter
    private var type: String = "text"
    private var ref: String? = ""
    private var image: String = ""
    private var imgUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_actual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrBackChatActual.setOnClickListener {
            hideKeyboard()
            findNavController().navigate(R.id.action_chatActualFragment_to_chatListFragment)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        ref = arguments?.getString(EXTRA_CHAT_REF)
        docRef = db.document(ref!!)

        initUI()

        attachButtonChat.setOnClickListener {
            openImageChoose()
        }

        sendMessageButton.setOnClickListener {
            if (messageEditText.text.toString().isNotEmpty()) {
                if (type == "text")
                    sendMessage()
                else
                    uploadPhotoToCloud()
            }
        }
    }


    private fun sendMessage() {
        imgViewAttach.visibility = View.GONE
        val message = messageEditText.text.toString()
        messageEditText.setText("")
        val user = firebaseAuth.currentUser!!
        val model = Message(user.uid, "", message, type, image)
        model.time = Timestamp.now()
        docRef.collection("messages").document().set(model)
            .addOnCompleteListener {
                val map = mutableMapOf<String, Any>()
                map["lastMessage"] = message
                map["lastMessageSenderId"] = user.uid
                map["lastMessageTime"] = Timestamp.now()
                docRef.set(map, SetOptions.merge()).addOnSuccessListener {
                    Log.d(
                        "ChatActualFragment",
                        "onViewCreated (line 70): success updated lastMessage"
                    )
                }
                recyclerViewMessagesChat.smoothScrollToPosition(0)
            }
    }

    private fun openImageChoose() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, "Выберите фото профила!"),
            CHOOSE_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CHOOSE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            val uri = data.data
            if (uri != null) {
                Log.d("Chat", "onActivityResult: $uri")
                imgViewAttach.setImageURI(uri)
                imgViewAttach.visibility = View.VISIBLE
                imgUri = uri
                type = "image"
                messageEditText.requestFocus()
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(messageEditText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadPhotoToCloud() {
        prBarChat.visibility = View.VISIBLE
        val user = firebaseAuth.currentUser
        if (user != null) {
            val ref =
                FirebaseStorage.getInstance().getReference("images/" + Date().time + ".jpg")
            ref.putFile(imgUri!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Chat", "uploadImageToCloud: downloadUrl: $it")
                    val url = it.toString()
                    if (url.isNotEmpty()) {
                        image = url
                        prBarChat.visibility = View.GONE
                        sendMessage()
                    } else {
                        prBarChat.visibility = View.GONE
                    }
                }
            }
        }
    }


    private fun initUI() {
        docRef.get().addOnSuccessListener {
            val clientId = it.getString("clientId")
            FirebaseFirestore.getInstance().collection("users").document(clientId!!).get()
                .addOnSuccessListener { sn ->
                    val userPhone = sn.getString("userPhone")
                    textViewUserNameChat.text = userPhone
                }
            videoCallButton.setOnClickListener {
                makeVideoCall(clientId)
            }
        }
    }

    private fun makeVideoCall(clientId: String) {
        val intent = Intent(requireContext(), CallUserActivity::class.java)
        intent.putExtra(EXTRA_USER_ID, clientId)
        startActivity(intent)
    }


    override fun onStart() {
        super.onStart()
        messageEditText.requestFocus()
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(messageEditText, InputMethodManager.SHOW_IMPLICIT)
        recyclerViewMessagesChat.smoothScrollToPosition(0)
        Log.d("ChatActualFragment", "onStart (line 100): startSmooth")
        val user = firebaseAuth.currentUser
        if (user != null)
            initRecyclerView(user)
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    private fun initRecyclerView(user: FirebaseUser) {

        recyclerViewMessagesChat.setHasFixedSize(true)

        val query =
            docRef.collection("messages").orderBy("time", Query.Direction.DESCENDING)
        val options: FirestoreRecyclerOptions<Message> =
            FirestoreRecyclerOptions.Builder<Message>().setQuery(query, Message::class.java).build()

        adapter = MessagesRecyclerViewAdapter(options, this)
        recyclerViewMessagesChat.adapter = adapter

        adapter.startListening()
        recyclerViewMessagesChat.smoothScrollToPosition(0)
        Log.d("ChatActualFragment", "initRecyclerView (line 124): scroll")

        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                recyclerViewMessagesChat.smoothScrollToPosition(0)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                recyclerViewMessagesChat.smoothScrollToPosition(0)
            }
        }
        adapter.registerAdapterDataObserver(observer)

        recyclerViewMessagesChat.smoothScrollToPosition(0)

        recyclerViewMessagesChat.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                recyclerViewMessagesChat.postDelayed({
                    Log.d("InitRecyclerView", "initRecyclerView: postDelayed")
                    recyclerViewMessagesChat.smoothScrollToPosition(0)
                }, 100)
            }
        }
    }

    override fun onImageClick(position: Int) {

        hideKeyboard()
        val bundle = Bundle()
        bundle.putString(EXTRA_IMG_URL, image)
        bundle.putString(EXTRA_CHAT_REF, ref)
        findNavController().navigate(
            R.id.action_chatActualFragment_to_imageFullFragment,
            bundle
        )
    }


}