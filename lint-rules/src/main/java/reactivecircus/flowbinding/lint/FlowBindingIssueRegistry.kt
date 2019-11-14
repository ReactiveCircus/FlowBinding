package reactivecircus.flowbinding.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

@Suppress("UnstableApiUsage")
class FlowBindingIssueRegistry : IssueRegistry() {
    override val issues: List<Issue> = listOf(MissingListenerRemovalDetector.ISSUE)

    override val api: Int = CURRENT_API
}
