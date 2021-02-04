package net.sipconsult.sipposorder.data.repository.user

import androidx.lifecycle.LiveData
import net.sipconsult.sipposorder.data.models.LoggedInUser
import net.sipconsult.sipposorder.data.models.SignInBody
import net.sipconsult.sipposorder.internal.Result

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

interface UserRepository {
    fun logout()

    suspend fun login(signInBody: SignInBody): Result<LoggedInUser>
    fun isLoggedIn(): Boolean

    fun setLoggedInUser(loggedInUser: LoggedInUser)

    val loggedInUser: LiveData<LoggedInUser>
}