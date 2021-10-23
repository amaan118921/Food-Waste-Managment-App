package com.example.foodwastemanagmentapp.viewModel

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.restaurants.AddRequestFragment
import com.example.foodwastemanagmentapp.restaurants.RestaurantHomeFragment
import com.example.foodwastemanagmentapp.restaurants.RestaurantHomeFragment.Companion.binding
import com.example.foodwastemanagmentapp.room.Dao
import com.example.foodwastemanagmentapp.room.ModelClasses
import com.example.foodwastemanagmentapp.room.NgoInfo
import com.example.foodwastemanagmentapp.room.UserInfo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import kotlin.properties.Delegates

class CommonViewModel(private val dao: Dao, private val fragment: Fragment): ViewModel() {
    private lateinit var title: String
    private lateinit var date: String
    private lateinit var add: String
    private lateinit var description: String
    private lateinit var data: ModelClasses
    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var ref: DatabaseReference
    private var key by Delegates.notNull<Int>()
     val bookmarkedList = mutableListOf<ModelClasses>()
    private lateinit var phone: String
     var list = MutableLiveData<MutableList<ModelClasses>>()




    init {
        getBookmarkList()
    }


    fun insertInRoomAndFirebase(title: String, add: String, description: String, date: String){
        val data = ModelClasses(title = title, address = add, desc = description, date = "10 Sep")
        insertInFirebase(data = data)
        insertData(data = data)
    }
    fun setPhone(phone: String) {
        this.phone = phone
    }
    fun getPhone(): String {
        return phone
    }

    fun updateInRoomAndFirebase(data: ModelClasses) {
        updateInRoom(data)
    }

    private fun updateInRoom(data: ModelClasses) {
        viewModelScope.launch {
            dao.update(data)
        }
    }


    fun insertNgoDetails(name: String, phone: String, uri: String, email: String) {
        val ngo = NgoInfo(name = name, phone = phone, uri = uri, email = email)
        insertNgoUser(ngo)
    }

    private fun insertNgoUser(ngo: NgoInfo) {
        viewModelScope.launch {
            dao.insertNgoUser(ngo)
        }
    }
    fun insertUserDetails(name: String, phone: String, uri: String, email: String) {
        val user = UserInfo(name = name, phone = phone, uri = uri, email = email)
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        reference = database.reference.child("users").child(auth.uid!!)


            reference.setValue(user).addOnCompleteListener(object: OnCompleteListener<Void> {
                override fun onComplete(p0: Task<Void>) {
                    if(p0.isSuccessful) {
                        Toast.makeText(fragment.requireContext(), "Registered Successfully", Toast.LENGTH_SHORT).show()
                    }
                }

            })


    }
        fun getBookmarkList() {
           auth = FirebaseAuth.getInstance()
           database = FirebaseDatabase.getInstance()
           GlobalScope.launch(Dispatchers.IO) {


               val ref = database.reference.child("bookmarks").child(auth.uid!!)
               ref.addValueEventListener(object : ValueEventListener {
                   override fun onDataChange(snapshot: DataSnapshot) {
                       for (snap: DataSnapshot in snapshot.children) {
                           val item = snap.getValue(ModelClasses::class.java)
                           bookmarkedList.add(item!!)
                       }
                   }

                   override fun onCancelled(error: DatabaseError) {
                       TODO("Not yet implemented")
                   }

               })
           }
    }


    fun getBookmark(): Int {
        return key
    }


    fun checkBookmark(item: ModelClasses){
        key = if(bookmarkedList.contains(item)) {
            1
        } else  {
            0
        }

    }
    fun fetchRequests() {
        auth = FirebaseAuth.getInstance()
        list.value = mutableListOf()
        database = FirebaseDatabase.getInstance()
        ref = database.reference.child("requests").child(auth.uid!!)
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(dataSnapshot: DataSnapshot in snapshot.children) {
                    val item = snapshot.getValue(ModelClasses::class.java)
                    list.value!!.add(item!!)
                }
                binding.recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun insertInFirebase(data: ModelClasses) {
        database = FirebaseDatabase.getInstance()
        reference = database.reference.child("2021")
        viewModelScope.launch {
            reference.push().setValue(data).addOnCompleteListener(object: OnCompleteListener<Void> {
                override fun onComplete(p0: Task<Void>) {
                    if(p0.isSuccessful) {
                        AddRequestFragment.binding.pb.visibility = View.INVISIBLE
                    }
                }

            })
        }
    }

    val userDetail = dao.getUser()
    val ngoDetail = dao.getNgoUser()

    private fun insertData(data: ModelClasses) {
        viewModelScope.launch {
            dao.insert(data = data)
        }
    }


    fun deleteInFirebase(name: String) {
        deleteFromFirebase(name)
    }

    private fun deleteFromFirebase(name: String) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        ref = database.reference.child("requests").child(auth.uid!!)
            val query: Query =ref.orderByChild("title").equalTo(name)
            query.addListenerForSingleValueEvent(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snap: DataSnapshot in snapshot.children) {
                        snap.ref.removeValue().addOnCompleteListener(object : OnCompleteListener<Void> {
                            override fun onComplete(p0: Task<Void>) {
                                if(p0.isSuccessful) {
                                    Toast.makeText(fragment.requireContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show()
                                }
                            }

                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



        viewModelScope.launch(Dispatchers.IO) {
            val dataRef = database.reference.child("common")
            val q: Query = dataRef.orderByChild("title").equalTo(name)
            q.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snap: DataSnapshot in snapshot.children) {
                        snap.ref.removeValue().addOnCompleteListener(object: OnCompleteListener<Void> {
                            override fun onComplete(p0: Task<Void>) {
                                if(p0.isSuccessful) {
                                }
                            }

                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }



    }
    private fun deleteData(data: ModelClasses) {
        viewModelScope.launch {
            dao.delete(data)
        }
    }
    fun setData(data: ModelClasses) {
        this.data = data
    }

    fun getData(): ModelClasses {
        return data
    }

    val allData: MutableLiveData<MutableList<ModelClasses>> = dao.getAllData().asLiveData() as MutableLiveData<MutableList<ModelClasses>>

    fun setTitle(title: String) {
        this.title = title
    }
    fun getTitle(): String {
        return  title
    }
    fun setDate(date: String) {
        this.date = date
    }
    fun getDate(): String {
        return date
    }
    fun setAddress(add: String) {
        this.add = add
    }
    fun getAddress(): String {
        return add
    }
    fun setDescription(description: String) {
        this.description = description
    }
    fun getDescription(): String {
        return description
    }




}

class CommonViewModelFactory(private val dao: Dao, private val fragment: Fragment) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CommonViewModel::class.java)) {
            return CommonViewModel(dao = dao, fragment) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}