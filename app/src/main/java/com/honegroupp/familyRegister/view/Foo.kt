package com.honegroupp.familyRegister.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.FamilyController
import com.honegroupp.familyRegister.view.family.FamilyCreateActivity
import com.honegroupp.familyRegister.view.family.FamilyJoinActivity
import kotlinx.android.synthetic.main.activity_create_join_family.*

class Foo :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_join_family)
    }
}