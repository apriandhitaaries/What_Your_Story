package com.example.storyapp.view.upload

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.example.storyapp.R
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.remote.response.FileUploadResponse
import com.example.storyapp.view.main.MainActivity
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UploadActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(UploadActivity::class.java)

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private lateinit var mockViewModel: UploadViewModel
    private lateinit var mockRepository: StoryRepository

    @Before
    fun setUp() {
        Intents.init()

        mockRepository = mockk()
        mockViewModel = spyk(UploadViewModel(mockRepository))

        mockkConstructor(ViewModelProvider::class)
        every {
            anyConstructed<ViewModelProvider>()[UploadViewModel::class.java]
        } returns mockViewModel
    }

    @After
    fun tearDown() {
        Intents.release()
        unmockkAll()
    }

    @Test
    fun testUploadImageFromGallery() {
        onView(withId(R.id.galleryButton)).perform(click())

        Intents.intended(
            allOf(
                hasAction(Intent.ACTION_PICK),
                hasData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            )
        )

        val testImageUri = Uri.parse("android.resource://com.example.storyapp/drawable/test_image")
        Intents.intending(hasAction(Intent.ACTION_PICK)).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                Intent().setData(testImageUri)
            )
        )

        onView(withId(R.id.previewImageView)).check(matches(isDisplayed()))
    }

    @Test
    fun testUploadImageFromCamera() {
        onView(withId(R.id.cameraButton)).perform(click())

        Intents.intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE))

        val testImageUri = Uri.parse("android.resource://com.example.storyapp/drawable/test_image")
        Intents.intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                Intent().putExtra(MediaStore.EXTRA_OUTPUT, testImageUri)
            )
        )

        onView(withId(R.id.previewImageView)).check(matches(isDisplayed()))
    }

    @Test
    fun testUploadStory() = runBlocking {
        val testImageUri = Uri.parse("android.resource://com.example.storyapp/drawable/test_image")
        mockkStatic("android.net.Uri")
        every { Uri.parse(any()) } returns testImageUri

        onView(withId(R.id.descriptionEditText)).perform(typeText("My new story"), closeSoftKeyboard())

        val mockResponse = FileUploadResponse(false,"Upload successful")
        coEvery { mockViewModel.addStory(any(), any(), any(), any()) } returns mockResponse

        onView(withId(R.id.uploadButton)).perform(click())

        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))

        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        Intents.intended(hasComponent(MainActivity::class.java.name))
    }
}
