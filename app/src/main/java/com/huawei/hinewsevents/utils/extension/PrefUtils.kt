package com.huawei.hinewsevents.utils.extension

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.util.*


open class PrefUtils {

    companion object{

        public const val TAG = "PrefUtils"

        private lateinit var spEditor: SharedPreferences.Editor
        private lateinit var preferences: SharedPreferences

        private const val preferencesName = "com.huawei.hinewsevents.preferences"
        private const val editorParamFontSize = "fontSize"

        private const val editorParamFontSizeTitle = "fontSizeTitle"
        private const val editorParamFontSizeContent = "fontSizeContent"
        private const val editorParamFontSizeLitle = "fontSizeLittle"

        fun getPreferencesFontSize(context: Context?): Int {

            preferences = context!!.getSharedPreferences( preferencesName, Context.MODE_PRIVATE )
            // TODO maybe change default size
            val isAnyHaveFontSize: Int = preferences.getInt( editorParamFontSize, 24 )

            Log.i( TAG, "getPreferencesFontSize() : editorParamFontSize : $isAnyHaveFontSize")

            return isAnyHaveFontSize
        }

        fun setPreferencesFontSize(context: Context?, fontSize: Int){
            getPreferencesFontSize(context)
            preferences = context!!.getSharedPreferences( preferencesName, Context.MODE_PRIVATE )
            spEditor = preferences.edit()
            spEditor.putInt( editorParamFontSize , fontSize )
            spEditor.apply()
            Log.i( TAG, "setPreferencesFontSize() : -> spEditor.apply" )

        }


    }

}