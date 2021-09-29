package com.rd.event

import android.app.Application
import androidx.test.runner.AndroidJUnitRunner
import com.rd.event.data.remote.EventApiClient
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module
import org.koin.dsl.single
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class ApiTestRunner : AndroidJUnitRunner() {

    override fun callApplicationOnCreate(app: Application?) {
        super.callApplicationOnCreate(app)

        loadKoinModules(module {
            single { provideEventApiClient() }
        })
    }

    private fun provideEventApiClient(): EventApiClient {
        return Retrofit.Builder()
            .baseUrl("https://localhost:$MOCK_WEB_SERVER_PORT/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(EventApiClient::class.java)
    }

    companion object {
        const val MOCK_WEB_SERVER_PORT = 4007
    }
}