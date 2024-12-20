package com.bangkit.storyapp.data.database

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bangkit.storyapp.data.api.ApiService
import com.bangkit.storyapp.data.response.AddStoryResponse
import com.bangkit.storyapp.data.response.DetailStoryResponse
import com.bangkit.storyapp.data.response.ListStoryItem
import com.bangkit.storyapp.data.response.LoginResponse
import com.bangkit.storyapp.data.response.RegisterResponse
import com.bangkit.storyapp.data.response.StoryResponse
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalStdlibApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest{
    private var mockApi: ApiService = FakeApiService()
    private var mockDatabase: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(mockDatabase, mockApi)
        val pagingState = PagingState<Int, ListStoryItem>(
            listOf(),
            null,
            PagingConfig(10),
            0,
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDatabase.close()
    }
}

class FakeApiService: ApiService {
    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getStories(page: Int, size: Int): StoryResponse {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                id = "story-$i",
                name = "name $i",
                description = "description $i",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-$i",
                createdAt = "2023-01-01T00:00:00Z",
                lat = null,
                lon = null
            )
            items.add(story)
        }
        return StoryResponse(items)
    }

    override suspend fun addStory(
        description: RequestBody,
        image: MultipartBody.Part,
        lat: Double?,
        lon: Double?
    ): AddStoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getDetailStory(id: String): DetailStoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getStoriesWithLocation(location: Int): StoryResponse {
        TODO("Not yet implemented")
    }

}