package expo.modules.launcharguments

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.core.interfaces.Package
import expo.modules.core.interfaces.ReactActivityLifecycleListener
import expo.modules.core.interfaces.ActivityProvider
import expo.modules.core.Promise
import expo.modules.core.ExportedModule
import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.content.Intent
import java.util.HashMap

const val MODULE_NAME = "ExpoLaunchArguments"

class ExpoLaunchArgumentsPackage : Package {
    override fun createReactActivityLifecycleListeners(activityContext: Context): List<ReactActivityLifecycleListener> {
        return listOf(ExpoLaunchArgumentsActivityLifecycleListener())
    }
}

class ExpoLaunchArgumentsActivityLifecycleListener : ReactActivityLifecycleListener {
    override fun onCreate(activity: Activity, savedInstanceState: Bundle?) {
        val module = ExpoLaunchArgumentsModule.getModule()
        module.setParsedIntentExtrasFromIntent(activity.intent)
    }
}

class ExpoLaunchArgumentsModule : Module() {
    private val DETOX_LAUNCH_ARGS_KEY = "launchArgs"
    private val ACTIVITY_WAIT_INTERVAL = 100L
    private val ACTIVITY_WAIT_TRIES = 200
    private var parsedIntentExtras: HashMap<String, Any>? = null

     override fun definition() = ModuleDefinition {
      Name(MODULE_NAME)
      Constants {
          waitForActivity()
          val arguments: HashMap<String, Any>
          arguments = HashMap(parsedIntentExtras ?: HashMap())
          return@Constants mapOf(
              "launchArguments" to arguments
          )
      }
    }

    companion object {
        private lateinit var instance: ExpoLaunchArgumentsModule

        fun getModule(): ExpoLaunchArgumentsModule {
            return instance
        }
    }


    fun setParsedIntentExtrasFromIntent(intent: Intent) {
      parsedIntentExtras = parseIntentExtrasFromIntent(intent)
    }

    private fun parseIntentExtrasFromIntent(intent: Intent): HashMap<String, Any> {
      val map = HashMap<String, Any>()

      if (intent == null) {
          return map
      }
      map.put("intent", intent.toString())
      parseDetoxExtras(map, intent)
      parseADBArgsExtras(map, intent)
      return map
    }

    private fun parseDetoxExtras(map: HashMap<String, Any>, intent: Intent) {
      val extrasBundle = intent.getBundleExtra(DETOX_LAUNCH_ARGS_KEY)
      if (extrasBundle != null) {
          map.put("extrasBundle", extrasBundle.toString())
          for (key in extrasBundle.keySet()) {
              map[key] = extrasBundle.getString(key) ?: ""
          }
      }
    }

    private fun parseADBArgsExtras(map: HashMap<String, Any>, intent: Intent) {
      val bundleExtras = intent.extras
      if (bundleExtras != null) {
          map.put("bundleExtras", bundleExtras.toString())
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


    private fun waitForActivity() {
      var tries = 0
      while (tries < ACTIVITY_WAIT_TRIES && parsedIntentExtras === null) {
          sleep(ACTIVITY_WAIT_INTERVAL)
          tries++
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
