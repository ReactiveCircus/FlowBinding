package reactivecircus.flowbinding.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.isJava
import com.intellij.openapi.util.Ref
import org.jetbrains.uast.UBinaryExpression
import org.jetbrains.uast.UBinaryExpressionWithType
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.UastBinaryExpressionWithTypeKind
import org.jetbrains.uast.UastBinaryOperator
import org.jetbrains.uast.isNullLiteral
import org.jetbrains.uast.visitor.AbstractUastVisitor
import java.util.EnumSet

/**
 * Lint check for detecting missing event listener removal
 * in the `awaitClose` block within a `callbackFlow` implementation.
 *
 * When one of these expressions within a `callbackFlow` block:
 *
 * `set*Listener(*)`
 * `*Listener = *`,
 * `add*Listener(*)`
 * `add*Callback(*)`
 * `add*Observer(*)`
 * `register*(*)`
 *
 * then one of the following must be present in an `awaitClose` block:
 * `set*Listener(null)`
 * `*Listener = null`,
 * `remove*Listener(*)`
 * `remove*Callback(*)`
 * `remove*Observer(*)`
 * `unregister*(*)`
 */
@Suppress("UnstableApiUsage", "ComplexCondition", "ReturnCount")
class MissingListenerRemovalDetector : Detector(), SourceCodeScanner {

    companion object {
        val ISSUE: Issue = Issue.create(
            id = "MissingListenerRemoval",
            briefDescription = "Listener not unregistered, removed, or set to null",
            explanation = """
                Listeners or callbacks should be removed, unregistered or set to null \
                in the `awaitClose` block within the `callbackFlow` implementation.""",
            implementation = Implementation(MissingListenerRemovalDetector::class.java, EnumSet.of(Scope.JAVA_FILE)),
            priority = 10,
            category = Category.CORRECTNESS,
            severity = Severity.ERROR
        )

        private const val CALLBACK_FLOW = "callbackFlow"

        private const val AWAIT_CLOSE = "awaitClose"

        private const val PATTERN_ADD_LISTENER_METHOD =
            "add.+Listener|add.*Callback|add.*Observer|register.+"

        private const val PATTERN_REMOVE_LISTENER_METHOD =
            "remove.+Listener|remove.*Callback|remove.*Observer|unregister.+"

        private const val PATTERN_SET_LISTENER = "set.+Listener"

        private const val SUFFIX_LISTENER = "listener"
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        val psi = context.uastFile?.sourcePsi ?: return null
        if (isJava(psi)) {
            return null
        }

        return KotlinVisitor(context)
    }

    private class KotlinVisitor(val context: JavaContext) : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            if (node.methodIdentifier?.name == CALLBACK_FLOW) {
                if (listenerAdded(node) && !listenerRemoved(node)) {
                    context.report(
                        issue = ISSUE,
                        scope = node,
                        location = context.getLocation(node),
                        message = "A listener or callback has been added within the `callbackFlow`, " +
                                "but it hasn't been removed / unregistered in the `awaitClose` block."
                    )
                }
            }
        }

        fun listenerAdded(node: UCallExpression): Boolean {
            val added = Ref(false)
            node.accept(object : AbstractUastVisitor() {
                override fun visitCallExpression(node: UCallExpression): Boolean {
                    if (isValidAddListenerExpression(node)) {
                        added.set(true)
                    }
                    return super.visitCallExpression(node)
                }

                override fun visitBinaryExpression(node: UBinaryExpression): Boolean {
                    // try to find the pattern `*Listener = listener`
                    if (node.operator is UastBinaryOperator.AssignOperator &&
                        (node.leftOperand as USimpleNameReferenceExpression)
                            .identifier.endsWith(SUFFIX_LISTENER, ignoreCase = true) &&
                        !node.rightOperand.isNullLiteralOrCastedNullLiteral()
                    ) {
                        added.set(true)
                    }

                    return super.visitBinaryExpression(node)
                }
            })
            return added.get()
        }

        fun listenerRemoved(node: UCallExpression): Boolean {
            val removed = Ref(false)
            node.accept(object : AbstractUastVisitor() {
                override fun visitCallExpression(node: UCallExpression): Boolean {
                    if (node.methodIdentifier?.name == AWAIT_CLOSE) {
                        node.accept(object : AbstractUastVisitor() {
                            override fun visitCallExpression(node: UCallExpression): Boolean {
                                if (isValidRemoveListenerExpression(node)) {
                                    removed.set(true)
                                }
                                return super.visitCallExpression(node)
                            }

                            override fun visitBinaryExpression(node: UBinaryExpression): Boolean {
                                // try to find the pattern `*Listener = null`
                                if (node.operator is UastBinaryOperator.AssignOperator &&
                                    (node.leftOperand as USimpleNameReferenceExpression)
                                        .identifier.endsWith(SUFFIX_LISTENER, ignoreCase = true) &&
                                    node.rightOperand.isNullLiteralOrCastedNullLiteral()
                                ) {
                                    removed.set(true)
                                }

                                return super.visitBinaryExpression(node)
                            }
                        })
                    }
                    return super.visitCallExpression(node)
                }
            })
            return removed.get()
        }

        fun isValidAddListenerExpression(node: UCallExpression): Boolean {
            node.methodIdentifier?.run {
                val mightBeAddListenerMethod = name.matches(PATTERN_ADD_LISTENER_METHOD.toRegex()) ||
                        name.matches(PATTERN_SET_LISTENER.toRegex())
                // must have a non-null argument
                if (mightBeAddListenerMethod &&
                    node.valueArgumentCount == 1 &&
                    !node.valueArguments[0].isNullLiteralOrCastedNullLiteral()
                ) {
                    return true
                }
            }
            return false
        }

        fun isValidRemoveListenerExpression(node: UCallExpression): Boolean {
            node.methodIdentifier?.run {
                // must have a non-null argument for a remove* or unregister* method
                // otherwise must have a null argument for a set*Listener method
                if (node.valueArgumentCount == 1 && (
                            name.matches(PATTERN_REMOVE_LISTENER_METHOD.toRegex()) &&
                                    !node.valueArguments[0].isNullLiteralOrCastedNullLiteral() ||
                                    name.matches(PATTERN_SET_LISTENER.toRegex()) &&
                                    node.valueArguments[0].isNullLiteralOrCastedNullLiteral()
                            )
                ) {
                    return true
                }
            }
            return false
        }
    }
}

/**
 * Check if expression is `null` or `null as Type?`
 */
private fun UExpression.isNullLiteralOrCastedNullLiteral(): Boolean {
    return isNullLiteral() ||
            (this is UBinaryExpressionWithType &&
                    operationKind is UastBinaryExpressionWithTypeKind.TypeCast &&
                    operand.isNullLiteral())
}
