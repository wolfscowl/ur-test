package com.wolfscowl.ur_client.model.element

sealed class URResult(val value: String) {
    data class Response(val message: String) : URResult(message) {
        override fun toString(): String = value
    }
    data class Failure(val exception: Exception) : URResult(exception.message ?: "") {
        override fun toString(): String = value
    }

    override fun toString(): String = value

    fun onFailure(action: (Failure)->Unit) {
        if(this is Failure) {
            action(this)
        }
    }


    fun onResponse(action: (Response)->Unit) {
        if(this is Response) {
            action(this)
        }
    }

    fun evaluate(onResponse: (Response) -> Unit, onFailure: (Failure) -> Unit) {
        when(this) {
            is Response-> onResponse(this)
            is Failure -> onFailure(this)
        }
    }

    fun getOrThrow(): String {
        when(this) {
            is Failure -> throw(exception)
            is Response-> return this.message
        }
    }

}