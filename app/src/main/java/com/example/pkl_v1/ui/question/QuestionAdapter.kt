package com.example.pkl_v1.ui.question

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pkl_v1.databinding.ItemQuestionBinding
import com.example.pkl_v1.model.ModelQuestion
import com.example.pkl_v1.ui.alarm.PilihanListener
import com.example.pkl_v1.util.MyDiffUtil

class QuestionAdapter : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {
    private var questionList = emptyList<ModelQuestion>()
    private var listener: PilihanListener? = null

    fun setData(newQuestionList: List<ModelQuestion>, listener: PilihanListener) {
//        val diffUtil= MyDiffUtil(questionList,newQuestionList)
//        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.questionList = newQuestionList
//        diffResult.dis(this)
        this.listener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, listener!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = questionList[position]
        holder.bind(currentData)
        holder.setIsRecyclable(false)
        if (currentData.pilihan1 == "") {
            holder.binding.IDItemQuestionPilihan1.visibility = View.GONE
        } else {
            holder.binding.IDItemQuestionPilihan1.text = currentData.pilihan1

        }
        if (currentData.pilihan2 == "") {
            holder.binding.IDItemQuestionPilihan2.visibility = View.GONE
        } else {
            holder.binding.IDItemQuestionPilihan2.text = currentData.pilihan2

        }
        if (currentData.pilihan3 == "") {
            holder.binding.IDItemQuestionPilihan3.visibility = View.GONE
        } else {
            holder.binding.IDItemQuestionPilihan3.text = currentData.pilihan3

        }
        if (currentData.pilihan4 == "") {
            holder.binding.IDItemQuestionPilihan4.visibility = View.GONE
        } else {
            holder.binding.IDItemQuestionPilihan4.text = currentData.pilihan4

        }
        if (currentData.pilihan5 == "") {
            holder.binding.IDItemQuestionPilihan5.visibility = View.GONE
        } else {
            holder.binding.IDItemQuestionPilihan5.text = currentData.pilihan5

        }
        holder.binding.IDItemQuestionTxtSoal.text = "${currentData.noSoal} ${currentData.soal}"

    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    class ViewHolder(val binding: ItemQuestionBinding, listener: PilihanListener) :
        RecyclerView.ViewHolder(binding.root) {
        private var listener: PilihanListener? = null

        fun bind(modelQuestion: ModelQuestion) {
            when (modelQuestion.dipilih) {
                1 -> binding.IDItemQuestionPilihan1.isChecked = true
                2 -> binding.IDItemQuestionPilihan2.isChecked = true
                3 -> binding.IDItemQuestionPilihan3.isChecked = true
                4 -> binding.IDItemQuestionPilihan4.isChecked = true
                5 -> binding.IDItemQuestionPilihan5.isChecked = true
            }
            binding.IDItemQuestionPilihan1.setOnClickListener {
                listener?.pilihan(
                    data(modelQuestion, 1)
                )
            }
            binding.IDItemQuestionPilihan2.setOnClickListener {
                listener?.pilihan(
                    data(modelQuestion, 2)
                )
            }
            binding.IDItemQuestionPilihan3.setOnClickListener {
                listener?.pilihan(
                    data(modelQuestion, 3)
                )
            }
            binding.IDItemQuestionPilihan4.setOnClickListener {
                listener?.pilihan(
                    data(modelQuestion, 4)
                )
            }
            binding.IDItemQuestionPilihan5.setOnClickListener {
                listener?.pilihan(
                    data(modelQuestion, 5)
                )
            }
        }

        fun data(modelQuestion: ModelQuestion, pilihan: Int): ModelQuestion {
            return ModelQuestion(
                modelQuestion.idSoal,
                modelQuestion.noSoal,
                modelQuestion.soal,
                modelQuestion.pilihan1,
                modelQuestion.pilihan2,
                modelQuestion.pilihan3,
                modelQuestion.pilihan4,
                modelQuestion.pilihan5,
                pilihan
            )
        }

        init {
            this.listener = listener
        }


    }
}