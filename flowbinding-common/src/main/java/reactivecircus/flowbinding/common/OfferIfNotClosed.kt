package reactivecircus.flowbinding.common

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel

@UseExperimental(ExperimentalCoroutinesApi::class)
fun <E> SendChannel<E>.offerIfNotClosed(value: E) = !isClosedForSend && try {
    offer(value)
} catch (e: CancellationException) {
    false
}
