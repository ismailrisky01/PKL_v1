package com.example.pkl_v1.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.recyclerview.widget.DiffUtil
import com.example.pkl_v1.R
import com.example.pkl_v1.model.ModelQuestion

class MyDiffUtil(private val oldList: List<ModelQuestion>, private val newList: List<ModelQuestion>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].idSoal == newList[newItemPosition].idSoal
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].pilihan1 != newList[newItemPosition].pilihan1 -> {
                false
            }
            oldList[oldItemPosition].pilihan2 != newList[newItemPosition].pilihan2->{
                false
            }
            oldList[oldItemPosition].pilihan3 != newList[newItemPosition].pilihan3->{
                false
            }
            oldList[oldItemPosition].pilihan4 != newList[newItemPosition].pilihan4->{
                false
            }
            oldList[oldItemPosition].pilihan5 != newList[newItemPosition].pilihan5->{
                false
            }oldList[oldItemPosition].dipilih != newList[newItemPosition].dipilih->{
                false
            }
            else->true

        }
    }
}
class LoadingHelper(context: Context) {

    private val dialog = Dialog(context).apply {
        setContentView(R.layout.layout_loading) // TODO("Should Be Change to ViewBinding Inflation")
        setCancelable(false)
    }

    fun show() = show(null, null)

    fun show(title: String) = show(title, null)

    fun show(title: String?, desc: String?) {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

}
fun wait(ms: Int) {
    try {
        Thread.sleep(ms.toLong())
    } catch (ex: InterruptedException) {
        Thread.currentThread().interrupt()
    }
}

fun dialogBuilder(context: Context, nilai: Int) {
    val builder = AlertDialog.Builder(context)
    val positiveButtonClick = { dialog: DialogInterface, which: Int ->

    }
    with(builder)
    {
        setTitle("Total")
        setMessage("" + nilai)
        setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
        show()
    }
}