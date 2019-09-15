package reactivecircus.flowbinding.android.testutil

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.ActionProvider
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import androidx.core.content.ContextCompat

class TestMenuItem(private val context: Context) : MenuItem {

    private var itemId: Int = 0
    private var groupId: Int = 0
    private var order: Int = 0
    private var title: CharSequence? = null
    private var titleCondensed: CharSequence? = null
    private var icon: Drawable? = null
    private var intent: Intent? = null
    private var numericChar: Char = ' '
    private var alphaChar: Char = ' '
    private var checkable: Boolean = false
    private var checked: Boolean = false
    private var visible: Boolean = false
    private var enabled: Boolean = false
    private var menuItemClickListener: MenuItem.OnMenuItemClickListener? = null
    private var actionEnum: Int = 0
    private var actionView: View? = null
    private var actionProvider: ActionProvider? = null
    private var isActionViewExpanded: Boolean = false
    private var actionExpandListener: MenuItem.OnActionExpandListener? = null

    fun performClick() {
        menuItemClickListener?.onMenuItemClick(this)
    }

    override fun expandActionView(): Boolean {
        return when {
            isActionViewExpanded -> true
            actionExpandListener != null && !actionExpandListener!!.onMenuItemActionExpand(this) -> false
            else -> {
                isActionViewExpanded = true
                return true
            }
        }
    }

    override fun hasSubMenu(): Boolean {
        return false
    }

    override fun getMenuInfo(): ContextMenu.ContextMenuInfo? {
        return null
    }

    override fun getItemId(): Int {
        return itemId
    }

    override fun getAlphabeticShortcut(): Char {
        return alphaChar
    }

    override fun setEnabled(enabled: Boolean): MenuItem {
        this.enabled = enabled
        return this
    }

    override fun setTitle(title: CharSequence?): MenuItem {
        this.title = title
        return this
    }

    override fun setTitle(title: Int): MenuItem {
        this.title = context.getText(title)
        return this
    }

    override fun setChecked(checked: Boolean): MenuItem {
        if (checkable) {
            this.checked = checked
        }
        return this
    }

    override fun getActionView(): View? {
        return actionView
    }

    override fun getTitle(): CharSequence? {
        return title
    }

    override fun getOrder(): Int {
        return order
    }

    override fun setOnActionExpandListener(listener: MenuItem.OnActionExpandListener?): MenuItem {
        this.actionExpandListener = listener
        return this
    }

    override fun getIntent(): Intent? {
        return intent
    }

    override fun setVisible(visible: Boolean): MenuItem {
        this.visible = visible
        return this
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun isCheckable(): Boolean {
        return checkable
    }

    override fun setShowAsAction(actionEnum: Int) {
        this.actionEnum = actionEnum
    }

    override fun getGroupId(): Int {
        return groupId
    }

    override fun setActionProvider(actionProvider: ActionProvider?): MenuItem {
        this.actionProvider = actionProvider
        return this
    }

    override fun setTitleCondensed(title: CharSequence?): MenuItem {
        this.titleCondensed = title
        return this
    }

    override fun getNumericShortcut(): Char {
        return numericChar
    }

    override fun isActionViewExpanded(): Boolean {
        return isActionViewExpanded
    }

    override fun collapseActionView(): Boolean {
        return when {
            !isActionViewExpanded -> false
            actionExpandListener != null && !actionExpandListener!!.onMenuItemActionCollapse(this) -> false
            else -> {
                isActionViewExpanded = false
                return true
            }
        }
    }

    override fun isVisible(): Boolean {
        return visible
    }

    override fun setNumericShortcut(numericChar: Char): MenuItem {
        this.numericChar = numericChar
        return this
    }

    override fun setActionView(view: View?): MenuItem {
        this.actionView = view
        return this
    }

    override fun setActionView(resId: Int): MenuItem {
        this.actionView = LayoutInflater.from(context).inflate(resId, null)
        return this
    }

    override fun setAlphabeticShortcut(alphaChar: Char): MenuItem {
        this.alphaChar = alphaChar
        return this
    }

    override fun setIcon(icon: Drawable?): MenuItem {
        this.icon = icon
        return this
    }

    override fun setIcon(iconRes: Int): MenuItem {
        this.icon = ContextCompat.getDrawable(context, iconRes)
        return this
    }

    override fun isChecked(): Boolean {
        return checked
    }

    override fun setIntent(intent: Intent?): MenuItem {
        this.intent = intent
        return this
    }

    override fun setShortcut(numericChar: Char, alphaChar: Char): MenuItem {
        this.numericChar = numericChar
        this.alphaChar = alphaChar
        return this
    }

    override fun getIcon(): Drawable? {
        return icon
    }

    override fun setShowAsActionFlags(actionEnum: Int): MenuItem {
        this.actionEnum = actionEnum
        return this
    }

    override fun setOnMenuItemClickListener(menuItemClickListener: MenuItem.OnMenuItemClickListener?): MenuItem {
        this.menuItemClickListener = menuItemClickListener
        return this
    }

    override fun getActionProvider(): ActionProvider? {
        return actionProvider
    }

    override fun setCheckable(checkable: Boolean): MenuItem {
        this.checkable = checkable
        return this
    }

    override fun getSubMenu(): SubMenu? {
        return null
    }

    override fun getTitleCondensed(): CharSequence? {
        return titleCondensed
    }
}
