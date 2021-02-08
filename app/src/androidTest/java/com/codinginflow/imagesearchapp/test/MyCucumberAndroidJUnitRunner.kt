package com.codinginflow.imagesearchapp.test

import android.os.Bundle
import io.cucumber.android.runner.CucumberAndroidJUnitRunner
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith
import java.io.File


/**
 * The CucumberOptions annotation is mandatory for exactly one of the classes in the test project.
 * Only the first annotated class that is found will be used, others are ignored. If no class is
 * annotated, an exception is thrown. This annotation does not have to placed in runner class
 */
@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["features"],
    glue = ["com.codinginflow.imagesearchapp"]
)
class MyCucumberAndroidJUnitRunner : CucumberAndroidJUnitRunner() {

    override fun onCreate(bundle: Bundle) {
        bundle.putString(
            "plugin",
            getPluginConfigurationString()
        ) // we programmatically create the plugin configuration
        //it crashes on Android R without it
        File(getAbsoluteFilesPath()).mkdirs()
        super.onCreate(bundle)
    }

    private fun getPluginConfigurationString(): String? {
        val cucumber = "cucumber"
        val separator = "--"
        return "junit:" + getCucumberXml(cucumber) + separator +
                "html:" + getCucumberHtml(cucumber) + separator +
                "json:" + getCucumberJson(cucumber)
    }

    private fun getCucumberHtml(cucumber: String): String {
        return getAbsoluteFilesPath() + "/" + cucumber + ".html"
    }

    private fun getCucumberXml(cucumber: String): String {
        return getAbsoluteFilesPath() + "/" + cucumber + ".xml"
    }

    private fun getCucumberJson(cucumber: String): String {
        return getAbsoluteFilesPath() + "/" + cucumber + ".json"
    }

    /**
     * Report is situated in sdcard/Android/data/com.siriusxm.aaos.uiapp
     */
    private fun getAbsoluteFilesPath(): String {
        val directory = targetContext.getExternalFilesDir(null)
        return File(directory, "reports").absolutePath
    }

}