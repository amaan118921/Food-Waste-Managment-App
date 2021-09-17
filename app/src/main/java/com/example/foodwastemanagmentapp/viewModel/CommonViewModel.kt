package com.example.foodwastemanagmentapp.viewModel

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.restaurants.AddRequestFragment
import com.example.foodwastemanagmentapp.restaurants.RestaurantHomeFragment
import com.example.foodwastemanagmentapp.room.Dao
import com.example.foodwastemanagmentapp.room.ModelClasses
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

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
    private lateinit var phone: String


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

    private fun insertData(data: ModelClasses) {
        viewModelScope.launch {
            dao.insert(data = data)
        }
    }


    fun deleteInRoomAndFirebase(data:ModelClasses, name: String) {
        deleteData(data)
        deleteFromFirebase(name)
    }
    private fun deleteFromFirebase(name: String) {
        database = FirebaseDatabase.getInstance()
        ref = database.reference.child("2021")
        val query: Query =ref.orderByChild("title").equalTo(name)
        query.addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap: DataSnapshot in snapshot.children) {
                snap.ref.removeValue().addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(p0: Task<Void>) {
                        if(p0.isSuccessful) {
                          RestaurantHomeFragment.binding.pb.visibility = View.INVISIBLE
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

    val allData: LiveData<MutableList<ModelClasses>> = dao.getAllData().asLiveData()

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