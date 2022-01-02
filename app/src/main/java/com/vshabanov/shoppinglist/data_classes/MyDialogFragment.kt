package com.vshabanov.shoppinglist.data_classes

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MyDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        return builder
            .setTitle("Диалоговое окно")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage("Для закрытия окна нажмите ОК")
            .setPositiveButton("OK", null)
            .setNegativeButton("Отмена", null)
            .create();
    }
}