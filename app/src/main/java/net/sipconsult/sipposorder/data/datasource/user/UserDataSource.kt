package net.sipconsult.sipposorder.data.datasource.user

import androidx.lifecycle.LiveData
import net.sipconsult.sipposorder.data.models.LoggedInUser
import net.sipconsult.sipposorder.data.models.SignInBody
import net.sipconsult.sipposorder.internal.Result

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
interface UserDataSource {

    suspend fun login(signInBody: SignInBody): Result<LoggedInUser>

    fun logout()

    fun setLoggedInUser(loggedInUser: LoggedInUser)

    val loggedInUser: LiveData<LoggedInUser>
}