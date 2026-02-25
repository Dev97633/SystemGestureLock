class OverlayService : Service() {

    private var lastTap = 0L
    private lateinit var wm: WindowManager
    private lateinit var view: View

    override fun onCreate() {
        super.onCreate()

        startForeground(1, NotificationUtil.create(this))

        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        view = View(this)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        view.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val now = System.currentTimeMillis()
                if (now - lastTap < 300) {
                    LockController.lock(this)
                }
                lastTap = now
            }
            false
        }

        wm.addView(view, params)
    }

    override fun onBind(intent: Intent?) = null
}
