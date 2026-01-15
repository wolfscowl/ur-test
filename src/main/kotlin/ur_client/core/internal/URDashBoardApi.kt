package com.wolfscowl.ur_client.core.internal

import com.wolfscowl.ur_client.core.URDashBoard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket


internal class URDashBoardApi(
    private val host: String,
    private val dashBoardPort: Int,
    private val connectTimeout: Int,
    private val soTimeout: Int
) : URDashBoard {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun loadInstallation(installation: String, onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd("load installation $installation", onFailure, onResponse)
    }

    override fun load(program: String, onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd("load $program", onFailure, onResponse)
    }

    override fun play(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd("play", onFailure, onResponse)
    }

    override fun stop(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd("stop", onFailure, onResponse)
    }

    override fun pause(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd("pause", onFailure, onResponse)
    }

    override fun fetchIsRunning(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd("running", onFailure, onResponse)
    }

    override fun fetchProgramState(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd("programState", onFailure, onResponse)
    }

    override fun fetchLoadedProgram(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd("get loaded program", onFailure, onResponse)
    }




    override fun powerOn(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd(
            listOf(
            "close popup",
            "close safety popup",
            "unlock protective stop",
            "power on",
            "brake release"
            ),
            onFailure,
            onResponse
        )
    }

    override fun powerOff(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd("power off", onFailure, onResponse)
    }

    override fun unlockProtectiveStop(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd(
            listOf(
            "unlock protective stop",
            "stop"
            ),
            onFailure,
            onResponse
        )
    }

    override fun fetchRobotModel(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd("get robot model", onFailure, onResponse)
    }

    override fun fetchSerialNumber(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd("get serial number", onFailure, onResponse)
    }

    override fun fetchSafetyStatus(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd("safetystatus", onFailure, onResponse)
    }

    override fun fetchPolyscopeVersion(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd("PolyscopeVersion", onFailure, onResponse)
    }

    override fun shutdown(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd("shutdown", onFailure, onResponse)
    }

    override fun fetchRobotMode(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        sendDashBoardCmd("robotmode", onFailure, onResponse)
    }

    private fun sendDashBoardCmd(cmd: String, onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        scope.launch {
            var urDashBoard: Socket? = null
            try {
                urDashBoard = Socket().apply {
                    soTimeout = this@URDashBoardApi.soTimeout
                    connect(InetSocketAddress(host, dashBoardPort), connectTimeout)
                }
                val inputBuffer = BufferedReader(InputStreamReader(urDashBoard.inputStream))
                val outputWriter = PrintWriter(urDashBoard.outputStream, true)
                inputBuffer.readLine()
                outputWriter.println(cmd)
                val response = inputBuffer.readLine()
                withContext(Dispatchers.Default) {
                    onResponse(response)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Default) {
                    onFailure(e)
                }
            } finally {
                urDashBoard?.close()
            }
        }
    }

    private fun sendDashBoardCmd(cmds: List<String>, onFailure: (Exception) -> Unit, onResponse: (String) -> Unit) {
        scope.launch {
            var urDashBoard: Socket? = null
            val combinedResponse = StringBuilder()

            try {
                urDashBoard = Socket().apply {
                    soTimeout = this@URDashBoardApi.soTimeout
                    connect(InetSocketAddress(host, dashBoardPort), connectTimeout)
                }
                val inputBuffer = BufferedReader(InputStreamReader(urDashBoard.inputStream))
                val outputWriter = PrintWriter(urDashBoard.outputStream, true)
                inputBuffer.readLine()
                cmds.forEachIndexed() { i, cmd ->
                    outputWriter.println(cmd)
                    val response = inputBuffer.readLine()
                    combinedResponse.append(response).append("\n")
                }
                withContext(Dispatchers.Default) {
                    val finalResponse = combinedResponse.toString().trimEnd()
                    onResponse(finalResponse)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Default) {
                    onFailure(e)
                }
            } finally {
                urDashBoard?.close()
            }
        }
    }

}
