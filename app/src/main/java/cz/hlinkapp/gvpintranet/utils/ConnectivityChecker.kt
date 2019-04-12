package cz.hlinkapp.gvpintranet.utils

import android.net.ConnectivityManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Simple utility class for checking the device's access to the internet.
 */
@Singleton
class ConnectivityChecker @Inject constructor(private val connectivityManager: ConnectivityManager){

    /**
     * Checks whether the device is currently connected to a network.
     * @return True if the device is connected.
     */
    fun isConnected(): Boolean {
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}