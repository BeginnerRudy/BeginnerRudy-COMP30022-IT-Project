package com.honegroupp.familyRegister.view.home

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.utility.EmailPathSwitch

/**
 * This class is responsible for the family password change in the view family page logic.
 *
 * */
class UserReAuthDialog(
    private val uid: String,
    private val mActivity: ViewFamilyActivity
) : AppCompatDialogFragment() {

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
        val view = inflater.inflate(R.layout.dialog_change_password_reauth, null)


        builder.setView(view)
            .setTitle(getString(R.string.reauth))
            .setPositiveButton(R.string.edit_ok)
            { _, _ -> }

        view.findViewById<Button>(R.id.reAuth_ok).setOnClickListener {
            var userPassword = view.findViewById<EditText>(R.id.user_password).text.toString()

            if (userPassword.isNullOrEmpty()) {
                Toast.makeText(
                    mActivity,
                    mActivity.getString(R.string.please_enter_the_password),
                    Toast.LENGTH_SHORT
                ).show()
            } else {


                val user = FirebaseAuth.getInstance().currentUser as FirebaseUser

                // Get auth credentials from the user for re-authentication. The example below shows
                // email and password credentials but there are multiple possible providers,
                // such as GoogleAuthProvider or FacebookAuthProvider.

                val credential: AuthCredential = EmailAuthProvider
                    .getCredential(EmailPathSwitch.pathToEmail(uid), userPassword)

                user!!.reauthenticate(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // navigate to family password change dialog
                            val familyNameChangeDialog = FamilyPasswordChangeDialog(uid)
                            familyNameChangeDialog.show(mActivity.supportFragmentManager, "Family Password Change Dialog")
                            // make the dialog disappear
                            this.dismiss()
                        } else {
                            Toast.makeText(
                                mActivity,
                                mActivity.getString(R.string.password_is_incorrect),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        return builder.create()
    }

}
