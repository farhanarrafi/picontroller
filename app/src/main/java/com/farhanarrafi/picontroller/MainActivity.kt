package com.farhanarrafi.picontroller

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.farhanarrafi.picontroller.model.Components
import com.farhanarrafi.picontroller.utils.DatabaseUtil
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), RoomDataUpdater, SwitchUpdateListener {

    private var TAG: String = "MainActivity"

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

        /* This must be performed in onCreate*/
        initializeFireBase()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater:MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_add -> {
                addAppliance()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addAppliance() {
        val intent = Intent(this, AddApplianceActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constants.COMPONENT_SINGLE_CHILD_ADDED)
        intentFilter.addAction(Constants.COMPONENT_SINGLE_CHILD_REMOVED)
        intentFilter.addAction(Constants.COMPONENT_SINGLE_CHILD_UPDATE)
        intentFilter.addAction(Constants.COMPONENT_CHILDREN_UPDATED)
        localBroadcastManager.registerReceiver(roomUpdateBroadcastReceiver, intentFilter)
    }

    override fun onStop() {
        localBroadcastManager.unregisterReceiver(roomUpdateBroadcastReceiver)
        super.onStop()
    }


    private fun initializeLayout() {
        recyclerView = layout_recycler_view
        recyclerView.setHasFixedSize(false)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        componentList = getComponentList()
        roomAdapter = RoomAdapter(componentList, this)
        recyclerView.adapter = roomAdapter
    }

    private fun getComponentList(): ArrayList<Components> {
        val componentList: ArrayList<Components> = ArrayList()
        return componentList
    }

    /**
     * Set Child Event Listeners
     */
    private fun initializeFireBase() {
        componentsDB.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError?) {
                Log.d(TAG, "onCancelled(): " + databaseError.toString())
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                val component = dataSnapshot!!.getValue(Components::class.java)
                Log.d(TAG, "onChildMoved(): " + component.toString())
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                try {
                    val component = dataSnapshot!!.getValue(Components::class.java)
                    Log.d(TAG, "onChildChanged()" + component!!.id + " " + component!!.status.toString())
                    sendComponentUpdate(Constants.COMPONENT_SINGLE_CHILD_UPDATE, component!!)
                } catch (ex:Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                try {
                    val component = dataSnapshot!!.getValue(Components::class.java)
                    Log.d(TAG, "onChildAdded()" + component!!.id)
                    sendComponentUpdate(Constants.COMPONENT_SINGLE_CHILD_ADDED, component!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                try {
                    val component = dataSnapshot!!.getValue(Components::class.java)
                    Log.d(TAG, "onChildRemoved(): " + component!!.id)
                    sendComponentUpdate(Constants.COMPONENT_SINGLE_CHILD_REMOVED, component!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

    }

    override fun updateComponentStatus(component: Components) {
        for (item in componentList) {
            if(item.id == component.id) {
                item.status = component.status
                Log.d(TAG, "updateComponentStatus(): " +component.name + component.status)
                break
            }
        }
         // recyclerView.adapter = RoomAdapter(componentList)
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun addComponent(component: Components) {
        Log.d(TAG, "addComponent(): $component")
        componentList.add(component)
        // recyclerView.adapter = RoomAdapter(componentList)
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun removeComponent(component: Components) {
        Log.d(TAG, "removeComponent(): $component")
        componentList.remove(component)
        // recyclerView.adapter = RoomAdapter(componentList)
        recyclerView.adapter.notifyDataSetChanged()
    }

    private fun sendComponentUpdate(intentString: String, component: Components) {
        Log.d(TAG, "sendComponentUpdate(): intentstring: $intentString component $component")
        val intent = Intent(intentString)
        intent.putExtra("component", component)
        localBroadcastManager.sendBroadcast(intent)
    }

    override fun updateLightStatus(id:Int, status: Boolean) {
        Log.d(TAG, "updateLightStatus(): id: $id status $status")
        if(status) {
            componentsDB.child(id.toString()).child("status").setValue(true)
            //holder.iv_component_image.setImageResource(R.drawable.light_on)
        } else {
            componentsDB.child(id.toString()).child("status").setValue(false)
            //holder.iv_component_image.setImageResource(R.drawable.light_off)
        }
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