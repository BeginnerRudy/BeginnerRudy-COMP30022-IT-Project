package com.honegroupp.familyRegister.view.item.itemEditDialogs

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


class LocationEnterPasswordDialog : AppCompatDialogFragment() {
    private var editTextPassword: EditText? = null
    private var listener: OnViewClickerListener? = null

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
        val view = inflater.inflate(R.layout.dialog_location_password_required, null)

        builder.setView(view)
            .setTitle(R.string.edit_password_required_for_location)
            .setNeutralButton(R.string.edit_cancel) { dialog, _ ->
                dialog.cancel()
            }

        editTextPassword = view.findViewById(R.id.edit_password)
        view.findViewById<Button>(R.id.view_button).setOnClickListener(){

            val password = editTextPassword!!.text.toString()

           //check the correctness of password
            listener!!.applyPasswords(password,this)

        }

        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as OnViewClickerListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement OnViewClickerListener")
        }

    }

    interface OnViewClickerListener {
        fun applyPasswords(password: String,dialog:LocationEnterPasswordDialog)
    }
}
