package com.honegroupp.familyRegister.view.family

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.ViewFamilyController

/**
 * This class is responsible for the family name change in the view family page logic.
 *
 * */
class FamilyNameChangeDialog(private val uid: String) :
    AppCompatDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.color.fui_bgAnonymous)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * create the dialog
     * */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context as Context)

        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_family_name_change, null)

        //set view
        builder.setView(view)
            .setTitle(R.string.change_family_name)
            .setPositiveButton(R.string.edit_ok) { _, _ -> }

        //when press the button
        view.findViewById<Button>(R.id.view_family_name_change_confirm_Btn)
            .setOnClickListener {
                var newFamilyName =
                        view.findViewById<EditText>(R.id.new_family_name).text
                            .toString()
                ViewFamilyController.changeFamilyName(uid, newFamilyName)
                // make the dialog disappear
                this.dismiss()
            }

        return builder.create()
    }
}
