package com.hantash.echojournal.echo.data.echo

import com.hantash.echojournal.core.database.echo.EchoDao
import com.hantash.echojournal.echo.domain.echo.Echo
import com.hantash.echojournal.echo.domain.echo.EchoDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomEchoDataSource(
    private val echoDao: EchoDao
): EchoDataSource {
    override fun observeEchos(): Flow<List<Echo>> {
        return echoDao
            .observeEchos()
            .map { echoWithTopics ->
                echoWithTopics.map { echoWithTopic ->
                    echoWithTopic.toEcho()
                }
            }
    }

    override fun observeTopics(): Flow<List<String>> {
        return echoDao
            .observeTopics()
            .map { topicEntities ->
                topicEntities.map { it.topic }
            }
    }

    override fun searchTopics(query: String): Flow<List<String>> {
        return echoDao
            .searchTopics(query)
            .map { topicEntities ->
                topicEntities.map { it.topic }
            }
    }

    override suspend fun insertEcho(echo: Echo) {
        echoDao.insertEchoWithTopics(echo.toEchoWithTopics())
    }

}