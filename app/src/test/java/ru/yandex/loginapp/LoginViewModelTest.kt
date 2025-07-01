package ru.yandex.loginapp

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private val dispatcher = StandardTestDispatcher()

    private companion object {
        const val INVALID_EMAIL = "email"
        const val VALID_EMAIL = "email@yandex.ru"
        const val VALID_PASSWORD = "password"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        viewModel = LoginViewModel()
        Dispatchers.setMain(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `EmptyFieldsError on login empty fields`() = runTest(dispatcher) {
        val actualEmail = ""
        val actualPassword = ""

        viewModel.login(actualEmail, actualPassword)

        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(
            LoginScreenState.EmptyFieldsError, viewModel.state.value
        )
    }

    @Test
    fun `EmailValidationError on incorrect email`() = runTest(dispatcher) {
        viewModel.login(INVALID_EMAIL, VALID_PASSWORD)

        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(
            LoginScreenState.EmailValidationError, viewModel.state.value
        )
    }

    @Test
    fun `login state Loading on correct login`() = runTest(dispatcher) {
        viewModel.login(VALID_EMAIL, VALID_PASSWORD)
        dispatcher.scheduler.runCurrent()

        assertEquals(
            LoginScreenState.Loading, viewModel.state.value
        )
    }

    @Test
    fun `login state Success on correct login`() = runTest(dispatcher) {
        viewModel.login(VALID_EMAIL, VALID_PASSWORD)

        dispatcher.scheduler.runCurrent()

        assertEquals(
            LoginScreenState.Loading, viewModel.state.value
        )

        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            LoginScreenState.Success, viewModel.state.value
        )
    }
}