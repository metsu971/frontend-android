package com.example.frontapp.viewmodel

import com.example.frontapp.model.User
import com.example.frontapp.repository.FakeUserRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeUserRepository
    private lateinit var viewModel: UserViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeUserRepository()
        viewModel = UserViewModel(fakeRepository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addUser should update user list`() = runTest {
        val user = User(id = 1L, name="Taro")
        viewModel.addUser(user)
        advanceUntilIdle()

        val expected = listOf(user)
        assertEquals(expected, viewModel.users.value)
    }

    @Test
    fun `deleteUser should remove user from list`() = runTest {
        val user = User(id = 1L, name = "Hanako")
        viewModel.addUser(user)
        advanceUntilIdle()

        viewModel.deleteUser(user.id!!)
        advanceUntilIdle()

        val expected = emptyList<User>()
        assertEquals(expected, viewModel.users.value)
    }

    @Test
    fun `updateUser should modify user in list`() = runTest {
        val user = User(id = 1L, name = "Ken")
        viewModel.addUser(user)
        advanceUntilIdle() // ← ここで ViewModel の StateFlow に反映される

        val updated = user.copy(name = "Updated Ken")
        viewModel.updateUser(updated)
        advanceUntilIdle()

        assertEquals("Updated Ken", viewModel.users.value.first().name)
    }

}