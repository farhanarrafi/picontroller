package com.farhanarrafi.picontroller

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.farhanarrafi.picontroller.model.Component
import com.farhanarrafi.picontroller.utils.DatabaseUtil
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var roomAdapter: RoomAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var componentsDB: DatabaseReference
    private lateinit var componentOption: FirebaseRecyclerOptions<Component>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseDatabase = DatabaseUtil.getDatabase()
        componentsDB = firebaseDatabase.getReference("components")
        componentsDB.keepSynced(true)
        initializeLayout()
    }

    override fun onStart() {
        super.onStart()
        roomAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        roomAdapter.stopListening()
    }

    private fun initializeLayout() {
        recyclerView = layout_recycler_view
        recyclerView.setHasFixedSize(false)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        componentOption = FirebaseRecyclerOptions.Builder<Component>()
                .setQuery(componentsDB, Component::class.java)
                .build()
        roomAdapter = RoomAdapter(componentOption)
        recyclerView.adapter = roomAdapter
    }
}