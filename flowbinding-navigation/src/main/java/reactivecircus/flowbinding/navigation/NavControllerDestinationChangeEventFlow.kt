@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.navigation

import android.os.Bundle
import androidx.annotation.CheckResult
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of destination change events on the [NavController] instance.
 *
 * Note: Created flow keeps a strong reference to the [NavController] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * navController.destinationChangeEvents()
 *     .onEach { event ->
 *          // handle destination change event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun NavController.destinationChangeEvents(): Flow<DestinationChangeEvent> = callbackFlow {
    checkMainThread()
    val listener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
        trySend(
            DestinationChangeEvent(
                navController = controller,
                destination = destination,
                arguments = arguments
            )
        )
    }
    addOnDestinationChangedListener(listener)
    awaitClose { removeOnDestinationChangedListener(listener) }
}.conflate()

public data class DestinationChangeEvent(
    public val navController: NavController,
    public val destination: NavDestination,
    public val arguments: Bundle?
)
