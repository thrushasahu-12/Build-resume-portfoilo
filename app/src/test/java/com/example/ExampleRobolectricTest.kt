package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Thrusha Sahu", appName)
  }

  @Test
  fun `verify portfolio repository has projects and profile`() {
    val projects = com.example.data.PortfolioRepository.projects
    org.junit.Assert.assertTrue(projects.isNotEmpty())
    val profile = com.example.data.PortfolioRepository.candidateProfile
    assertEquals("Thrusha Sahu", profile["name"])
  }
}
