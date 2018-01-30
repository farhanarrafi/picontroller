package com.farhanarrafi.picontroller

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.farhanarrafi.picontroller.model.Components
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout_recycler_view.setHasFixedSize(false)
        layoutManager = LinearLayoutManager(this)
        layout_recycler_view.layoutManager = layoutManager

        layout_recycler_view.adapter = RoomAdapter(getComponentList())
    }

    private fun getComponentList(): ArrayList<Components> {
        var componentList: ArrayList<Components> = ArrayList<Components>()
        componentList.add(Components(resources.getDrawable(R.drawable.light_on),"light", "Tube light" ,true))
        componentList.add(Components(resources.getDrawable(R.drawable.light_off),"fan", "Wall light",false))
        componentList.add(Components(resources.getDrawable(R.drawable.light_on),"light", "Tube light" ,true))
        componentList.add(Components(resources.getDrawable(R.drawable.light_off),"fan", "Wall light",false))
        return componentList
    }
}