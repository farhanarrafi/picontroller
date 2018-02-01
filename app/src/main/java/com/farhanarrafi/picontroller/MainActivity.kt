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
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), RoomDataUpdater {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var roomAdapter: RoomAdapter
    private lateinit var componentList: ArrayList<Components>
    private lateinit var roomUpdateBroadcastReceiver: RoomUpdateBroadcastReceiver
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeLayout()

        initializeFirebase()
        roomUpdateBroadcastReceiver = RoomUpdateBroadcastReceiver(this)
        localBroadcastManager = LocalBroadcastManager.getInstance(this)


    }

    override fun onResume() {
        super.onResume()
        localBroadcastManager.registerReceiver(roomUpdateBroadcastReceiver,
                IntentFilter(Constants.COMPONENT_SINGLE_CHILD_UPDATE))
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
        var componentList: ArrayList<Components> = ArrayList<Components>()
        componentList.add(Components("1001","light", "Tube light" ,true, "light"))
        componentList.add(Components("1002","fan", "Wall light",false, "light"))
        componentList.add(Components("1003","light", "Tube light" ,true, "light"))
        componentList.add(Components("1004","fan", "Wall light",false, "light"))
        return componentList
    }

    private fun initializeFirebase() {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var myRef: DatabaseReference = database.getReference("components")

        //val component = Components("light", "Tube light" ,true, "light")
        //myRef.setValue(getComponentList())

        myRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError?) {
                Log.d("onCancelled", databaseError.toString())
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                val component = dataSnapshot!!.getValue(Components::class.java)
                Log.d("onChildMoved", component.toString())
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                val component: Components?
                try {
                    component = dataSnapshot!!.getValue(Components::class.java)
                    Log.d("onChildChanged",component!!.id + " " + component!!.status.toString())
                    sendRoomUpdate(Constants.COMPONENT_SINGLE_CHILD_UPDATE, component)
                } catch (ex:Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                val component = dataSnapshot!!.getValue(Components::class.java)
                Log.d("onChildAdded", component.toString())
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                val component = dataSnapshot!!.getValue(Components::class.java)
                Log.d("onChildRemoved", component.toString())
                sendRoomUpdate(Constants.COMPONENT_SINGLE_CHILD_REMOVED, component!!)
            }
        })

    }

    override fun updateRoomStatus(id: String, status: Boolean) {
        for (component in componentList) {
            if(component.id == id) {
                component.status = status
                Log.d("updateRoomStatus", component.name)
                break
            }
        }
        recyclerView.adapter = RoomAdapter(componentList)
        recyclerView.adapter.notifyDataSetChanged()
        Log.d("notifyDataSetChanged", "Called")
    }

    private fun sendRoomUpdate(intentString: String, component: Components) {
        val intent = Intent(intentString)
        intent.putExtra("id",component.id)
        intent.putExtra("status", component.status)
        localBroadcastManager.sendBroadcast(intent)
    }

    override fun onPause() {
        localBroadcastManager.unregisterReceiver(roomUpdateBroadcastReceiver)
        super.onPause()
    }
}