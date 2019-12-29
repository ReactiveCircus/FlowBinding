@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.preference

import androidx.annotation.CheckResult
import androidx.preference.Preference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of click events on the [Preference] instance.
 *
 * Note: Created flow keeps a strong reference to the [Preference] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * preference.preferenceClicks()
 *     .onEach {
 *          // handle preference click event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun Preference.preferenceClicks(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = Preference.OnPreferenceClickListener {
        safeOffer(Unit)
    }
    onPreferenceClickListener = listener
    awaitClose { onPreferenceClickListener = null }
}.conflate()
