package reactivecircus.flowbinding.common

import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel

@RestrictTo(LIBRARY_GROUP)
@UseExperimental(ExperimentalCoroutinesApi::class)
fun <E> SendChannel<E>.safeOffer(value: E): Boolean {
    return runCatching { offer(value) }.getOrDefault(false)
}
