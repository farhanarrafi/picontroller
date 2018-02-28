package com.farhanarrafi.picontroller

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.farhanarrafi.picontroller.model.Components
import com.farhanarrafi.picontroller.utils.DatabaseUtil
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), RoomDataUpdater {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var roomAdapter: RoomAdapter
    private lateinit var componentList: ArrayList<Components>
    private lateinit var roomUpdateBroadcastReceiver: RoomUpdateBroadcastReceiver
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var componentsDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseDatabase = DatabaseUtil.getDatabase()
        componentsDB = firebaseDatabase.getReference("components")
        componentsDB.keepSynced(true)
        initializeLayout()

        roomUpdateBroadcastReceiver = RoomUpdateBroadcastReceiver(this)
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
    }

    override fun onResume() {
        super.onResume()
        initializeFireBase()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constants.COMPONENT_SINGLE_CHILD_ADDED)
        intentFilter.addAction(Constants.COMPONENT_SINGLE_CHILD_REMOVED)
        intentFilter.addAction(Constants.COMPONENT_SINGLE_CHILD_UPDATE)
        intentFilter.addAction(Constants.COMPONENT_CHILDREN_UPDATED)
        localBroadcastManager.registerReceiver(roomUpdateBroadcastReceiver, intentFilter)
    }

    private fun initializeLayout() {
        recyclerView = layout_recycler_view
        recyclerView.setHasFixedSize(false)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        componentList = getComponentList()
        roomAdapter = RoomAdapter(componentList)
        recyclerView.adapter = roomAdapter
    }

    private fun getComponentList(): ArrayList<Components> {
        val componentList: ArrayList<Components> = ArrayList()
        return componentList
    }

    private fun initializeFireBase() {
        componentsDB.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError?) {
                //Log.d("onCancelled", databaseError.toString())
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                val component = dataSnapshot!!.getValue(Components::class.java)
                //Log.d("onChildMoved", component.toString())
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                try {
                    val component = dataSnapshot!!.getValue(Components::class.java)
                    //Log.d("onChildChanged",component!!.id + " " + component!!.status.toString())
                    sendComponentUpdate(Constants.COMPONENT_SINGLE_CHILD_UPDATE, component!!)
                } catch (ex:Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                val component = dataSnapshot!!.getValue(Components::class.java)
                //Log.d("onChildAdded", component!!.id)
                sendComponentUpdate(Constants.COMPONENT_SINGLE_CHILD_ADDED, component!!)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                val component = dataSnapshot!!.getValue(Components::class.java)
                //Log.d("onChildRemoved", component!!.id)
                sendComponentUpdate(Constants.COMPONENT_SINGLE_CHILD_REMOVED, component!!)
            }
        })

    }

    override fun updateComponentStatus(component: Components) {
        for (item in componentList) {
            if(item.id == component.id) {
                item.status = component.status
                Log.d("updateComponentStatus", component.name + component.status)
                break
            }
        }
        recyclerView.adapter = RoomAdapter(componentList)
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun addComponent(component: Components) {
        componentList.add(component)
        recyclerView.adapter = RoomAdapter(componentList)
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun removeComponent(component: Components) {
        componentList.remove(component)
        recyclerView.adapter = RoomAdapter(componentList)
        recyclerView.adapter.notifyDataSetChanged()
    }

    private fun sendComponentUpdate(intentString: String, component: Components) {
        val intent = Intent(intentString)
        intent.putExtra("component", component)
        localBroadcastManager.sendBroadcast(intent)
    }

    override fun onPause() {
        localBroadcastManager.unregisterReceiver(roomUpdateBroadcastReceiver)
        super.onPause()
    }

    /*private fun getComponentList(): ArrayList<Components> {


        val componentList: ArrayList<Components> = ArrayList()
        componentList.add(Components("1001","light", "Tube light" ,true, "light"))
        componentList.add(Components("1002","fan", "Wall light",false, "light"))
        componentList.add(Components("1003","light", "Tube light" ,true, "light"))
        componentList.add(Components("1004","fan", "Wall light",false, "light"))

        return componentList
    }*/
}