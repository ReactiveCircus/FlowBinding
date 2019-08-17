package reactivecircus.flowbinding.common

import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel

@RestrictTo(LIBRARY_GROUP)
@UseExperimental(ExperimentalCoroutinesApi::class)
fun <E> SendChannel<E>.offerIfNotClosed(value: E) = !isClosedForSend && try {
    offer(value)
} catch (e: CancellationException) {
    false
}
