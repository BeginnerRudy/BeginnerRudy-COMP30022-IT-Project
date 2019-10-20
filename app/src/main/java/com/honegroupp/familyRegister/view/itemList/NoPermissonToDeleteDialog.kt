package com.honegroupp.familyRegister.view.itemList

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.honegroupp.familyRegister.R

/**
 * This class is responsible for telling the user that he/she has no permission
 * to delete the selected item, since he/she is not the owner of the item.
 *
 * */
class NoPermissonToDeleteDialog() : AppCompatDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.color.fui_bgAnonymous)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context as Context)

        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_no_permission_delete, null)


        builder.setView(view)
            .setTitle(
                getString(R.string.tip)
            )

        view.findViewById<Button>(R.id.ok_button).setOnClickListener {
            // make the dialog disappear
            this.dismiss()
        }

        return builder.create()
    }
}
