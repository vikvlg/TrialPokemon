package ru.vik.trials.pokemon.ui.common

import android.util.SparseArray
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

// Обеспечивает доступ к активным ViewHolder в RecyclerView.
abstract class RecyclerViewHolderHelper<VH: RecyclerView.ViewHolder>
    : RecyclerView.Adapter<VH>() {

    private var mHoldersByPosition = SparseArray<WeakReference<VH>>()

    override fun onBindViewHolder(holder: VH, position: Int) {
        mHoldersByPosition.put(position, WeakReference(holder))
    }

    /**
     * Возвращает представление.
     *
     * По сути метод дублирует [RecyclerView.findViewHolderForAdapterPosition].
     * @param position Позиция представления в RecyclerView.
     */
    protected fun getHolderAtPosition(position: Int): VH? {
        val holderRef = mHoldersByPosition[position]
        if (null == holderRef) {
            mHoldersByPosition.remove(position)
            return null
        }

        val holder = holderRef.get()
        if (null == holder || (holder.adapterPosition != position && holder.adapterPosition != RecyclerView.NO_POSITION)) {
            mHoldersByPosition.remove(position)
            return null
        }

        return holder
    }

    /** Возвращает количество представлений. */
    protected fun getHolderCount(): Int {
        return mHoldersByPosition.size()
    }

    /**
     * Возвращает представление.
     * @param index Индекс представления.
     */
    protected fun getHolderAtIndex(index: Int): VH? {
        val key = mHoldersByPosition.keyAt(index)
        val holderRef = mHoldersByPosition[key]
        if (null == holderRef) {
            mHoldersByPosition.remove(index)
            return null
        }

        val holder = holderRef.get()
        if (null == holder) {
            mHoldersByPosition.remove(index)
            return null
        }

        return holder
    }
}