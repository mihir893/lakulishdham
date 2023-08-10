package org.lakulishdham.api

interface BaseInterface {

    fun onError(errorString: String?)
    
    fun onDynamicError(errString: String?)
}