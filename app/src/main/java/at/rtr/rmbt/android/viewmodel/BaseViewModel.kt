package at.rtr.rmbt.android.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import at.rmbt.util.Maybe
import at.rmbt.util.exception.HandledException
import at.rtr.rmbt.android.ui.viewstate.ViewState

open class BaseViewModel : ViewModel() {

    private val viewStates = mutableSetOf<ViewState>()
    private val _errorLiveData = MutableLiveData<HandledException>()

    val errorLiveData: LiveData<HandledException>
        get() = _errorLiveData

    /**
     * Sends an error to default error-handling LiveData
     */
    protected fun postError(error: HandledException) = _errorLiveData.postValue(error)

    /**
     * Send result to given LiveData on success otherwise sends an error to default error-handling LiveData
     */
    protected fun <T> Maybe<T>.postTo(liveData: MutableLiveData<T>) {
        onSuccess { liveData.postValue(it) }
        onFailure { postError(it) }
    }

    /**
     * Should be called in init{} block of ViewModel to add [ViewState] to save/restore states
     */
    fun addStateSaveHandler(vararg viewState: ViewState) {
        viewStates.addAll(viewState)
    }

    /**
     * Should be called to restore states from bundle
     */
    open fun onRestoreState(bundle: Bundle?) {
        viewStates.forEach { it.onRestoreState(bundle) }
    }

    /**
     * Should be called to save states to bundle
     */
    open fun onSaveState(bundle: Bundle?) {
        viewStates.forEach { it.onSaveState(bundle) }
    }
}