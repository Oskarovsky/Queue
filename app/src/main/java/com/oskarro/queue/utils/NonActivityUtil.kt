package com.oskarro.queue.utils

import android.content.Context
import android.content.Intent

object NonActivityUtil {

    fun startNewActivity(context: Context, clazz: Class<*>, extras: String?) {
        val intent = Intent(context, clazz)
        // To pass any data to next activity
        intent.putExtra(Constants.ORDER_NUMBER, extras)
//        intent.putExtra(Constants.INVOICE_NUMBER, extras)
        // start your next activity
        context.startActivity(intent)
    }

    fun startNewActivity(context: Context, clazz: Class<*>) {
        val intent = Intent(context, clazz)
        // start your next activity
        context.startActivity(intent)
    }

    fun startNewActivityUsingAny(context: Context, clazz: Class<Any>) {
        val intent = Intent(context, clazz)
        // start your next activity
        context.startActivity(intent)
    }


}