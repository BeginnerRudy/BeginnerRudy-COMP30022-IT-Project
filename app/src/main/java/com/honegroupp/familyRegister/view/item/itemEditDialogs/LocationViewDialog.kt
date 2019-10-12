package com.honegroupp.familyRegister.view.item.itemEditDialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.honegroupp.familyRegister.R


class LocationViewDialog(private val itemLocation: String) : AppCompatDialogFragment() {
    private var listener: OnChangeClickListener? = null

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
        val view = inflater.inflate(R.layout.dialog_location_view, null)

        view.findViewById<TextView>(R.id.view_location_text).text = itemLocation

        builder.setView(view)
            .setTitle(R.string.edit_dialog_location_text)
            .setPositiveButton(R.string.edit_ok) { _, _ -> }

        view.findViewById<Button>(R.id.view_location_changeBtn).setOnClickListener{
            listener!!.clickOnChangeLocation()
            this.dismiss()
        }

        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as OnChangeClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement OnChangeClickListener")
        }

    }

    interface OnChangeClickListener {
        fun clickOnChangeLocation()
    }
}
