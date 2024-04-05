import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast

class SharedPreferencesManager private constructor(private val mPref: SharedPreferences) {

    fun saveLoggedInUser(email: String) {
        val editor = mPref.edit()
        editor.putString(LOGGED_IN_USER, email)
        editor.apply()
    }

    fun getLoggedInUser(): String? {
        return mPref.getString(LOGGED_IN_USER, null)
    }

    fun logoutUser() {
        val editor = mPref.edit()
        editor.remove(LOGGED_IN_USER)
        editor.apply()
    }

    companion object {
        private const val LOGGED_IN_USER = "logged_in_user"

        private var mInstance: SharedPreferencesManager? = null

        @Synchronized
        fun getInstance(context: Context, prefName: String): SharedPreferencesManager {
            if (mInstance == null) {
                mInstance = SharedPreferencesManager(context.getSharedPreferences(prefName, Context.MODE_PRIVATE))
            }
            return mInstance!!
        }
    }
}