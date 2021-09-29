package com.rd.event

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.koin.test.KoinTest

open class BaseUiTest : KoinTest {

    private lateinit var webServer: MockWebServer

    @Before
    @Throws(Exception::class)
    fun server() {
        webServer = MockWebServer()
        webServer.start(ApiTestRunner.MOCK_WEB_SERVER_PORT)
        webServer.dispatcher = retrieveDispatcher()
    }

    private fun retrieveDispatcher(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path) {
                "events" -> MockResponse().setResponseCode(200).setBody("[{\"people\":[],\"date\":1534784400000,\"description\":\"O Patas Dadas estar\u00e1 na Reden\u00e7\u00e3o, nesse domingo, com c\u00e3es para ado\u00e7\u00e3o e produtos \u00e0 venda!\n\nNa ocasi\u00e3o, teremos bottons, bloquinhos e camisetas!\n\nTraga seu Pet, os amigos e o chima, e venha aproveitar esse dia de sol com a gente e com alguns de nossos peludinhos - que estar\u00e3o prontinhos para ganhar o \u2665 de um humano bem legal pra chamar de seu. \n\nAceitaremos todos os tipos de doa\u00e7\u00e3o:\n- guias e coleiras em bom estado\n- ra\u00e7\u00e3o (as que mais precisamos no momento s\u00e3o s\u00eanior e filhote)\n- roupinhas \n- cobertas \n- rem\u00e9dios dentro do prazo de validade\",\"image\":\"http://lproweb.procempa.com.br/pmpa/prefpoa/seda_news/usu_img/Papel%20de%20Parede.png\",\"longitude\":-51.2146267,\"latitude\":-30.0392981,\"price\":29.99,\"title\":\"Feira de ado\u00e7\u00e3o de animais na Reden\u00e7\u00e3o\",\"id\":\"1\"}]")
                "event/1" -> MockResponse().setResponseCode(200).setBody("{\"people\":[],\"date\":1534784400000,\"description\":\"O Patas Dadas estar\u00e1 na Reden\u00e7\u00e3o, nesse domingo, com c\u00e3es para ado\u00e7\u00e3o e produtos \u00e0 venda!\n\nNa ocasi\u00e3o, teremos bottons, bloquinhos e camisetas!\n\nTraga seu Pet, os amigos e o chima, e venha aproveitar esse dia de sol com a gente e com alguns de nossos peludinhos - que estar\u00e3o prontinhos para ganhar o \u2665 de um humano bem legal pra chamar de seu. \n\nAceitaremos todos os tipos de doa\u00e7\u00e3o:\n- guias e coleiras em bom estado\n- ra\u00e7\u00e3o (as que mais precisamos no momento s\u00e3o s\u00eanior e filhote)\n- roupinhas \n- cobertas \n- rem\u00e9dios dentro do prazo de validade\",\"image\":\"http://lproweb.procempa.com.br/pmpa/prefpoa/seda_news/usu_img/Papel%20de%20Parede.png\",\"longitude\":-51.2146267,\"latitude\":-30.0392981,\"price\":29.99,\"title\":\"Feira de ado\u00e7\u00e3o de animais na Reden\u00e7\u00e3o\",\"id\":\"1\"}")
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        webServer.shutdown()
    }
}