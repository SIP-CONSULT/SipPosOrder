package net.sipconsult.sipposorder.ui.base

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class ScopedFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    private lateinit var handler: Handler

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        handler = Handler()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        if (handler != null) {
            handler.removeCallbacksAndMessages(null)
        }
    }
}