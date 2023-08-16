package expo.modules.launcharguments

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.core.interfaces.ActivityProvider

import expo.modules.kotlin.Promise
import expo.modules.kotlin.exception.CodedException

import android.content.Intent
import android.app.Activity
import expo.modules.core.errors.CurrentActivityNotFoundException
import java.util.HashMap

const val MODULE_NAME = "ExpoLaunchArguments"

class ExpoLaunchArgumentsModule : Module() {

  init {
        waitForActivity()
    }

    override fun definition() = ModuleDefinition {
        Name(MODULE_NAME)

        Constants {
          return@Constants mapOf(
            "launchArguments" to parseIntentExtras()
          )
        }
    }

    private val DETOX_LAUNCH_ARGS_KEY = "launchArgs"
    private val ACTIVITY_WAIT_INTERVAL = 100L // Can be adjusted as needed
    private val ACTIVITY_WAIT_TRIES = 200 // Can be adjusted as needed


    private fun waitForActivity() {
        var tries = 0
        while (tries < ACTIVITY_WAIT_TRIES && getCurrentActivity() == null) {
            sleep(ACTIVITY_WAIT_INTERVAL)
            tries++
        }
    }

    private fun parseIntentExtras(): HashMap<String, Any> {
        val map = HashMap<String, Any>()

        var activity = getCurrentActivity()
        if(activity == null) {
          return map
        }

        val intent = activity.intent
        if (intent == null) {
          return map
        }

        parseDetoxExtras(map, intent)
        parseADBArgsExtras(map, intent)
        return map
    }

    private fun getCurrentActivity (): Activity {
        return appContext.activityProvider?.currentActivity ?: throw CurrentActivityNotFoundException()
    }

    private fun parseDetoxExtras(map: HashMap<String, Any>, intent: Intent) {
        val bundle = intent.getBundleExtra(DETOX_LAUNCH_ARGS_KEY)
        if (bundle != null) {
            for (key in bundle.keySet()) {
                map[key] = bundle.getString(key) ?: ""
            }
        }
    }

    private fun parseADBArgsExtras(map: HashMap<String, Any>, intent: Intent) {
        val bundleExtras = intent.extras
        if (bundleExtras != null) {
            for (key in bundleExtras.keySet()) {
                if (key != DETOX_LAUNCH_ARGS_KEY && key != "android.nfc.extra.NDEF_MESSAGES") {
                    val value = bundleExtras.get(key)
                    if (value is Int || value is Double || value is Boolean || value is String) {
                        map[key] = value
                    } else if (value != null) {
                        map[key] = value.toString()
                    }
                }
            }
        }
    }

    private fun sleep(ms: Long) {
        try {
            Thread.sleep(ms)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}