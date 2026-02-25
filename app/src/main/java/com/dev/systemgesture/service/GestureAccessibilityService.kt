class GestureAccessibilityService : AccessibilityService() {

    private var lastPress = 0L

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_VOLUME_UP &&
            event.action == KeyEvent.ACTION_DOWN) {

            val now = System.currentTimeMillis()

            if (now - lastPress < 350) {
                WakeController.wake(this)
            }

            lastPress = now
        }
        return false
    }
}
