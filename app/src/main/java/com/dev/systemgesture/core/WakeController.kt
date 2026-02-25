object WakeController {

    fun wake(context: Context) {
        val intent = Intent(context, WakeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
